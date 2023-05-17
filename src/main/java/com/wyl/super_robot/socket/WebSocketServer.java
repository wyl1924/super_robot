package com.wyl.super_robot.socket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyl.super_robot.openai.ChatGPTStreamUtil;
import com.wyl.super_robot.openai.entity.LocalCache;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.Message;
import com.wyl.super_robot.openai.enums.MessageRole;
import com.wyl.super_robot.utils.JsonConvertKeyUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @创建人 王延领
 * @创建时间 2021/04/14
 * 描述
 **/

@Slf4j
@Component
@ServerEndpoint("/ws")
public class WebSocketServer {
    private static ChatGPTStreamUtil chatGPTStrreamUtil;

    @Autowired
    public void setOrderService(ChatGPTStreamUtil openAiStreamClient) {
        this.chatGPTStrreamUtil = openAiStreamClient;
    }

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    public static ConcurrentHashMap<String, SocketSession> webSocketMap = new ConcurrentHashMap<>();

    private static ObjectMapper objectMapper = new ObjectMapper();

    private ConcurrentHashMap<String, Method> serviceMap = new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen(Session session) {
        try {
            modify(session, "id", new String(UUID.randomUUID() + ""));
            //暂时没用
            SocketSession scoketSession = new SocketSession();
            scoketSession.setSession(session);
            webSocketMap.put(session.getId(), scoketSession);
            addOnlineCount();
        } catch (Exception e) {
            log.error(e.toString());
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug(message);
        try {
            if (!message.contains("{")) {
                log.info("message:{}", message);
                String messageContext = (String) LocalCache.CACHE.get(session.getId());
                List<Message> messages = new ArrayList<>();
                if (StrUtil.isNotBlank(messageContext)) {
                    messages = JSONUtil.toList(messageContext, Message.class);
                    if (messages.size() >= 10) {
                        messages = messages.subList(1, 10);
                    }
                    Message currentMessage = Message.builder().content(message).role(MessageRole.USER.getValue()).build();
                    messages.add(currentMessage);
                } else {
                    Message currentMessage = Message.builder().content(message).role(MessageRole.USER.getValue()).build();
                    messages.add(currentMessage);
                }
                chatGPTStrreamUtil.chat(messages, session.getId(), session);
                LocalCache.CACHE.put(session.getId(), JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);
            } else {
                var chatCompletion = JSONObject.parseObject(message, ChatCompletion.class);
                log.info("chatCompletion:{}", message);
                if (chatCompletion.getKey() != null && !chatCompletion.getKey().isEmpty() && !ObjectUtils.nullSafeEquals(chatCompletion.getKey(), chatGPTStrreamUtil.getToken())) {
                    chatGPTStrreamUtil.setToken(chatCompletion.getKey());
                    chatGPTStrreamUtil.init();
                }
                chatCompletion.setKey(null);
                chatGPTStrreamUtil.chat(chatCompletion, session);
            }
        } catch (Exception e) {
            log.error(e.toString());
            log.error("", e);
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("连接异常...");
        error.printStackTrace();
        SocketSession socketSession = webSocketMap.get(session.getId());
        if (socketSession != null) {
            // 将map中的连接信息删除掉
            webSocketMap.remove(session.getId());
            subOnlineCount();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("连接关闭");
        SocketSession socketSession = webSocketMap.get(session.getId());
        if (socketSession != null) {
            // 将map中的连接信息删除掉
            webSocketMap.remove(session.getId());
            subOnlineCount();
        }
    }

    /**
     * 实现服务器主动推送
     */
    public static void sendMessage(String message) throws IOException {
        Enumeration<String> keys = webSocketMap.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Session sessionValue = webSocketMap.get(key).getSession();
            if (sessionValue.isOpen()) {
                sessionValue.getBasicRemote().sendText(message);
            } else {
                sessionValue.close();
                webSocketMap.remove(key);
            }
        }
    }

    /**
     * 发送自定义消息
     */
    @SuppressWarnings("deprecation")
    public static void sendInfo(String message, String id) throws IOException {
        System.out.println("发送消息到:" + id + "，内容:" + message);
        if (!StringUtils.isEmpty(id) && webSocketMap.containsKey(id)) {
            webSocketMap.get(id).getSession().getBasicRemote().sendText(message);
            // webSocketServer.SendMessage(message);
        }
    }

    public static void send(Session session, Object obj) {
        try {
            session.getBasicRemote()
                    .sendText(obj.getClass().getSimpleName() + JsonConvertKeyUtil.convertJSONKeyRetrunString(obj, true));
        } catch (Exception e) {
            System.out.println("发送失败");
        }
    }


    /**
     * 广播给web端
     */
    public static Boolean broadcastToWeb2(Object msgObj) {
        try {
            List<SocketSession> scoketSessionList = new ArrayList<>();
            ConcurrentHashMap<String, SocketSession> webSocketMap = WebSocketServer.webSocketMap;
            for (Map.Entry map : webSocketMap.entrySet()) {
                SocketSession scoketSession = (SocketSession) map.getValue(); // 获取值
                scoketSessionList.add(scoketSession);
            }
            if (scoketSessionList.size() > 0) {
                for (SocketSession scoketSession : scoketSessionList) {
                    scoketSession.send(msgObj);
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public static void modify(Object object, String fieldName, Object newFieldValue) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, newFieldValue);
        System.out.println(field.get(object));
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}

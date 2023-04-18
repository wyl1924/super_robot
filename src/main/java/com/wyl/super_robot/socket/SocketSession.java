package com.wyl.super_robot.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyl.super_robot.utils.JsonConvertKeyUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Collection;

/**
 * @author zhangzeyin
 * @ClassName: ScoketSession
 * @Description: ScoketSession 扩展
 * @date 2021年5月6日
 */
@Data
@Slf4j
public class SocketSession {

    private Session session;

    private Integer deviceId;

    private Boolean checked = false;

    private String userId;

    private Integer subtype;

    private String remoteHost;

    public Integer isGuest;

    public void send(Object obj) {
        synchronized (this.session) {
            try {
                String msg = "{\"method\":\"" + JsonConvertKeyUtil.stringUtilMin(obj.getClass().getSimpleName())
                        + "\",\"param\":" + JSON.toJSONString(obj) + "}";
                log.debug(msg);
                if (this.session.isOpen())
                    this.session.getBasicRemote().sendText(msg);
            } catch (Exception e) {
                log.error("发送失败", e);
            }
        }
    }

    public void send(String className, Object obj) {
        synchronized (this.session) {
            try {
                String msg = "{\"method\":\"" + JsonConvertKeyUtil.stringUtilMin(className)
                        + "\",\"param\":" + JSON.toJSONString(obj) + "}";
                log.debug(msg);
                if (this.session.isOpen())
                    this.session.getBasicRemote().sendText(msg);
            } catch (Exception e) {
                log.error("发送失败", e);
            }
        }
    }

    public void send(String className, JSONObject obj) {
        synchronized (this.session) {
            try {
                String msg = "{\"method\":\"" + JsonConvertKeyUtil.stringUtilMin(className) + "\",\"param\":"
                        + obj.toJSONString() + "}";
                System.out.println("消息内容为：" + msg);
                log.debug(msg);
                if (this.session.isOpen())
                    this.session.getBasicRemote().sendText(msg);
            } catch (Exception e) {
                log.error("发送失败", e);
            }
        }
    }

    public static void sendByUserId(Object obj, Integer userId) {

        WebSocketServer.webSocketMap.values().stream().filter(o -> userId.equals(o.getUserId())).forEach(o -> {
            try {
                if (o.getSession().isOpen()) {
                    o.send(obj);
                }
            } catch (Exception e) {
                log.error("发送失败", e);
            }
        });

    }


    public static void sendByUserIdAndClassName(Object obj,String className, Integer userId) {

        WebSocketServer.webSocketMap.values().stream().filter(o -> userId.equals(o.getUserId())).forEach(o -> {
            try {
                if (o.getSession().isOpen()) {
                    o.send(className,obj);
                }
            } catch (Exception e) {
                log.error("发送失败", e);
            }
        });

    }

    public static void broadcastToWeb(Object jsonObject) {
        WebSocketServer.webSocketMap.values().stream().forEach(x -> {
            if (x.getSession().isOpen())
                    x.send(jsonObject);
        });
    }

    public static void broadcastToAllWeb(String className, JSONObject jsonObject) {
        Collection<SocketSession> values = WebSocketServer.webSocketMap.values();
        for (SocketSession v : values) {
            if (v.getSession().isOpen()) {
                v.send(className, jsonObject);
            }
        }
    }
}

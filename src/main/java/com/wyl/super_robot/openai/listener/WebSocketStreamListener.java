package com.wyl.super_robot.openai.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyl.super_robot.openai.entity.chat.ChatCompletionResponse;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @author wyl
 * @date 2023/4/17 13:40
 */
@Slf4j
public class WebSocketStreamListener  extends AbstractStreamListener {

    private Session session;

    public WebSocketStreamListener(Session session) {
        this.session = session;
    }
    /**
     * Called when a new message is received.
     * 收到消息 单个字
     *
     * @param message the new message
     */
    @Override
    public void onMsg(String message) {
        try {
            log.info("OpenAI返回数据：{}", message);
            if (message.equals("[DONE]")) {
                log.info("OpenAI返回数据结束了");
                session.getBasicRemote().sendText("[DONE]");
                return;
            }
//            ObjectMapper mapper = new ObjectMapper();
//            ChatCompletionResponse completionResponse = mapper.readValue(message, ChatCompletionResponse.class); // 读取Json
//            String delta = mapper.writeValueAsString(completionResponse.getChoices().get(0).getDelta());
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when an error occurs.
     * 出错时调用
     *
     * @param throwable the throwable that caused the error
     * @param response  the response associated with the error, if any
     */
    @Override
    public void onError(Throwable throwable, String response) {

    }
}

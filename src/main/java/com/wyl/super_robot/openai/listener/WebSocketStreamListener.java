package com.wyl.super_robot.openai.listener;

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
            if (message.equals("[DONE]")) {
                session.getBasicRemote().sendText("[DONE]");
                return;
            }
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

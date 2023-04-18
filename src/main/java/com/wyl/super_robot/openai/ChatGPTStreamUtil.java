package com.wyl.super_robot.openai;


import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.Message;
import com.wyl.super_robot.openai.listener.ConsoleStreamListener;
import com.wyl.super_robot.openai.listener.WebSocketStreamListener;
import com.wyl.super_robot.utils.Proxys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.websocket.Session;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class ChatGPTStreamUtil {
    @Value("${openai.secret_key}")
    private String token;
    @Value("${openai.apiHost}")
    private String apiHost;
    private ChatGPTStream chatGPTStream;

    @Value("${proxy.ip}")
    private String proxyIp;
    @Value("${proxy.port}")
    private Integer proxyPort;

    @PostConstruct
    public void init(){
        //如果在国内访问，使用这个
        if(!StringUtils.isEmpty(proxyIp)){
            Proxy proxy = Proxys.http(proxyIp, proxyPort);
            chatGPTStream = ChatGPTStream.builder()
                    .apiKey(token)
                    .timeout(900)
                    .proxy(proxy)
                    .apiHost(apiHost)
                    .build()
                    .init();
        }else{
            chatGPTStream = ChatGPTStream.builder()
                    .apiKey(token)
                    .timeout(900)
                    .apiHost(apiHost)
                    .build()
                    .init();
        }
    }
    public void chat(String userMessage, String user, Session session) {
        WebSocketStreamListener listener = new WebSocketStreamListener(session);
        Message message = Message.of(userMessage);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .user(user)
                .messages(Arrays.asList(message))
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getMsg())
                .build();
        chatGPTStream.streamChatCompletion(chatCompletion, listener);
    }

    public void chat(List<Message> messages, String user, Session session) {
        WebSocketStreamListener listener = new WebSocketStreamListener(session);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .user(user)
                .messages(messages)
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getMsg())
                .stream(true).build();
        chatGPTStream.streamChatCompletion(chatCompletion, listener);
    }


}

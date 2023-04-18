package com.wyl.super_robot.openai;

import com.wyl.super_robot.openai.entity.AuthenticationBean;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.Message;
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

/**
 * @author wyl
 * @date 2023/4/18 15:23
 */
@Slf4j
@Component
public class OkClientUnit { @Value("${openai.secret_key}")
private String token;
    @Value("${openai.apiHost}")
    private String apiHost;
    private OkClientStream chatGPTStream;

    @Value("${proxy.ip}")
    private String proxyIp;
    @Value("${proxy.port}")
    private Integer proxyPort;

    @PostConstruct
    public void init(){
        //如果在国内访问，使用这个
        if(!StringUtils.isEmpty(proxyIp)){
            Proxy proxy = Proxys.http(proxyIp, proxyPort);
            chatGPTStream = OkClientStream.builder()
                    .apiKey(token)
                    .timeout(900)
                    .proxy(proxy)
                    .apiHost(apiHost)
                    .build()
                    .init();
        }else{
            chatGPTStream = OkClientStream.builder()
                    .apiKey(token)
                    .timeout(900)
                    .apiHost(apiHost)
                    .build()
                    .init();
        }
    }


    public void chat(String msg, String user, Session session) {
        WebSocketStreamListener listener = new WebSocketStreamListener(session);
        AuthenticationBean authenticationBean1 = AuthenticationBean.builder()
                        .password("123456")
                                .username("wyl").authType(1).build();
        chatGPTStream.streamChatCompletion(authenticationBean1, listener);
    }


}

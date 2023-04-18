package com.wyl.super_robot.openai;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyl.super_robot.openai.api.Api;
import com.wyl.super_robot.openai.entity.AuthenticationBean;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

import java.net.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author wyl
 * @date 2023/4/18 15:28
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OkClientStream {
    private String apiKey;
    private List<String> apiKeyList;

    private OkHttpClient okHttpClient;
    /**
     * 连接超时
     */
    @Builder.Default
    private long timeout = 90;

    /**
     * 网络代理
     */
    @Builder.Default
    private Proxy proxy = Proxy.NO_PROXY;
    /**
     * 反向代理
     */
    @Builder.Default
    private String apiHost = Api.DEFAULT_API_HOST;

    /**
     * 初始化
     */
    public OkClientStream init() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        if (Objects.nonNull(proxy)) {
            client.proxy(proxy);
        }

        okHttpClient = client.build();
        return this;
    }
    public void streamChatCompletion(AuthenticationBean chatCompletion,
                                     EventSourceListener eventSourceListener) {
        try {
            EventSource.Factory factory = EventSources.createFactory(okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(chatCompletion);


            Request request = new Request.Builder()
                    .url(apiHost + "bp/log/login")
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
                            requestBody))

                    .build();
            factory.newEventSource(request, eventSourceListener);

        } catch (Exception e) {
            log.error("请求出错：{}", e);
        }
    }
}

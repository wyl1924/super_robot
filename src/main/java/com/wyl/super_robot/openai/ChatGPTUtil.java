package com.wyl.super_robot.openai;


import com.wyl.super_robot.openai.entity.billing.BillingUsage;
import com.wyl.super_robot.openai.entity.billing.CreditGrantsResponse;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.ChatCompletionResponse;
import com.wyl.super_robot.openai.entity.chat.Message;
import com.wyl.super_robot.openai.entity.models.ListModels;
import com.wyl.super_robot.openai.entity.models.Model;
import io.reactivex.Single;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@Component
@Data
public class ChatGPTUtil {
    public ChatGPT chatGPT;

    @Value("${openai.secret_key}")
    public String token;
    @Value("${openai.apiHost}")
    private String apiHost;
    private ChatGPTStream chatGPTStream;

    @Value("${proxy.ip}")
    private String proxyIp;
    @Value("${proxy.port}")
    private Integer proxyPort;

    @PostConstruct
    public void init() {

            chatGPT = ChatGPT.builder()
                    .apiKey(token)
                    .timeout(600)
                    .apiHost("https://api.openai.com/") //代理地址
                    .build()
                    .init();


    }

    /**
     * 模型列表
     */
    public Single<ListModels> listModels() {
        return chatGPT.listModels();
    }

    /**
     * 获取指定模型
     *
     * @param id
     * @return
     */
    public Single<Model> getModel(String id) {
        return chatGPT.getModel(id);
    }

    /**
     * 余额查询
     * @return
     */
    public CreditGrantsResponse creditGrants() {
        return chatGPT.creditGrants();
    }
    public BigDecimal balance(String key) {
        return chatGPT.balance(key);
    }
    /**
     * 账单查询
     * @param starDate
     * @param endDate
     * @return
     */
    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate) {
        return chatGPT.billingUsage(starDate, endDate);
    }
    public ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion) {
        ChatCompletionResponse chatCompletionResponse = chatGPT.chatCompletion(chatCompletion);
        return chatCompletionResponse;
    }
    public Message chatCompletion(String userMessage, String user) {
        Message message = Message.of(userMessage);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getMsg())
                .user(user)
                .messages(Arrays.asList(message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage();
    }
}

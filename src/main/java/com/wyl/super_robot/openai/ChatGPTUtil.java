package com.wyl.super_robot.openai;


import com.wyl.super_robot.openai.edits.Edit;
import com.wyl.super_robot.openai.edits.EditResponse;
import com.wyl.super_robot.openai.entity.billing.BillingUsage;
import com.wyl.super_robot.openai.entity.billing.CreditGrantsResponse;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.ChatCompletionResponse;
import com.wyl.super_robot.openai.entity.chat.Message;
import com.wyl.super_robot.openai.entity.completions.CompletionResponse;
import com.wyl.super_robot.openai.entity.images.*;
import com.wyl.super_robot.openai.entity.models.ListModels;
import com.wyl.super_robot.openai.entity.models.Model;
import com.wyl.super_robot.openai.entity.transcriptions.Transcriptions;
import com.wyl.super_robot.openai.entity.transcriptions.Translations;
import com.wyl.super_robot.openai.entity.transcriptions.WhisperResponse;
import io.reactivex.Single;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import javax.annotation.processing.Completion;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
                .apiHost("https://api.openai.com/")
                .build()
                .init();
    }
    /**
     * 账单查询
     *
     * @param starDate
     * @param endDate
     * @return
     */
    public BillingUsage billingUsage(@NotNull Date starDate, @NotNull Date endDate) {
        return chatGPT.billingUsage(starDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }
    /**
     * 聊天（不包含配置信息）
     *
     * @param userMessage
     * @return
     */
    public Message chatCompletion(String userMessage) {
        Message message = Message.of(userMessage);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getMsg())
                .user("user")
                .messages(Arrays.asList(message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage();
    }

    /**
     * 上下文聊天（包含配置信息）
     *
     * @param messages
     * @return
     */
    public ChatCompletionResponse chatCompletion(List<Message> messages) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getMsg())
                .user("user")
                .messages(messages)
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        return response;
    }
    /**
     * 简单问答（不包含上下文）
     */
    public String chat(String message) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(Arrays.asList(Message.of(message)))
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage().getContent();
    }

    public EditResponse edits(@RequestBody String input, String instruction) {
        Edit edit = Edit.builder()
                .input(input)
                .instruction(instruction)
                .model(Edit.Model.CODE_DAVINCI_EDIT_001.getName())
                .build();
        EditResponse edits = this.chatGPT.edit(edit);
        return edits;
    }



}

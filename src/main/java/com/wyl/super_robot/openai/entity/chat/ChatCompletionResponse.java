package com.wyl.super_robot.openai.entity.chat;

import com.wyl.super_robot.openai.entity.billing.Usage;
import lombok.Data;

import java.util.List;

/**
 * chat答案类
 *
 * @author wyl
 */
@Data
public class ChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;
}

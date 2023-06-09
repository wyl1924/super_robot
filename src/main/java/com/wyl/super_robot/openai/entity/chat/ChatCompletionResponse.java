package com.wyl.super_robot.openai.entity.chat;

import com.wyl.super_robot.openai.entity.common.Usage;
import lombok.Data;

import java.util.List;

/**
 * chat答案类
 *
 * @author wyl
 */
@Data
public class ChatCompletionResponse {
    public String id;
    public String object;
    public long created;
    public String model;
    public List<ChatChoice> choices;
    public Usage usage;
}

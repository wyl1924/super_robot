package com.wyl.super_robot.openai.entity.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author wyl
 */
@Data
public class ChatChoice {
    public long index;
    /**
     * 请求参数stream为true返回是delta
     */
    @JsonProperty("delta")
    public Message delta;
    /**
     * 请求参数stream为false返回是message
     */
    @JsonProperty("message")
    public Message message;
    @JsonProperty("finish_reason")
    public String finishReason;
}

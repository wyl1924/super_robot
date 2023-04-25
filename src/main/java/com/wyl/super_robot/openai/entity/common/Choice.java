package com.wyl.super_robot.openai.entity.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wyl
 * @date 2023/4/24 10:01
 */
@Data
public class Choice implements Serializable {
    public String text;
    public long index;
    public Object logprobs;
    @JsonProperty("finish_reason")
    public String finishReason;
}

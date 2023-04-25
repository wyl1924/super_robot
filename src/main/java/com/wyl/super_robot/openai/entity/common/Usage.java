package com.wyl.super_robot.openai.entity.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wyl
 * @date 2023/4/24 10:02
 */
@Data
public class Usage implements Serializable {
    @JsonProperty("prompt_tokens")
    public long promptTokens;
    @JsonProperty("completion_tokens")
    public long completionTokens;
    @JsonProperty("total_tokens")
    public long totalTokens;
}

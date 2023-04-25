package com.wyl.super_robot.openai.entity.completions;

import com.wyl.super_robot.openai.entity.common.Choice;
import com.wyl.super_robot.openai.entity.common.OpenAiResponse;
import com.wyl.super_robot.openai.entity.common.Usage;

import java.io.Serializable;

/**
 * @author wyl
 * @date 2023/4/24 9:59
 */
public class CompletionResponse extends OpenAiResponse implements Serializable {
    public String id;
    public String object;
    public long created;
    public String model;
    public Choice[] choices;
    public Usage usage;
}
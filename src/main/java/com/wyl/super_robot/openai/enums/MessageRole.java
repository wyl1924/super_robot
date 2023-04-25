package com.wyl.super_robot.openai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wyl
 * @date 2023/4/25 17:44
 */
@Getter
@AllArgsConstructor
public enum MessageRole {

    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    ;
    public String value;
}

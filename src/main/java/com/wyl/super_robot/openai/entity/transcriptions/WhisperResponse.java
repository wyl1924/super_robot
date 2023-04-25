package com.wyl.super_robot.openai.entity.transcriptions;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wyl
 * @date 2023/4/25 10:39
 */
@Data
public class WhisperResponse implements Serializable {
    public String text;
}

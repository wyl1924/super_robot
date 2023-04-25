package com.wyl.super_robot.openai.edits;

import com.wyl.super_robot.openai.entity.common.Choice;
import com.wyl.super_robot.openai.entity.common.Usage;

import java.io.Serializable;

/**
 * @author wyl
 * @date 2023/4/25 9:26
 */
public class EditResponse implements Serializable {
    private String id;
    private String object;
    private long created;
    private String model;
    private Choice[] choices;
    private Usage usage;
}
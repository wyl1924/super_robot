package com.wyl.super_robot.openai.entity.files;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wyl
 * @date 2023/4/25 10:29
 */
@Data
public class DeleteResponse implements Serializable {
    public String id;
    public String object;
    public boolean deleted;
}

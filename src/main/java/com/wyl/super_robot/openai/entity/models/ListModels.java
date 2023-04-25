package com.wyl.super_robot.openai.entity.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wyl
 * @date 2023/4/21 13:33
 */
public class ListModels implements Serializable {
    public String object;
    public List<Model> data;

}

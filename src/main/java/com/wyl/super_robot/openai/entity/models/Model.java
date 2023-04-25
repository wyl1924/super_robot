package com.wyl.super_robot.openai.entity.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wyl
 * @date 2023/4/23 17:18
 */
@Data
public class Model implements Serializable {

    public String id;
    public String object;
    public long created;
    @JsonProperty("owned_by")
    public String ownedBy;
    @JsonProperty("permission")
    public List<Permission> permission;
    public String root;
    public Object parent;
}


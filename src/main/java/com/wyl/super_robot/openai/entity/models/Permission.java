package com.wyl.super_robot.openai.entity.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wyl
 * @date 2023/4/23 17:18
 */

@Data
public class Permission implements Serializable {

    public String id;
    @JsonProperty("object")
    public String object;
    @JsonProperty("created")
    public long created;
    @JsonProperty("allow_create_engine")
    public boolean allowCreateEngine;
    @JsonProperty("allow_sampling")
    public boolean allowSampling;
    @JsonProperty("allow_logprobs")
    public boolean allowLogprobs;
    @JsonProperty("allow_search_indices")
    public boolean allowSearchIndices;
    @JsonProperty("allow_view")
    public boolean allowView;
    @JsonProperty("allow_fine_tuning")
    public boolean allowFineTuning;
    @JsonProperty("organization")
    public String organization;
    /**
     * 不知道是什么类型的数据
     */
    @JsonProperty("group")
    public Object group;
    @JsonProperty("is_blocking")
    public boolean isBlocking;
}

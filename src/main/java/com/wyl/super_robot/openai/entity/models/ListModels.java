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
    private String object;
    private List<Model> data;
    @Data
    private class Model implements Serializable {

        private String id;
        private String object;
        private long created;
        @JsonProperty("owned_by")
        private String ownedBy;
        @JsonProperty("permission")
        private List<Permission> permission;
        private String root;
        private Object parent;
    }

    @Data
    private class Permission implements Serializable {

        private String id;
        @JsonProperty("object")
        private String object;
        @JsonProperty("created")
        private long created;
        @JsonProperty("allow_create_engine")
        private boolean allowCreateEngine;
        @JsonProperty("allow_sampling")
        private boolean allowSampling;
        @JsonProperty("allow_logprobs")
        private boolean allowLogprobs;
        @JsonProperty("allow_search_indices")
        private boolean allowSearchIndices;
        @JsonProperty("allow_view")
        private boolean allowView;
        @JsonProperty("allow_fine_tuning")
        private boolean allowFineTuning;
        @JsonProperty("organization")
        private String organization;
        /**
         * 不知道是什么类型的数据
         */
        @JsonProperty("group")
        private Object group;
        @JsonProperty("is_blocking")
        private boolean isBlocking;
    }
}

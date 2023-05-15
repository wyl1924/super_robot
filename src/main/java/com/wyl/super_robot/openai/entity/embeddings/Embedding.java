package com.wyl.super_robot.openai.entity.embeddings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wyl.super_robot.enums.EmbeddingModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author wyl
 * @date 2023/4/25 9:39
 */
@Getter
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class Embedding implements Serializable {
    @NonNull
    @Builder.Default
    public String model = EmbeddingModel.TEXT_EMBEDDING_ADA_002.getName();
    /**
     * 必选项：长度不能超过：8192
     */
    @NonNull
    public List<String> input;

    public String user;

    public void setUser(String user) {
        this.user = user;
    }


}


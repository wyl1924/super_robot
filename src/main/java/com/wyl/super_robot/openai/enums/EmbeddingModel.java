package com.wyl.super_robot.openai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wyl
 * @date 2023/4/25 9:46
 */
@Getter
@AllArgsConstructor
public enum EmbeddingModel {
    TEXT_EMBEDDING_ADA_001("text-search-ada-doc-001"),
    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002"),
    ;
    private String name;
}

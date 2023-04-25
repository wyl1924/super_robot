package com.wyl.super_robot.openai.entity.embeddings;

import com.wyl.super_robot.openai.entity.common.Usage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wyl
 * @date 2023/4/25 9:50
 */
@Data
public class EmbeddingResponse implements Serializable {

    public String object;
    public List<Item> data;
    public String model;
    public Usage usage;
}


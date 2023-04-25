package com.wyl.super_robot.openai.entity.embeddings;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author wyl
 * @date 2023/4/25 9:52
 */
@Data
public class Item implements Serializable {
    public String object;
    public List<BigDecimal> embedding;
    public Integer index;
}

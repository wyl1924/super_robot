package com.wyl.super_robot.openai.entity.billing;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wyl
 * @date 2023/4/25 10:35
 */
@Data
public class LineItem {
    /**
     * 模型名称
     */
    public String name;
    /**
     * 消耗金额
     */
    public BigDecimal cost;
}


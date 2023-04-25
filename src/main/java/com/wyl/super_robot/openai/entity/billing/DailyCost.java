package com.wyl.super_robot.openai.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wyl
 * @date 2023/4/25 10:34
 */
@Data
public class DailyCost {
    /**
     * 时间戳
     */
    @JsonProperty("timestamp")
    public long timestamp;
    /**
     * 模型消耗金额详情
     */
    @JsonProperty("line_items")
    public List<LineItem> lineItems;
}

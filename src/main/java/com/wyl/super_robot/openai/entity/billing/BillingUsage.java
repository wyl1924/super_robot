package com.wyl.super_robot.openai.entity.billing;

/**
 * @author wyl
 * @date 2023/4/25 10:37
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class BillingUsage {

    @JsonProperty("object")
    public String object;
    /**
     * 账号金额消耗明细
     */
    @JsonProperty("daily_costs")
    public List<DailyCost> dailyCosts;
    /**
     * 总使用金额：美分
     */
    @JsonProperty("total_usage")
    public BigDecimal totalUsage;

}


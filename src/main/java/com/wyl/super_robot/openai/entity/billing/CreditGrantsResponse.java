package com.wyl.super_robot.openai.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 余额查询接口返回值
 *
 * @author wyl
 */
@Data
public class CreditGrantsResponse {
    public String object;
    /**
     * 总金额：美元
     */
    @JsonProperty("total_granted")
    public BigDecimal totalGranted;
    /**
     * 总使用金额：美元
     */
    @JsonProperty("total_used")
    public BigDecimal totalUsed;
    /**
     * 总剩余金额：美元
     */
    @JsonProperty("total_available")
    public BigDecimal totalAvailable;
    /**
     * 余额明细
     */
    public Grants grants;
}

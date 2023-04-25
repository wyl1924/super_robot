package com.wyl.super_robot.openai.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wyl
 */
@Data
public class Datum {
    public String object;
    public String id;
    /**
     * 赠送金额：美元
     */
    @JsonProperty("grant_amount")
    public BigDecimal grantAmount;
    /**
     * 使用金额：美元
     */
    @JsonProperty("used_amount")
    public BigDecimal usedAmount;
    /**
     * 生效时间戳
     */
    @JsonProperty("effective_at")
    public Long effectiveAt;
    /**
     * 过期时间戳
     */
    @JsonProperty("expires_at")
    public Long expiresAt;
}

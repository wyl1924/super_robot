package com.wyl.super_robot.openai.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wyl
 * @date 2023/4/25 10:34
 */
@Data
public class Subscription {
    @JsonProperty("object")
    public String object;
    @JsonProperty("has_payment_method")
    public boolean hasPaymentMethod;
    @JsonProperty("canceled")
    public boolean canceled;
    @JsonProperty("canceled_at")
    public Object canceledAt;
    @JsonProperty("delinquent")
    public Object delinquent;
    @JsonProperty("access_until")
    public long accessUntil;
    @JsonProperty("soft_limit")
    public long softLimit;
    @JsonProperty("hard_limit")
    public long hardLimit;
    @JsonProperty("system_hard_limit")
    public long systemHardLimit;
    @JsonProperty("soft_limit_usd")
    public double softLimitUsd;
    @JsonProperty("hard_limit_usd")
    public double hardLimitUsd;
    @JsonProperty("system_hard_limit_usd")
    public double systemHardLimitUsd;
    @JsonProperty("plan")
    public Plan plan;
    @JsonProperty("account_name")
    public String accountName;
    @JsonProperty("po_number")
    public Object poNumber;
    @JsonProperty("billing_email")
    public Object billingEmail;
    @JsonProperty("tax_ids")
    public Object taxIds;
    @JsonProperty("billing_address")
    public Object billingAddress;
    @JsonProperty("business_address")
    public Object businessAddress;
}


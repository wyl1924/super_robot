package com.wyl.super_robot.controller;

import com.wyl.super_robot.openai.ChatGPTUtil;
import com.wyl.super_robot.openai.entity.billing.BillingUsage;
import com.wyl.super_robot.openai.entity.billing.Subscription;
import io.reactivex.Single;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author wyl
 * @date 2023/5/12 14:22
 */
@Slf4j
@RestController
@Api(tags = "账户接口")
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private ChatGPTUtil chatGPTUtil;
    @GetMapping("/billing/subscription")
    @Operation(summary = "账户信息查询：里面包含总金额（美元）等信息")
    public Subscription subscription() {
        return chatGPTUtil.chatGPT.subscription();
    }
    @Operation(summary = "账户调用接口消耗金额信息查询")
    @GetMapping("/dashboard/billing/usage")
   public BillingUsage billingUsage(@Parameter(description = "文件") @RequestParam Date starDate, @Parameter(description = "文件") @RequestParam Date endDate){
        return chatGPTUtil.billingUsage(starDate,endDate);
    }
}

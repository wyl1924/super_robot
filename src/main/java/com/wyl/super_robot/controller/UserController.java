package com.wyl.super_robot.controller;

/**
 * @author wyl
 * @date 2023/4/14 11:29
 */

import com.wyl.super_robot.utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "用户接口")
@RequestMapping("/user")
public class UserController {
    @PostMapping("getStatus")
    @Operation(summary = "查看用户状态")
    public ResponseData<?> getUserStatus(@RequestParam(required = true) Integer userId) {
        return ResponseData.buildSuccess();
    }
}

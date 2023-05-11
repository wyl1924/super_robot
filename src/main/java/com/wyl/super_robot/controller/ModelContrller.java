package com.wyl.super_robot.controller;

import com.wyl.super_robot.openai.ChatGPTUtil;
import com.wyl.super_robot.openai.entity.models.ListModels;
import com.wyl.super_robot.utils.ResponseData;
import io.reactivex.Single;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wyl
 * @date 2023/5/11 11:41
 */
@Slf4j
@RestController
@Api(tags = "模型接口")
public class ModelContrller {
    @Autowired
    private ChatGPTUtil chatGPTUtil;
    @RequestMapping(value="models",method = RequestMethod.GET)
    @Operation(summary = "查看用户状态")
    public Single<ListModels> listModels() {
       var ss= chatGPTUtil.listModels();
       return ss;
    }
}

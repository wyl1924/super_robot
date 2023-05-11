package com.wyl.super_robot.controller;

import com.wyl.super_robot.openai.ChatGPTUtil;
import com.wyl.super_robot.openai.entity.images.Image;
import com.wyl.super_robot.openai.entity.images.ImageResponse;
import com.wyl.super_robot.utils.ResponseData;
import io.reactivex.Single;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author wyl
 * @date 2023/5/11 13:18
 */
@Slf4j
@RestController
@Api(tags = "图片接口")
@RequestMapping("/images")
public class ImagesCpntroller {
    @Autowired
    private ChatGPTUtil chatGPTUtil;
    @PostMapping("generations")
    @Operation(summary = "根据提示创建图像")
    public Single<ImageResponse> genImages(@Body Image image) {
        return null;
    }
}

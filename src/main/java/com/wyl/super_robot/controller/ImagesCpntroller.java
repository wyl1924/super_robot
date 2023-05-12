package com.wyl.super_robot.controller;

import com.wyl.super_robot.openai.ChatGPTUtil;
import com.wyl.super_robot.openai.entity.images.Image;
import com.wyl.super_robot.openai.entity.images.ImageResponse;
import com.wyl.super_robot.openai.entity.images.ImageVariations;
import com.wyl.super_robot.openai.entity.images.Item;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Body;

import java.io.File;
import java.util.List;

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
    public ImageResponse genImages(@Body Image image) {
        var images = chatGPTUtil.chatGPT.genImages(image);
        return images;
    }

    @PostMapping("genImages")
    @Operation(summary = "根据提示创建图像")
    public ImageResponse genImages(@RequestParam(required = true) String describe) {
        var images = chatGPTUtil.chatGPT.genImages(describe);
        return images;
    }

    @PostMapping("editImages")
    @Operation(summary = "根据提示修改图像")
    public List<Item> editImages(@Parameter(description = "文件") @RequestParam File file,
                                 @RequestParam(required = true) String describe) {
        var images = chatGPTUtil.chatGPT.editImages(file, describe);
        return images;
    }

    @PostMapping("variationsImages")
    @Operation(summary = "根据提示修改图像(原始)")
    public ImageResponse editImages(@Parameter(description = "文件") @RequestParam File file, @RequestParam(required = true) ImageVariations imageVariations) {
        var images = chatGPTUtil.chatGPT.variationsImages(file, imageVariations);
        return images;
    }
}

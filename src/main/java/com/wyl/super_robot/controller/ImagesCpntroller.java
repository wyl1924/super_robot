package com.wyl.super_robot.controller;

import com.wyl.super_robot.openai.ChatGPTUtil;
import com.wyl.super_robot.openai.entity.images.Image;
import com.wyl.super_robot.openai.entity.images.ImageResponse;
import com.wyl.super_robot.openai.entity.images.ImageVariations;
import com.wyl.super_robot.openai.entity.images.Item;
import com.wyl.super_robot.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import retrofit2.http.Body;

import java.io.File;
import java.io.IOException;
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
    public ImageResponse genImages(@RequestBody Image image) {
        log.info("image:{}", image.getPrompt());
        var images = chatGPTUtil.chatGPT.genImages(image);
        return images;
    }

    @PostMapping("genImages")
    @Operation(summary = "根据提示创建图像")
    public ImageResponse genImages(@RequestParam(required = true) String describe) {
        var images = chatGPTUtil.chatGPT.genImages(describe);
        return images;
    }

    @PostMapping("edits")
    @Operation(summary = "根据提示修改图像")
    public List<Item> editImages(MultipartFile image, String prompt) {
        log.info("image:{},prompt:{}", image.getOriginalFilename(), prompt);
        try {
            File file = FileUtil.convertMultipartFileToFile(image);
            var images = chatGPTUtil.chatGPT.editImages(file, prompt);
            return images;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("variationsImages")
    @Operation(summary = "根据提示修改图像(原始)")
    public ImageResponse editImages(MultipartFile image, String prompt, Integer n, String size) {
        try {
            File file = FileUtil.convertMultipartFileToFile(image);
            ImageVariations imageVariations = new ImageVariations();
            imageVariations.setN(n);
            imageVariations.setSize(size);
            var images = chatGPTUtil.chatGPT.variationsImages(file, imageVariations);
            file.delete();
            return images;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

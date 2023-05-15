package com.wyl.super_robot.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author wyl
 * @date 2023/5/15 14:01
 */
public class FileUtil {
    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        try {
            File file = File.createTempFile("temp", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

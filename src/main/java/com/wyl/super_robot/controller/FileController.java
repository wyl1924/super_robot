package com.wyl.super_robot.controller;

import com.wyl.super_robot.openai.ChatGPTUtil;
import com.wyl.super_robot.openai.entity.files.DeleteResponse;
import com.wyl.super_robot.openai.entity.files.File;
import com.wyl.super_robot.openai.entity.moderations.Moderation;
import com.wyl.super_robot.openai.entity.moderations.ModerationResponse;
import io.reactivex.Single;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.*;

import java.util.List;

/**
 * @author wyl
 * @date 2023/5/12 15:09
 */
@Slf4j
@RestController
@Api(tags = "文件接口")
public class FileController {
    @Autowired
    private ChatGPTUtil chatGPTUtil;

    @RequestMapping(value = "files", method = RequestMethod.GET)
    @Operation(summary = "获取文件列表")
    public List<File> files() {
        return chatGPTUtil.chatGPT.files();
    }

    @RequestMapping(value = "/files/{file_id}", method = RequestMethod.DELETE)
    @Operation(summary = "删除文件列表")
    public DeleteResponse deleteFile(@RequestParam("file_id") String fileId) {
        return chatGPTUtil.chatGPT.deleteFile(fileId);
    }

    @RequestMapping(value = "files/{file_id}/content", method = RequestMethod.GET)
    @Operation(summary = "检索文件内容(必须会员)")
    public File retrieveFileContent(@RequestParam("file_id") String fileId) {
        return chatGPTUtil.chatGPT.retrieveFile(fileId);
    }

    @RequestMapping(value = "moderations", method = RequestMethod.POST)
    @Operation(summary = "文本审核")
    public ModerationResponse moderations(@RequestBody List<String> input) {
        return chatGPTUtil.chatGPT.moderations(input);
    }

}

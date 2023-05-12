package com.wyl.super_robot.controller;

import com.wyl.super_robot.openai.ChatGPTUtil;
import com.wyl.super_robot.openai.entity.transcriptions.Transcriptions;
import com.wyl.super_robot.openai.entity.transcriptions.Translations;
import com.wyl.super_robot.openai.entity.transcriptions.WhisperResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author wyl
 * @date 2023/5/12 14:33
 */
@Slf4j
@RestController
@Api(tags = "音频接口")
@RequestMapping("/audio")
public class AudioController {
    @Autowired
    private ChatGPTUtil chatGPTUtil;
    @GetMapping("/speechToTextTranscriptions")
    @Operation(summary = "语音转文字")
    public WhisperResponse speechToTextTranscriptions(@RequestParam("语音文件 最大支持25MB mp3, mp4, mpeg, mpga, m4a, wav, webm") File file, Transcriptions transcriptions) {
        return chatGPTUtil.chatGPT.speechToTextTranscriptions(file,transcriptions);
    }

    @GetMapping("/speechToTextTranscriptionsOnlyFile")
    @Operation(summary = "语音转文字")
    public WhisperResponse speechToTextTranscriptions(File file) {
        return chatGPTUtil.chatGPT.speechToTextTranscriptions(file);
    }

    @GetMapping("/speechToTextTranslations")
    @Operation(summary = "语音翻译：目前仅支持翻译为英文")
    public WhisperResponse speechToTextTranslations(java.io.File file, Translations translations) {
        return chatGPTUtil.chatGPT.speechToTextTranslations(file,translations);
    }
    @GetMapping("/speechToTextTranslationsOnlyFile")
    @Operation(summary = "语音翻译：目前仅支持翻译为英文")
    public WhisperResponse speechToTextTranslations(File file)
    {
        return chatGPTUtil.chatGPT.speechToTextTranslations(file);
    }
}

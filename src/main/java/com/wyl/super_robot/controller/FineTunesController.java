package com.wyl.super_robot.controller;

import com.wyl.super_robot.openai.ChatGPTUtil;
import com.wyl.super_robot.openai.entity.common.OpenAiResponse;
import com.wyl.super_robot.openai.entity.fineTune.Event;
import com.wyl.super_robot.openai.entity.fineTune.FineTune;
import com.wyl.super_robot.openai.entity.fineTune.FineTuneDeleteResponse;
import com.wyl.super_robot.openai.entity.fineTune.FineTuneResponse;
import io.reactivex.Single;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.*;

import java.util.List;

/**
 * @author wyl
 * @date 2023/5/12 15:18
 */
@Slf4j
@RestController
@Api(tags = "微调接口")
public class FineTunesController {
    @Autowired
    private ChatGPTUtil chatGPTUtil;

    @RequestMapping(value = "fine-tunes", method = RequestMethod.POST)
    @Operation(summary = "创建微调作业")
    public FineTuneResponse fineTune(@RequestBody FineTune fineTune) {
        return chatGPTUtil.chatGPT.fineTune(fineTune);
    }

    @RequestMapping(value = "fine-tunes", method = RequestMethod.GET)
    @Operation(summary = "微调作业集合")
    public List<FineTuneResponse> fineTunes() {
        return chatGPTUtil.chatGPT.fineTunes();
    }

    @RequestMapping(value = "fine-tunes/{fine_tune_id}", method = RequestMethod.GET)
    @Operation(summary = "检索微调作业")
    public FineTuneResponse retrieveFineTune(@RequestParam("fine_tune_id") String fineTuneId) {
        return chatGPTUtil.chatGPT.retrieveFineTune(fineTuneId);
    }


    @RequestMapping(value = "fine-tunes/{fine_tune_id}/cancel", method = RequestMethod.POST)
    @Operation(summary = "取消微调作业")
    public FineTuneResponse cancelFineTune(@RequestParam("fine_tune_id") String fineTuneId) {
        return chatGPTUtil.chatGPT.cancelFineTune(fineTuneId);
    }

    @RequestMapping(value = "fine-tunes/{fine_tune_id}/events", method = RequestMethod.GET)
    @Operation(summary = "微调作业事件列表")
    public List<Event> fineTuneEvents(@RequestParam("fine_tune_id") String fineTuneId) {
        return chatGPTUtil.chatGPT.fineTuneEvents(fineTuneId);
    }

    @RequestMapping(value = "models/{model}", method = RequestMethod.DELETE)
    @Operation(summary = "删除微调作业模型")
    public  FineTuneDeleteResponse deleteFineTuneModel(@RequestParam("model") String model)
    {
        return chatGPTUtil.chatGPT.deleteFineTuneModel(model);
    }


}

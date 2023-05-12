package com.wyl.super_robot.controller;

import com.wyl.super_robot.openai.ChatGPTUtil;
import com.wyl.super_robot.openai.edits.Edit;
import com.wyl.super_robot.openai.edits.EditResponse;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.ChatCompletionResponse;
import com.wyl.super_robot.openai.entity.chat.Message;
import com.wyl.super_robot.openai.entity.completions.CompletionResponse;
import com.wyl.super_robot.openai.enums.MessageRole;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.processing.Completion;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wyl
 * @date 2023/5/11 13:31
 */
@Slf4j
@RestController
@Api(tags = "chat接口")
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatGPTUtil chatGPTUtil;

    @PostMapping("completions")
    @Operation(summary = "chat原始")
    public ChatCompletionResponse chatCompletion(@RequestBody ChatCompletion chatCompletion) {
        var chat = chatGPTUtil.chatGPT.chatCompletion(chatCompletion);
        return chat;
    }

    @RequestMapping(value = "completions", method = RequestMethod.GET)
    @Operation(summary = "问答")
    public CompletionResponse completions(@RequestBody Completion completion) {
        return chatGPTUtil.chatGPT.completions(completion);
    }

    @RequestMapping(value = "edits", method = RequestMethod.POST)
    @Operation(summary = "修改文本")
    public EditResponse edits(@RequestBody Edit completion) {
        return chatGPTUtil.chatGPT.edit(completion);
    }

    @RequestMapping(value = "completionsByInuput", method = RequestMethod.POST)
    @Operation(summary = "修改文本(EQ inupt=内容,instruction=修改内容")
    public EditResponse edits(@RequestBody String input, String instruction) {
        return chatGPTUtil.edits(input,instruction);
    }

    @PostMapping("completionsTomessages")
    @Operation(summary = "包含上下文")
    public ChatCompletionResponse chatCompletion(List<Message> describes) {
        var chat = chatGPTUtil.chatCompletion(describes);
        return chat;
    }

    @PostMapping("completionsTochat")
    @Operation(summary = "简单描述")
    public String chat(String describe) {
        var chat = chatGPTUtil.chat(describe);
        return chat;
    }

    @PostMapping("completionsTochats")
    @Operation(summary = "包含上下文简单聊天")
    public ChatCompletionResponse chat(List<String> describe) {
        List<Message> messages = new ArrayList<>();
        describe.forEach(item -> {
            messages.add(Message.builder().role(MessageRole.USER.getValue()).content(item).build());
        });
        var chat = chatGPTUtil.chatCompletion(messages);
        return chat;
    }

    @PostMapping("completionsToMessage")
    @Operation(summary = "聊天（不包含配置信息）")
    public Message chatCompletion(String describe) {
        var chat = chatGPTUtil.chatCompletion(describe);
        return chat;
    }

}

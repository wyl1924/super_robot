package com.wyl.super_robot.openai.entity.chat;

import com.wyl.super_robot.openai.enums.MessageRole;
import lombok.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author wyl
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    /**
     * 目前支持三中角色参考官网，进行情景输入：https://platform.openai.com/docs/guides/chat/introduction
     */
    public String role;
    public String content;

    public static Message of(String content) {

        return new Message(MessageRole.USER.getValue(), content);
    }

    public static Message ofSystem(String content) {

        return new Message(MessageRole.SYSTEM.getValue(), content);
    }

    public static Message ofAssistant(String content) {

        return new Message(MessageRole.ASSISTANT.getValue(), content);
    }

}

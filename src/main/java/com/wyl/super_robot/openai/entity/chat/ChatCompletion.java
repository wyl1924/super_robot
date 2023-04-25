package com.wyl.super_robot.openai.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * chat
 *
 * @author wyl
 */
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletion implements Serializable {

    @NonNull
    @Builder.Default
    public String model = Model.GPT_3_5_TURBO.getMsg();

    @NonNull
    public List<Message> messages;
    /**
     * 使用什么取样温度，0到2之间。越高越奔放。越低越保守。
     * <p>
     * 不要同时改这个和topP
     */
    @Builder.Default
    public double temperature = 0.9;

    /**
     * 0-1
     * 建议0.9
     * 不要同时改这个和temperature
     */
    @JsonProperty("top_p")
    @Builder.Default
    public double topP = 0.9;


    /**
     * 结果数。
     */
    @Builder.Default
    public Integer n = 1;


    /**
     * 是否流式输出.
     * default:false
     */
    @Builder.Default
    public boolean stream = false;
    /**
     * 停用词
     */
    public List<String> stop;
    /**
     * 3.5 最大支持4096
     * 4.0 最大32k
     */
    @JsonProperty("max_tokens")
    public Integer maxTokens;


    @JsonProperty("presence_penalty")
    public double presencePenalty;

    /**
     * -2.0 ~~ 2.0
     */
    @JsonProperty("frequency_penalty")
    public double frequencyPenalty;

    @JsonProperty("logit_bias")
    public Map logitBias;
    /**
     * 用户唯一值，确保接口不被重复调用
     */
    public String user;


    @Getter
    public enum Model {
        /**
         * gpt-3.5-turbo
         */
        GPT_3_5_TURBO("gpt-3.5-turbo"),
        /**
         * 临时模型，不建议使用
         */
        GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301"),
        /**
         * GPT4.0
         */
        GPT_4("gpt-4"),
        /**
         * 临时模型，不建议使用
         */
        GPT_4_0314("gpt-4-0314"),
        /**
         * GPT4.0 超长上下文
         */
        GPT_4_32K("gpt-4-32k"),
        /**
         * 临时模型，不建议使用
         */
        GPT_4_32K_0314("gpt-4-32k-0314"),
        ;


        /**
         * 描述
         */
        public String msg;

        Model(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}



package com.wyl.super_robot.openai.entity.images;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wyl.super_robot.enums.ResultEnums;
import com.wyl.super_robot.openai.enums.ResponseFormat;
import com.wyl.super_robot.openai.enums.SizeEnum;
import com.wyl.super_robot.openai.exception.ChatException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Getter
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class Image implements Serializable {

    @NonNull
    public String prompt;
    /**
     * 为每个提示生成的完成次数。
     */
    @Builder.Default
    public Integer n = 1;
    /**
     * 256x256
     * 512x512
     * 1024x1024
     */
    @Builder.Default
    public String size = "512x512";

    @JsonProperty("response_format")
    @Builder.Default
    public String responseFormat = ResponseFormat.URL.getName();

    public String user;

    public void setN(Integer n) {
        if(n < 1){
            log.warn("n最小值1");
            this.n = 1;
            return;
        }
        if(n > 10){
            log.warn("n最大值10");
            this.n = 10;
            return;
        }
        this.n = n;
    }

    public void setPrompt(String prompt) {
        if(Objects.isNull(prompt) || "".equals(prompt)){
            log.error("参数异常");
            throw new ChatException(ResultEnums.PARAM_ERROR.getMsg());
        }
        if(prompt.length() > 1000){
            log.error("长度超过1000");
            throw new ChatException(ResultEnums.PARAM_length.getMsg());
        }
        this.prompt = prompt;
    }

    public void setUser(String user) {
        this.user = user;
    }

}

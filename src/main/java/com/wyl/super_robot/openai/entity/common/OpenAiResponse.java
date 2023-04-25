package com.wyl.super_robot.openai.entity.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wyl
 * @date 2023/4/24 10:00
 */
@Data
public class OpenAiResponse<T> implements Serializable {
    public String object;
    public List<T> data;
    public Error error;


    @Data
    public class Error {
        public String message;
        public String type;
        public String param;
        public String code;
    }
}

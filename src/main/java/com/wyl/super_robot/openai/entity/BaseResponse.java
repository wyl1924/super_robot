package com.wyl.super_robot.openai.entity;

import lombok.Data;

import java.util.List;

/**
 * @author wyl
 */
@Data
public class BaseResponse<T> {
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

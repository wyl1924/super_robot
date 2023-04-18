package com.wyl.super_robot.openai.entity;

import lombok.Data;

import java.util.List;

/**
 * @author wyl
 */
@Data
public class BaseResponse<T> {
    private String object;
    private List<T> data;
    private Error error;


    @Data
    public class Error {
        private String message;
        private String type;
        private String param;
        private String code;
    }
}

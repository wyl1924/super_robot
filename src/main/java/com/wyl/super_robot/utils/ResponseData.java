package com.wyl.super_robot.utils;

import com.wyl.super_robot.enums.ResultEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @创建人 王延领
 * @创建时间 2023/04/14
 * 描述
 **/
@Getter
@Setter
@ApiModel("返回实体")
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = 4828723180335905210L;

    @ApiModelProperty("返回编码")
    private Integer errorCode;

    @ApiModelProperty("返回描述")
    private String errorMsg;

    @ApiModelProperty("内容")
    private T content;
    @ApiModelProperty("是否成功")
    private Boolean isSuccess;

    public ResponseData(Integer errorCode, String errorMsg, T content, boolean isSuccess) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.content = content;
        this.setIsSuccess(isSuccess);
    }
    public ResponseData(ResultEnums error) {
        this.errorCode = error.getCode();
        this.errorMsg = error.getMsg();
        this.content = null;
        this.setIsSuccess(false);
    }
    public ResponseData(Integer errorCode, String errorMsg, boolean isSuccess) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.setIsSuccess(isSuccess);
    }

    public ResponseData(ResultEnums resultEnums, boolean isSuccess) {
        this.errorCode = resultEnums.getCode();
        this.errorMsg = resultEnums.getMsg();
        this.setIsSuccess(isSuccess);
    }

    public ResponseData(ResultEnums resultEnums, T content, boolean isSuccess) {
        this.errorCode = resultEnums.getCode();
        this.errorMsg = resultEnums.getMsg();
        this.content = content;
        this.setIsSuccess(isSuccess);
    }

    public ResponseData() {
    }

    public static <T> ResponseData<T> buildSuccess(T data) {
        return new ResponseData<T>(ResultEnums.SUCCESS, data,true);
    }

    public static <T> ResponseData<T> buildSuccess() {
        return new ResponseData<T>(ResultEnums.SUCCESS,true);
    }

    public static <T> ResponseData<T> buildSuccess(String msg) {
        return new ResponseData<T>(ResultEnums.SUCCESS.getCode(), msg,true);
    }

    public static <T> ResponseData<T> buildSuccess(Integer code, String msg) {
        return new ResponseData<T>(code, msg,true);
    }

    public static <T> ResponseData<T> buildSuccess(String msg, T data) {
        return new ResponseData<T>(ResultEnums.SUCCESS.getCode(), msg, data,true);
    }

    public static <T> ResponseData<T> buildSuccess(Integer code, String msg, T data) {
        return new ResponseData<T>(code, msg, data,true);
    }

    public static <T> ResponseData<T> buildSuccess(ResultEnums resultEnums) {
        return new ResponseData<T>(resultEnums,true);
    }

    public static <T> ResponseData<T> buildError(T data) {
        return new ResponseData<T>(ResultEnums.ERROR, data,false);
    }

    public static <T> ResponseData<T> buildError() {
        return new ResponseData<T>(ResultEnums.ERROR,false);
    }

    public static <T> ResponseData<T> buildError(String msg) {
        return new ResponseData<T>(ResultEnums.ERROR.getCode(), msg,false);
    }

    public static <T> ResponseData<T> buildError(Integer code, String msg) {
        return new ResponseData<T>(code, msg,false);
    }

    public static <T> ResponseData<T> buildError(String msg, T data) {
        return new ResponseData<T>(ResultEnums.ERROR.getCode(), msg, data,false);
    }

    public static <T> ResponseData<T> buildError(Integer code, String msg, T data) {
        return new ResponseData<T>(code, msg, data,false);
    }

    public static <T> ResponseData<T> buildError(ResultEnums resultEnums) {
        return new ResponseData<T>(resultEnums,false);
    }

}
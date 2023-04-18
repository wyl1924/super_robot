package com.wyl.super_robot.enums;

/**
 * @author wyl
 * @date 2023/4/14 11:23
 */
public enum ResultEnums {
    /**
     * 请求成功
     */
    SUCCESS(0, "请求成功"),

    /**
     * 请求失败
     */
    ERROR(1, "请求失败"),
    API_KEYS_NOT_NUL(500, "API KEYS 不能为空"),
    SYS_ERROR(500, "系统繁忙"),
    PARAM_ERROR(501, "参数异常"),
    PARAM_length(501, "参数长度超过最大允许反胃"),
    RETRY_ERROR(502, "请求异常，请重试~"),
    //官方的错误码列表：https://platform.openai.com/docs/guides/error-codes/api-errors
    OPENAI_AUTHENTICATION_ERROR(401, "身份验证无效/提供的 API 密钥不正确/您必须是组织的成员才能使用 API"),
    OPENAI_LIMIT_ERROR(429 , "达到请求的速率限制/您超出了当前配额，请检查您的计划和帐单详细信息/发动机当前过载，请稍后重试"),
    OPENAI_SERVER_ERROR(500, "服务器在处理您的请求时出错"),

    ;

    /**
     * 编码
     */
    private Integer code;

    /**
     * 描述
     */
    private String msg;

    ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

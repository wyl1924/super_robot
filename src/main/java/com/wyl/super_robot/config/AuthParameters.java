package com.wyl.super_robot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @创建人 王延领
 * @创建时间 2023/04/14
 * 描述
 **/
@Component
public class AuthParameters {
    private String jwtTokenSecret;
    private long tokenExpiredMs;
    private String tokenHead;

    public String getJwtTokenSecret() {
        return jwtTokenSecret;
    }

    @Value("${jwt.secret}")
    public void setJwtTokenSecret(String jwtTokenSecret) {
        this.jwtTokenSecret = jwtTokenSecret;
    }

    public long getTokenExpiredMs() {
        return tokenExpiredMs;
    }

    @Value("${jwt.expiration}")
    public void setTokenExpiredMs(long tokenExpiredMs) {
        this.tokenExpiredMs = tokenExpiredMs;
    }

    public String getTokenHead() {
        return tokenHead;
    }

    @Value("${jwt.tokenHead}")
    public void setTokenHead(String tokenHead) {
        this.tokenHead = tokenHead;
    }

}

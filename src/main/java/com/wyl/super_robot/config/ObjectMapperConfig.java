package com.wyl.super_robot.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @创建人 王延领
 * @创建时间 2021/11/23
 * 描述 全局jackson
 **/
@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS); // 不序列化null的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}

package com.wyl.super_robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi //swagger3
@EnableTransactionManagement(proxyTargetClass = true)//启动注解事务
@EnableAsync//异步注解
@EnableConfigurationProperties
@SpringBootApplication
public class SuperRobotApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SuperRobotApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SuperRobotApplication.class);
    }
}

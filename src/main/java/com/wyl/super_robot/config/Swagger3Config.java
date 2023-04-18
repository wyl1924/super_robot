package com.wyl.super_robot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * @创建人 王延领
 * @创建时间 2021/11/17
 * 描述
 **/
@Configuration
public class Swagger3Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(createApiInfo())
                .securitySchemes(Collections.singletonList(HttpAuthenticationScheme.JWT_BEARER_BUILDER
                        // 显示用
                        .name("JWT").build()))
                .securityContexts(Collections.singletonList(SecurityContext.builder()
                        .securityReferences(Collections.singletonList(
                                SecurityReference.builder().
                                        scopes(new AuthorizationScope[0]).reference("JWT").build()))
                        // 声明作用域
                        .operationSelector(o -> o.requestMappingPattern().matches("/.*")).build()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wyl.super_robot.controller"))
                .paths(PathSelectors.any())
                .build();

            }
    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger3接口文档")
                .description("王延领学习项目")
                .contact(new Contact("王延领。", "http://wyl1924.gitee.io",
                        "1714404171.com"))
                .version("1.0")
                .build();
    }
}

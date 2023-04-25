package com.wyl.super_robot.openai.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author wyl
 * @date 2023/4/18 15:34
 */
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationBean implements Serializable {

    public static final long serialVersionUID = -4135009485485165947L;

    public String username;
    public String password;
    public Integer authType;

}

package com.wyl.super_robot.openai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@AllArgsConstructor
@Getter
public enum ResponseFormat implements Serializable {
    URL("url"),
    B64_JSON("b64_json"),
    ;

    public String name;
}

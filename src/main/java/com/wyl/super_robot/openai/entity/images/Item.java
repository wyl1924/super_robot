package com.wyl.super_robot.openai.entity.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
public class Item implements Serializable {
    public String url;
    @JsonProperty("b64_json")
    public String b64Json;
}

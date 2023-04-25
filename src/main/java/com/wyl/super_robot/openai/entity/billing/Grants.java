package com.wyl.super_robot.openai.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wyl
 */
@Data
public class Grants {
    public String object;
    @JsonProperty("data")
    public List<Datum> data;
}

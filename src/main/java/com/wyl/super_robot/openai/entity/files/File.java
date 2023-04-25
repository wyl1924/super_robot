package com.wyl.super_robot.openai.entity.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wyl
 * @date 2023/4/25 9:36
 */
@Data
public class File implements Serializable {

    public String id;
    public String object;
    public long bytes;
    public long created_at;
    public String filename;
    public String purpose;
    public String status;
    @JsonProperty("status_details")
    public String statusDetails;
}

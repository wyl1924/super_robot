package com.wyl.super_robot.openai.entity.images;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
public class ImageResponse implements Serializable {
    public long created;
    public List<Item> data;
}

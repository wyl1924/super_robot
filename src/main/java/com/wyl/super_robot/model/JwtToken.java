package com.wyl.super_robot.model;

import lombok.*;

/**
 * @创建人 王延领
 * @创建时间 2023/04/14
 * 描述
 **/
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {
    /// <summary>
    /// 是否成功
    /// </summary>
    public Boolean status;

    /// <summary>
    /// Token
    /// </summary>
    public String accessToken ;

    /// <summary>
    /// 过期秒数
    /// </summary>
    public long expiresIn ;

    /// <summary>
    /// Token类型
    /// </summary>
    public String tokenType ;


    /// <summary>
    /// 获取Token失败的原因
    /// </summary>
    public String message ;
}

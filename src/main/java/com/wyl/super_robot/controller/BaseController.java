package com.wyl.super_robot.controller;

import com.wyl.super_robot.utils.JwtTokenUtil;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @创建人 王延领
 * @创建时间 2021/04/14
 * 描述
 **/
public abstract class BaseController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String getIP() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 优先取 X-Real-IP
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ip)) {
                ip = "0:0:0:0";
            }
        }
        if ("unknown".equalsIgnoreCase(ip)) {
            ip = "0:0:0:0";
            return ip;
        }
        int index = ip.indexOf(',');
        if (index >= 0) {
            ip = ip.substring(0, index);
        }
        return ip;
    }
    /**
     * @描述 获取用户id
     * @参数
     * @返回值
     * @创建人 王延领
     * @创建时间 2021/11/28
     * @修改人和其它信息
     */
    protected int getUserId() {
        var userId = -1;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        var token = jwtTokenUtil.getJwtFromRequest(request);
        var userClaim = jwtTokenUtil.getClaims(token);
        if (userClaim != null) {
            userId = Integer.parseInt(userClaim.getSubject());
        }
        return userId;
    }


}

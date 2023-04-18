package com.wyl.super_robot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyl.super_robot.config.AuthParameters;
import com.wyl.super_robot.model.JwtToken;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

;

/**
 * @创建人 王延领
 * @创建时间 2023/04/14
 * 描述
 **/
@Slf4j
@Component
@Configuration
public class JwtTokenUtil {
    @Autowired
    private AuthParameters authParameters;
    @Autowired
    private ObjectMapper objectMapper;

    public JwtToken createJwtToken(String userName,String userAuth) throws JsonProcessingException {
        // expire time
        Date expireTime = new Date(System.currentTimeMillis() + authParameters.getTokenExpiredMs());
        Map claims = new HashMap<>();
        claims.put("CLAIM_KEY_USERNAME", userName);
        claims.put("CLAIM_KEY_CREATE_TIME", expireTime);

        // create token
        String token = Jwts.builder()
                //payload'
                .setClaims(claims)//如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setSubject(userAuth)
                .setExpiration(expireTime)//设置过期时间
                .setIssuedAt(new Date()) //iat: jwt的签发时间
                //signature
                .signWith(SignatureAlgorithm.HS512, authParameters.getJwtTokenSecret())
                .compact();
        JwtToken jwtToken = new JwtToken(true,token, authParameters.getTokenExpiredMs(), "Bearer","");
        return jwtToken;
    }

    public Boolean validateTokenExpiration(String token) {
        Claims claims=getClaims(token);
        final Date expiration = claims.getExpiration();
        return expiration.after(Calendar.getInstance().getTime());
    }
    public  String getId(String token) {
        Claims claims = getClaims(token);
        return claims.getId();
    }
    public Claims getClaims(String token) {
        try
        {
            token = token.replace(authParameters.getTokenHead(), "");
            Claims claims = Jwts
                    .parser()
                    .setSigningKey(authParameters.getJwtTokenSecret())
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        }
        catch (ExpiredJwtException e) {
            log.error("Token 已过期");
            return null;
        } catch (UnsupportedJwtException e) {
            log.error("不支持的 Token");
            return null;
        } catch (MalformedJwtException e) {
            log.error("Token 无效");
            return null;
        } catch (SignatureException e) {
            log.error("无效的 Token 签名");
            return null;
        } catch (IllegalArgumentException e) {
            log.error("Token 参数不存在");
            return null;
        }

    }
    /**
     * 从 request 的 header 中获取 JWT
     *
     * @param request 请求
     * @return JWT
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

package com.wyl.super_robot.filter;

import com.wyl.super_robot.utils.JwtTokenUtil;
import com.wyl.super_robot.utils.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Order(1)
@Component
public class JwtRequestFilter    implements Filter {
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private JwtTokenUtil jwtUtil;

    // Swagger WHITELIST
    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/webjars/**",
            "/favicon.ico",
            "/index.html",
            "/",
            "/static/**",
            "/ws/**",
            "/version.json"
    };

    // System WHITELIST
    public static final String[] SYSTEM_WHITELIST = {
            "/api/Token/**",
            "/api/Login/**",
            "/api/SystemSetting/**"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            String path = request.getRequestURI();
//           if (1 == 1) {
//               filterChain.doFilter(request, response);
//               return;
//           }
            // 免登录
            for (String url : SYSTEM_WHITELIST) {
                if (antPathMatcher.match(url, path)) {
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }
            }
            for (String url : SWAGGER_WHITELIST) {
                if (antPathMatcher.match(url, path)) {
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }
            }
            // List<String> token = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
            String token=  request.getHeader("Authorization");
            String jwt = jwtUtil.getJwtFromRequest(request);
            if (StringUtils.isNotBlank(jwt)&& jwtUtil.validateTokenExpiration(jwt)) {
                filterChain.doFilter(servletRequest,servletResponse);
            }else {
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json; charset=utf-8");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("token验证失败");
            }
        }catch (Exception ex) {
            ResponseData.buildError(ex.toString());
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json; charset=utf-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write(ex.toString());
        }
    }

    @Override
    public void destroy() {

    }
}
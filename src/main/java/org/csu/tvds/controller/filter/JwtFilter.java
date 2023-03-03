package org.csu.tvds.controller.filter;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.shiro.JwtToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.csu.tvds.common.ResponseCode.NEED_LOGIN;

/**
 * Shiro 过滤器
 *
 * @author Kwanho
 */
@Component
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 拦截请求的入口
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 1. 取出请求头的Authorization（token）
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");
        // 2. 判断是否有token
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        try { // 不为空
            SecurityUtils.getSubject().login(new JwtToken(token)); // 推给Realm处理
            return true;
        } catch (AuthenticationException e) { // 有token，但是无效
            log.info("{}：{}", e.getMessage(), ((HttpServletRequest) request).getHeader("Authorization"));
            return false;
        }
    }

    /**
     * 禁止访问
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.println(JSONUtil.toJsonStr(CommonResponse.createForError(NEED_LOGIN.getCode(), "未登录或登录状态已失效，请重新登录")));
        out.flush();
        out.close();
        return false;
    }
}

package org.csu.inheritance.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 通过这个类将 string 的 token 包装为 AuthenticationToken
 * （由于Shiro不接收字符串的token，所以需要对其进行包装，Shiro才能接收）
 *
 * @author Kwanho
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    /**
     * 构造器
     * 包装字符串类型的token为Shiro可用的AuthenticationToken
     *
     * @param token token
     */
    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}

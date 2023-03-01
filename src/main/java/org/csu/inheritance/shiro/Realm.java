package org.csu.inheritance.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.csu.inheritance.util.JwtUtil;
import org.csu.inheritance.util.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义Realm
 * 用于认证和授权
 *
 * @author Kwanho
 */
@Component
public class Realm extends AuthorizingRealm {
    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 限定此Realm的适配范围为JwtToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        // 1. 取出token
        String token = (String) authenticationToken.getCredentials();
        // 2. 校验token是否有效，是否过期，是否位于Redis中
        if (!jwtUtil.verifyToken(token) || jwtUtil.isExpire(token) || !redisUtil.hasKey(token)) {
            throw new AuthenticationException("用户携带了无效token"); // 过期抛出认证异常
        }
        // 3. 拿出用户信息
        String userId = jwtUtil.getUserId(token);
        return new SimpleAuthenticationInfo(userId, token, this.getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 授权，查询用户权限...
        // 暂时还没有授权功能的规划
        return null;
    }
}

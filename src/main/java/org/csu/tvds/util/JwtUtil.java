package org.csu.tvds.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 用于生成 token、校验 token
 *
 * @author Kwanho
 */
@ConfigurationProperties(prefix = "jwt")
@Component
@Slf4j
public class JwtUtil {
    @Setter
    private String secret; // 加密算法所需要的密钥
    @Setter
    public long defaultExpireTime; // 默认过期时间
    @Setter
    public String expireUnit; // 过期时间的单位

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 使用默认过期时间
     * 生成包含用户id的token
     *
     * @param userId     用户id
     * @param isSetCache 是否设置到Redis缓存中
     * @return token
     * @author Kwanho
     */
    public String createJwtToken(String userId, boolean isSetCache) {
        Date date = new Date(System.currentTimeMillis() + getDuration(expireUnit, defaultExpireTime));
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withClaim("userId", userId)
                .withExpiresAt(date) // 设置过期时间
                .sign(algorithm);     // 设置签名算法
        if (isSetCache) {
            redisUtil.set(token, userId, getDuration(expireUnit, defaultExpireTime), TimeUnit.MILLISECONDS);
        }
        return token;
    }

    /**
     * 使用指定过期时间
     * 生成包含用户id和自定义过期时间的token
     *
     * @param userId     用户id
     * @param timeUnit   过期时间单位
     * @param time       过期时间
     * @param isSetCache 是否设置到Redis缓存中
     * @return token
     */
    public String createJwtToken(String userId, TimeUnit timeUnit, long time, boolean isSetCache) {
        Date date = new Date(System.currentTimeMillis() + timeUnit.toMillis(time));
        Algorithm algorithm = Algorithm.HMAC256(secret);


        String token = JWT.create()
                .withClaim("userId", userId)
                .withExpiresAt(date) // 设置过期时间
                .sign(algorithm);     // 设置签名算法
        if (isSetCache) {
            redisUtil.set(token, userId, time, timeUnit);
        }
        return token;
    }

    /**
     * 使用默认过期时间
     * 生成包含自定义信息的token
     *
     * @param map        自定义信息-键值对
     * @param isSetCache 是否设置到Redis缓存中
     * @return token
     * @author Kwanho
     */
    public String createJwtToken(Map<String, String> map, boolean isSetCache) {
        JWTCreator.Builder builder = JWT.create();
        if (MapUtils.isNotEmpty(map)) {
            map.forEach(builder::withClaim);
        }
        Date date = new Date(System.currentTimeMillis() + getDuration(expireUnit, defaultExpireTime));
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = builder
                .withExpiresAt(date) // 过期时间
                .sign(algorithm);    // 签名算法
        if (isSetCache) {
            redisUtil.set(token, map, getDuration(expireUnit, defaultExpireTime), TimeUnit.MILLISECONDS);
        }
        return token;
    }

    /**
     * 使用指定过期时间
     * 生成包含自定义信息的token
     *
     * @param map        自定义信息-键值对
     * @param timeUnit   过期时间单位
     * @param time       过期时间
     * @param isSetCache 是否设置到Redis缓存中
     * @return token
     * @author Kwanho
     */
    public String createJwtToken(Map<String, String> map, TimeUnit timeUnit, long time, boolean isSetCache) {
        JWTCreator.Builder builder = JWT.create();
        if (MapUtils.isNotEmpty(map)) {
            map.forEach(builder::withClaim);
        }
        Date date = new Date(System.currentTimeMillis() + timeUnit.toMillis(time));
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = builder
                .withExpiresAt(date) // 过期时间
                .sign(algorithm);    // 签名算法
        if (isSetCache) {
            redisUtil.set(token, map, time, timeUnit);
        }
        return token;
    }

    /**
     * 校验token是否合法
     *
     * @param token token
     * @return 是否正确
     * @author Kwanho
     */
    public boolean verifyToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获取token中的userId
     *
     * @param token token
     * @return token中包含的用户id
     * @author Kwanho
     */
    public String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取token的负载
     *
     * @param token token
     * @param key   负载的键
     * @return 对应的值
     * @author Kwanho
     */
    public String getTokenAttr(String token, String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取token中的所有负载
     *
     * @param token token
     * @return Map形式的负载
     * @author Kwanho
     */
    public Map<String, String> getAllTokenPayloads(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, String> payload = new HashMap<>();
            jwt.getClaims().forEach((k, v) -> {
                payload.put(k, v.asString());
            });
            return payload;
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 是否过期
     * @author Kwanho
     */
    public boolean isExpire(String token) {
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        } catch (Exception e) {
            return true;
        }
        // 如果token的过期时间小于当前时间，则表示已过期，为true
        return jwt.getExpiresAt().getTime() < System.currentTimeMillis();
    }

    /**
     * 计算过期时间
     *
     * @return 单位
     * @author Kwanho
     */
    public long getDuration(String expireUnit, long time) {
        long duration;
        switch (expireUnit) {
            case "MINUTE":
                duration = TimeUnit.MINUTES.toMillis(time);
                break;
            case "HOUR":
                duration = TimeUnit.HOURS.toMillis(time);
                break;
            case "DAY":
                duration = TimeUnit.DAYS.toMillis(time);
                break;
            case "WEEK":
                duration = TimeUnit.DAYS.toMillis(time * 7);
                break;
            case "MONTH":
                duration = TimeUnit.DAYS.toMillis(time * 30);
                break;
            case "SECOND":
            default:
                duration = TimeUnit.SECONDS.toMillis(time);
                break;
        }
        return duration;
    }
}

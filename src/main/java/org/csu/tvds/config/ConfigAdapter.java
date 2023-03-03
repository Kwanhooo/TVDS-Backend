package org.csu.tvds.config;

import org.csu.tvds.shiro.auth.AuthUserMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Kwanho
 * @date 2022-10-29 16:56
 */
@Configuration
public class ConfigAdapter implements WebMvcConfigurer {
    @Autowired
    AuthUserMethodArgumentResolver authUserMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authUserMethodArgumentResolver);
    }
}

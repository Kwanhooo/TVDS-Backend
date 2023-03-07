package org.csu.tvds.config;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.csu.tvds.controller.filter.JwtFilter;
import org.csu.tvds.shiro.Realm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Shiro配置类
 *
 * @author Kwanho
 */
@Configuration
@ComponentScan(value = "org.csu")
public class ShiroConfig {

    /**
     * Shiro过滤器工厂
     * 拦截所有请求
     *
     * @param defaultWebSecurityManager 安全管理器
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 1. 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        // 2. 自定义过滤器（命名为jwt）
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        filterMap.put("common", new CompositeFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        // 3. 配置系统受限资源
        Map<String, String> map = new HashMap<>();
        map.put("/user/login", "common");
        map.put("/user/register", "common");
//        map.put("/fileIO/callback", "common");
        map.put("/test/**", "common");
//        map.put("/**", "common");
        map.put("/**", "jwt"); // 除了上面的白名单之外，都流入jwt过滤器
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    /**
     * Shiro安全管理器
     *
     * @param realm 自定义Realm
     * @return DefaultWebSecurityManager
     */
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 1. 设置Realm
        securityManager.setRealm(realm);
        // 2. 关闭Shiro的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }
}

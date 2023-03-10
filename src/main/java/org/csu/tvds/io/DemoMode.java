package org.csu.tvds.io;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.tvds.common.RuntimeConfig;
import org.csu.tvds.entity.mysql.User;
import org.csu.tvds.persistence.mysql.UserMapper;
import org.csu.tvds.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class DemoMode {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;

    @PostConstruct
    public void init() {
        if (RuntimeConfig.DEMO_MODE) {
            registerDemoUser();
            System.out.println("`演示模式`已启动，请使用 { 用户名：demo，密码：000000 } 来登录！");
        }
    }

    private void registerDemoUser() {
        // 查找名为demo的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "demo");
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            User demoUser = new User();
            demoUser.setUsername("demo");
            demoUser.setPassword("000000");
            demoUser.setNickname("TVDS 演示者");
            userService.register(demoUser);
        }
    }
}

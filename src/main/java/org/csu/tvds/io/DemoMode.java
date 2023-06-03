package org.csu.tvds.io;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.tvds.common.Constant;
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
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 查找名为demo的用户
        queryWrapper.eq("username", "demo");
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            User demoUser = new User();
            demoUser.setUsername("demo");
            demoUser.setPassword("000000");
            demoUser.setNickname("TVDS 演示者");
            demoUser.setRole(Constant.Role.USER);
            userService.register(demoUser);
        }

        // 查找名为admin的用户
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "admin");
        User admin = userMapper.selectOne(queryWrapper);
        if (admin == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("000000");
            adminUser.setNickname("TVDS 管理员");
            adminUser.setRole(Constant.Role.ADMIN);
            userService.register(adminUser);
        }

        // 查找名为A的用户
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "AAA");
        User A = userMapper.selectOne(queryWrapper);
        if (A == null) {
            User AUser = new User();
            AUser.setUsername("AAA");
            AUser.setPassword("000000");
            AUser.setNickname("TVDS 用户AAA");
            AUser.setRole(Constant.Role.USER);
            userService.register(AUser);
        }

        // 查找名为B的用户
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "BBB");
        User B = userMapper.selectOne(queryWrapper);
        if (B == null) {
            User BUser = new User();
            BUser.setUsername("BBB");
            BUser.setPassword("000000");
            BUser.setNickname("TVDS 用户BBB");
            BUser.setRole(Constant.Role.USER);
            userService.register(BUser);
        }
    }
}

package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.entity.mysql.User;
import org.csu.tvds.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 注册
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("register")
    public CommonResponse<?> register(
            @RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 认证结果
     * @author Kwanho
     */
    @PostMapping("login")
    public CommonResponse<?> login(
            @RequestParam String username,
            @RequestParam String password) {
        return userService.login(username, password);
    }

    /**
     * 登出
     * 废除当前用户携带的token
     *
     * @param Authorization token
     * @return 登出结果
     */
    @PostMapping("logout")
    public CommonResponse<?> logout(
            @RequestHeader String Authorization) {
        System.out.println(Authorization);
        return userService.logout(Authorization);
    }
}

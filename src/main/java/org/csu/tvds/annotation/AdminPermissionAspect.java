package org.csu.tvds.annotation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.csu.tvds.common.Constant;
import org.csu.tvds.common.ResponseCode;
import org.csu.tvds.entity.mysql.User;
import org.csu.tvds.exception.PermissionException;
import org.csu.tvds.persistence.mysql.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Aspect
public class AdminPermissionAspect {
    @Resource
    private UserMapper userMapper;


    @Pointcut("@annotation(org.csu.tvds.annotation.AdminPermission)")
    public void logPoint() {
    }

    @Before("logPoint()")
    public void beforeAop() {
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        System.out.println("PRINCIPLE => " + principal);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id", Long.valueOf(principal)));
        System.out.println(user);
        if (!user.getRole().equals(Constant.Role.ADMIN))
            throw new PermissionException(ResponseCode.NEED_PERMISSION, "您没有权限进行此操作");
    }
}

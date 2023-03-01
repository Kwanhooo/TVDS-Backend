package org.csu.inheritance.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author Kwanho
 */
@Data
@TableName("lib_user")
public class User {
    /**学号 主键生成策略(自动生成)*/
    @TableId(value = "user_id", type = IdType.INPUT)
    @NotBlank(message = "学号不能为空")
    private String userId;

    /**真实姓名*/
    @NotBlank(message = "真实姓名不能为空")
    private String name;

    /**用户名*/
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**密码*/
    @NotBlank(message = "密码不能为空")
    private String password;

    /**昵称*/
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    /**注册时间 LocalDateTime Java8*/
    @TableField(value = "reg_time", fill = FieldFill.INSERT)
    private LocalDateTime regTime;

    /**是否激活（1激活、0封号）*/
    @TableField("is_valid")
    private Integer isValid;

    /**身份（user/admin）*/
    @NotBlank(message = "身份不能为空")
    private String role;
}

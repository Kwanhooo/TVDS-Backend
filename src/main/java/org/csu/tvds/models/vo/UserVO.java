package org.csu.tvds.models.vo;

import lombok.Data;

@Data
public class UserVO {
    private String token;
    private UserInfoVo userInfo;
}

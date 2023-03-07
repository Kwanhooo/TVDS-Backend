package org.csu.tvds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.entity.mysql.User;
import org.csu.tvds.models.vo.UserInfoVo;

/**
 * @author kwanho
 */
public interface UserService extends IService<User> {
    CommonResponse<UserInfoVo> register(User user);

    CommonResponse<?> login(String username, String password);

    CommonResponse<?> logout(String authorization);
}

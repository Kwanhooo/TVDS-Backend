package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.common.Constant;
import org.csu.tvds.common.ResponseCode;
import org.csu.tvds.entity.mysql.User;
import org.csu.tvds.exception.BusinessException;
import org.csu.tvds.models.vo.UserInfoVo;
import org.csu.tvds.models.vo.UserVO;
import org.csu.tvds.persistence.mysql.UserMapper;
import org.csu.tvds.service.UserService;
import org.csu.tvds.util.EncryptUtil;
import org.csu.tvds.util.JwtUtil;
import org.csu.tvds.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author kwanho
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public CommonResponse<UserInfoVo> register(User user) {
        User query_user = query().eq("username", user.getUsername())
                .eq("user_id", user.getUserId()).one();

        // 这里还是需要重新判断一下 看信息是否唯一
        if (query_user != null) {
            // 不唯一则不能注册
            throw new BusinessException(ResponseCode.ERROR.getCode(), "当前用户已经存在！不能重复注册", null);
        }

        // 为空则可以注册
        user.setPassword(EncryptUtil.encode(user.getPassword())); //密码加密
        user.setIsValid(Constant.IS_VALID.VALID); // 默认激活
        boolean flag = save(user);
        if (!flag) {
            throw new BusinessException(ResponseCode.ERROR.getCode(), "用户注册失败", null);
        }
        UserInfoVo userInfoVO = createUserInfoVO(user);
        return CommonResponse.createForSuccess(userInfoVO);
    }

    @Override
    public CommonResponse<?> login(String username, String password) {
        // 1. 有没有这个用户
        User loginUser = query().eq("username", username).one();
        if (loginUser == null) {
            throw new BusinessException(ResponseCode.ERROR.getCode(), "用户名或密码错误", null);
        }
        // 2. 有则校验密码
        if (!EncryptUtil.match(password, loginUser.getPassword())) {
            throw new BusinessException(ResponseCode.ERROR.getCode(), "用户名或密码错误", null);
        }
        // 3. 通过则颁发token
        String token = jwtUtil.createJwtToken(loginUser.getUserId(), true);
        // 4. 返回前端
        // 返回一个UserVO的组合数据
        UserVO userVO = createUserVO(token, loginUser);
        return CommonResponse.createForSuccess(userVO);
    }

    @Override
    public CommonResponse<?> logout(String token) {
        // 1. 从redis中删除token
        redisUtil.delete(token);
        // 2. 返回结果
        return CommonResponse.createForSuccess("登出成功");
    }

    private UserVO createUserVO(String token, User user) {
        UserVO userVO = new UserVO();
        UserInfoVo userInVO = createUserInfoVO(user);
        userVO.setToken(token);
        userVO.setUserInfo(userInVO);

        return userVO;
    }

    private UserInfoVo createUserInfoVO(User user) {
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(user, vo);
        vo.setPassword(null);
        return vo;
    }
}





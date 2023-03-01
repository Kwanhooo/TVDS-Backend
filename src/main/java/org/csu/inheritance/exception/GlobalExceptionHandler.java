package org.csu.inheritance.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.csu.inheritance.common.CommonResponse;
import org.csu.inheritance.common.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import static org.csu.inheritance.common.ResponseCode.NEED_LOGIN;

/**
 * 全局异常捕获器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 捕获未处理的运行时异常
     *
     * @param e 运行时异常
     * @return 响应
     */
//    @ExceptionHandler(RuntimeException.class)
//    @ResponseBody
//    public CommonResponse<?> handler(RuntimeException e) {
//        log.error("运行时异常：消息 -> {}", e.getMessage());
//        return CommonResponse.createForError(e.getMessage());
//    }

    /**
     * 捕获未处理的业务逻辑异常
     *
     * @param e 业务逻辑异常
     * @return 响应
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public CommonResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("业务逻辑异常: 消息 -> {} 描述 -> " + e.getMessage(), e.getDescription());
        return CommonResponse.createForError(e.getCode(), e.getMessage());
    }

    /**
     * 捕获未处理的Shiro异常
     *
     * @param e Shiro异常
     * @return 响应
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    @ResponseBody
    public CommonResponse<?> handler(ShiroException e) {
        log.error("用户未认证：消息 -> {}", e.getMessage());
        return CommonResponse.createForError(NEED_LOGIN.getCode(), e.getMessage());
    }

    /**
     *
     * @param e METHOD_NOT_ALLOWED异常
     * @return 响应
     */

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = MethodNotAllowedException.class)
    @ResponseBody
    public CommonResponse<?> handleMethodNotAllowedException(MethodNotAllowedException e){
        log.error("请求方法有错");
        return CommonResponse.createForError(405, "请求方法有错");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public CommonResponse<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        log.error("请求参数有误");
        return CommonResponse.createForError(ResponseCode.ARGUMENT_ILLEGAL.getCode(), ResponseCode.ARGUMENT_ILLEGAL.getDescription());
    }
}

package org.csu.tvds.exception;

import org.csu.tvds.common.ResponseCode;

/**
 * 业务异常类
 *
 * @author Kwanho
 */
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ResponseCode errorCode) {
        super(errorCode.getDescription());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ResponseCode errorCode, String description) {
        super(errorCode.getDescription());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

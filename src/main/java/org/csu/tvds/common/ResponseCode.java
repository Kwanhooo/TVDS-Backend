package org.csu.tvds.common;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    ARGUMENT_ILLEGAL(10, "ARGUMENT_ILLEGAL"),
    NEED_LOGIN(11, "NEED_LOGIN"),
    NEED_PERMISSION(12, "NEED_PERMISSION"),
    FILE_SERVICE_ERROR(20, "FILE_SERVICE_ERROR"),
    FILE_NOT_EXISTS(21, "FILE_NOT_EXISTS"),
    FILE_NO_PERMISSION(22, "FILE_NO_PERMISSION");


    private final int code;
    private final String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

}

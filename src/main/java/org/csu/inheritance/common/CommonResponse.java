package org.csu.inheritance.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
//序列化成Json字符串的时候将NULL值去除
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private Integer code;
    private String message;
    private T data;

    //用来给静态函数的接口构造CommonResponse
    protected CommonResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    //这个不需要序列化
    @JsonIgnore
    //用于判断成功与否
    public boolean isSuccess() {
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    //请求成功，无数据返回
    public static <T> CommonResponse<T> createForSuccess() {
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDescription(), null);
    }

    //请求成功，并返回响应数据
    public static <T> CommonResponse<T> createForSuccess(T return_data) {
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDescription(), return_data);
    }

    //请求错误，默认错误信息
    public static <T> CommonResponse<T> createForError() {
        return new CommonResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDescription(), null);
    }

    //请求错误，指定错误信息
    public static <T> CommonResponse<T> createForError(String err_message) {
        return new CommonResponse<>(ResponseCode.ERROR.getCode(), err_message, null);
    }

    //请求错误，指定错误码和错误信息
    public static <T> CommonResponse<T> createForError(Integer code, String err_message) {
        return new CommonResponse<>(code, err_message, null);
    }

}

package com.example.saassystem.common;

import lombok.Data;

/**
 * @Author cxt
 * @Date 2026-10-09 15:53
 */

@Data
public class Result<T> {

    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code; this.message = message; this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(0, "ok", data);
    }

    public static Result<Void> success() {
        return new Result<>(0, "ok", null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

}

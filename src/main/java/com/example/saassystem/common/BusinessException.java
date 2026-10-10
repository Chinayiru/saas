package com.example.saassystem.common;

import lombok.Getter;

/**
 * @Author cxt
 * @Date 2026-10-09 15:56
 */

@Getter
public class BusinessException extends RuntimeException{
    private final int code;

    public BusinessException(int code , String message) {
        super(message);
        this.code = code;
    }
}

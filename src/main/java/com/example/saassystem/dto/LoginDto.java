package com.example.saassystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录/注册请求体
 *
 * @author cxt
 * @date 2026-10-09
 */
@Data
public class LoginDto {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}

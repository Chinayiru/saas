package com.example.saassystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.saassystem.common.BusinessException;
import com.example.saassystem.common.Result;
import com.example.saassystem.dto.LoginDto;
import com.example.saassystem.entity.SysUser;
import com.example.saassystem.mapper.UserMapper;
import com.example.saassystem.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册 / 登录
 *
 * @author cxt
 * @date 2026-10-09
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtil;


    //注册
    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Valid LoginDto dto) {
        SysUser u = new SysUser();
        u.setTenantId(1L);   // 演示：默认挂到租户 1
        u.setUsername(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword()));
        userMapper.insert(u);
        return Result.success();
    }


    //登录
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDto dto) {
        SysUser u = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getUsername()));
        if (u == null || !encoder.matches(dto.getPassword(), u.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        String token = jwtUtil.createToken(u.getId(), u.getUsername(), u.getTenantId());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("nickname", u.getNickname() == null ? u.getUsername() : u.getNickname());
        return Result.success(data);
    }
}

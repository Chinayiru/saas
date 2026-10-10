package com.example.saassystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户
 *
 * @author cxt
 * @date 2026-10-09
 */
@Data
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;
    private String username;
    private String password;   // BCrypt 加密存储
    private String nickname;
    private String email;
    private LocalDateTime createTime;
}

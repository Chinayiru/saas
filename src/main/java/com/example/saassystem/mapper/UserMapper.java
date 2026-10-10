package com.example.saassystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.saassystem.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper
 *
 * @author cxt
 * @date 2026-10-09
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
}

package com.example.saassystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.saassystem.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户表 Mapper
 *
 * @author cxt
 * @date 2026-10-09
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}

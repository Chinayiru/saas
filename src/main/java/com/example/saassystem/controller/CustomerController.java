package com.example.saassystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.saassystem.common.Result;
import com.example.saassystem.entity.Customer;
import com.example.saassystem.mapper.CustomerMapper;
import com.example.saassystem.tenant.TenantContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户管理 CRUD（分页 + 校验 + 全局异常）
 *
 * @author cxt
 * @date 2026-10-09
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping("/list")
    public Result<Page<Customer>> list(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(required = false) String name) {
        Page<Customer> p = new Page<>(page, size);
        LambdaQueryWrapper<Customer> qw = new LambdaQueryWrapper<Customer>()
                .like(StringUtils.hasText(name), Customer::getName, name)
                .orderByDesc(Customer::getCreateTime);
        return Result.success(customerMapper.selectPage(p, qw));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Valid Customer customer) {
        customer.setId(null);
        customer.setTenantId(TenantContext.get());   // 写入时显式带上租户
        customerMapper.insert(customer);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody @Valid Customer customer) {
        customerMapper.updateById(customer);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        customerMapper.deleteById(id);
        return Result.success();
    }
}

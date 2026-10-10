package com.example.saassystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户（业务表示例）
 *
 * @author cxt
 * @date 2026-10-09
 */
@Data
@TableName("biz_customer")
public class Customer {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tenantId;   // 多租户插件自动填充查询条件
    @NotBlank(message = "姓名不能为空")
    private String name;
    private String phone;
    private String company;
    private Integer status;
    private LocalDateTime createTime;
}

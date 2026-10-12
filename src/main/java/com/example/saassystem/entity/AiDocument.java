package com.example.saassystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI 知识库文档登记表
 *
 * @author cxt
 * @date 2026-10-12
 */
@Data
@TableName("ai_document")
public class AiDocument {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    /** Python 侧生成的 doc_id（UUID），与向量库对应 */
    private String docId;

    private String fileName;

    private Integer chunkCount;

    private LocalDateTime createTime;
}

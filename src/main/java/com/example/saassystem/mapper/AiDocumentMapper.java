package com.example.saassystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.saassystem.entity.AiDocument;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI 知识库文档表 Mapper
 *
 * @author cxt
 * @date 2026-10-12
 */
@Mapper
public interface AiDocumentMapper extends BaseMapper<AiDocument> {
}

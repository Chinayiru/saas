package com.example.saassystem.controller;

import com.example.saassystem.common.BusinessException;
import com.example.saassystem.common.Result;
import com.example.saassystem.entity.AiDocument;
import com.example.saassystem.mapper.AiDocumentMapper;
import com.example.saassystem.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 知识库文档上传：Java → Python AI 服务解析入库
 *
 * @author cxt
 * @date 2026-10-10
 */
@RestController
@RequestMapping("/api/document")
public class UploadController {
    @Value("${app.ai.url}") private String aiUrl;
    @Autowired private RestTemplate restTemplate;
    @Autowired private AiDocumentMapper aiDocumentMapper;

    @PostMapping("/upload")
    public Result<Map> upload(@RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("file", new ByteArrayResource(file.getBytes()) {
                @Override public String getFilename() { return file.getOriginalFilename(); }
            });
            form.add("tenant_id", tenantId);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(form, headers);
            Map resp = restTemplate.postForObject(aiUrl + "/api/document/upload", entity, Map.class);

            // 上传成功后登记到 MySQL ai_document 表
            if (resp != null && resp.get("doc_id") != null) {
                AiDocument doc = new AiDocument();
                doc.setTenantId(TenantContext.get());
                doc.setDocId((String) resp.get("doc_id"));
                doc.setFileName((String) resp.get("file_name"));
                doc.setChunkCount(((Number) resp.get("chunk_count")).intValue());
                aiDocumentMapper.insert(doc);
            }
            return Result.success(resp);
        } catch (Exception e) {
            throw new BusinessException(500, "上传失败：" + e.getMessage());
        }
    }
}

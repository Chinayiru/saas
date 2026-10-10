package com.example.saassystem.controller;

import com.example.saassystem.common.Result;
import com.example.saassystem.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * AI 对话转发：Java → Python AI 服务
 *
 * @author cxt
 * @date 2026-10-10
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Value("${app.ai.url}") private String aiUrl;
    @Autowired private RestTemplate restTemplate;

    @PostMapping("/chat")
    public Result<Map> chat(@RequestBody Map<String, String> body) {
        Map resp = restTemplate.postForObject(aiUrl + "/api/chat", body, Map.class);
        return Result.success(resp);
    }

    @PostMapping("/chat/rag")     // 知识库问答，第 3 周启用
    public Result<Map> ragChat(@RequestBody Map<String, Object> body) {
        body.put("tenant_id", TenantContext.get());
        Map resp = restTemplate.postForObject(aiUrl + "/api/chat/rag", body, Map.class);
        return Result.success(resp);
    }
}

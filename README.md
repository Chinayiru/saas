# 智能 SaaS 后台系统

基于 **Spring Boot + FastAPI + 通义千问** 的双服务 SaaS 后台系统，支持 JWT 鉴权、多租户隔离、AI 对话和 RAG 知识库问答。

## 技术栈

| 层 | 技术 |
|---|---|
| 后端 Java | Spring Boot 3 + MyBatis-Plus + JWT + MySQL 8 + Redis |
| 后端 Python | FastAPI + Uvicorn + ChromaDB + 通义千问（DashScope SDK） |
| 前端 | 原生 HTML + JavaScript（单文件） |
| 部署 | Docker + docker-compose |

## 目录结构

```
saas/
├── saas-system/              # Java 后端（Spring Boot）
│   ├── src/main/java/com/example/saassystem/
│   │   ├── common/            # Result / 全局异常
│   │   ├── config/           # MyBatis / RestTemplate / Security
│   │   ├── controller/        # Auth / Customer / AI / Upload
│   │   ├── entity/           # 数据库实体
│   │   ├── mapper/           # MyBatis-Plus Mapper
│   │   ├── security/         # JWT 工具 + 过滤器
│   │   └── tenant/           # 多租户上下文
│   └── Dockerfile
├── ai-service/               # Python AI 服务（FastAPI）
│   ├── routers/               # chat / document 路由
│   ├── services/              # rag_service 向量检索
│   ├── main.py
│   └── Dockerfile
├── frontend/                 # 前端页面（单文件 HTML）
│   └── index.html
├── docker-compose.yml        # 一键启动
└── README.md
```

## 快速启动

### 方式一：本地开发（推荐）

1. **启动 MySQL 和 Redis**
2. **启动 Python AI 服务**（端口 8000）
   ```bash
   cd ai-service
   pip install -r requirements.txt
   uvicorn main:app --reload --port 8000
   ```
3. **启动 Java 后端**（端口 8080）
   ```bash
   cd saas-system
   mvn spring-boot:run
   ```
4. **打开前端页面**
   - 浏览器直接打开 `frontend/index.html`

### 方式二：Docker 一键启动

```bash
docker-compose up -d
```

## 接口列表

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/auth/login` | 登录，返回 JWT token |
| POST | `/api/auth/register` | 注册 |
| POST | `/api/ai/chat` | AI 对话（通义千问） |
| POST | `/api/ai/chat/rag` | RAG 知识库问答（带引用） |
| POST | `/api/document/upload` | 上传文档到知识库 |

## 默认账号

- 用户名：`admin`
- 密码：`123456`

## 环境变量

Python 服务需要配置 `.env` 文件：

```
DASHSCOPE_API_KEY=sk-你的百炼API Key
CHAT_MODEL=qwen-plus
EMBED_MODEL=text-embedding-v3
```

## 开发进度

- [x] 第一周：Java 后台骨架（JWT 鉴权 + 客户 CRUD + 多租户）
- [x] 第二周：AI 对话链路（Python FastAPI + 通义千问）
- [x] 第三周：RAG 知识库问答（文档上传 + 向量检索）
- [x] 第四周：部署与前端（Docker + 管理页面）

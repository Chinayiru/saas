# routers/chat.py 普通对话（RAG 问答第 3 周再加）
from fastapi import APIRouter
from pydantic import BaseModel
from services import llm_service

router = APIRouter(prefix="/api")


class ChatReq(BaseModel):
    message: str


@router.post("/chat")
def chat(req: ChatReq):
    return {"reply": llm_service.chat([{"role": "user", "content": req.message}])}

# ---- 以下第 3 周 RAG 启用时再打开 ----
# from services import rag_service
# class RagReq(BaseModel):
#     message: str
#     tenant_id: int
#
# @router.post("/chat/rag")
# def rag_chat(req: RagReq):
#     docs = rag_service.query_kb(req.message, req.tenant_id)
#     reply = rag_service.build_answer(req.message, docs, req.tenant_id)
#     return {"reply": reply, "sources": docs}

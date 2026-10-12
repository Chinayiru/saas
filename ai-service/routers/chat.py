# routers/chat.py 普通对话 + RAG 问答
from fastapi import APIRouter
from pydantic import BaseModel
from services import llm_service, rag_service

router = APIRouter(prefix="/api")


class ChatReq(BaseModel):
    message: str


@router.post("/chat")
def chat(req: ChatReq):
    return {"reply": llm_service.chat([{"role": "user", "content": req.message}])}


class RagReq(BaseModel):
    message: str
    tenant_id: int


@router.post("/chat/rag")
def rag_chat(req: RagReq):
    docs = rag_service.query_kb(req.message, req.tenant_id)
    reply = rag_service.build_answer(req.message, docs, req.tenant_id)
    return {"reply": reply, "sources": docs}

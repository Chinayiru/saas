# services/rag_service.py
import os
import chromadb
from openai import OpenAI
from pypdf import PdfReader
import docx
from config import DASHSCOPE_API_KEY, DASHSCOPE_BASE_URL, EMBED_MODEL, CHROMA_PATH

client = OpenAI(api_key=DASHSCOPE_API_KEY, base_url=DASHSCOPE_BASE_URL)

# PersistentClient：数据落盘到 kb_data/，重启不丢
chroma_client = chromadb.PersistentClient(path=CHROMA_PATH)
collection = chroma_client.get_or_create_collection("knowledge")


def embed_texts(texts):
    """中文向量化：DashScope text-embedding-v3"""
    resp = client.embeddings.create(model=EMBED_MODEL, input=texts)
    return [d.embedding for d in resp.data]


def extract_text(file_path):
    if file_path.endswith(".pdf"):
        reader = PdfReader(file_path)
        return "\n\n".join(p.extract_text() or "" for p in reader.pages)
    if file_path.endswith(".docx"):
        return "\n\n".join(p.text for p in docx.Document(file_path).paragraphs)
    raise ValueError("仅支持 PDF / DOCX")


def split_chunks(text, size=400, overlap=100):
    """按段落切片，相邻块保留 overlap，避免语义断裂"""
    paras = [p.strip() for p in text.split("\n\n") if p.strip()]
    chunks, buf = [], ""
    for p in paras:
        if len(buf) + len(p) <= size:
            buf = p if not buf else buf + "\n" + p
        else:
            if buf:
                chunks.append(buf)
            buf = (buf[-overlap:] + "\n" + p) if buf else p
    if buf:
        chunks.append(buf)
    return chunks


def save_document(doc_id, file_path, tenant_id):
    """解析 → 切片 → 向量化 → 入库（doc_id 前缀防重复）"""
    text = extract_text(file_path)
    chunks = split_chunks(text)
    embeds = embed_texts(chunks)
    ids = [f"{doc_id}_{i}" for i in range(len(chunks))]
    metas = [{"doc_id": doc_id, "tenant_id": str(tenant_id)} for _ in chunks]
    collection.add(ids=ids, documents=chunks, embeddings=embeds, metadatas=metas)
    return len(chunks)


def delete_document(doc_id):
    got = collection.get(where={"doc_id": doc_id})
    if got["ids"]:
        collection.delete(ids=got["ids"])


def query_kb(question, tenant_id, n=3):
    """按租户隔离检索最相关的 n 段"""
    q = embed_texts([question])
    res = collection.query(query_embeddings=q, n_results=n,
                           where={"tenant_id": str(tenant_id)})
    return res["documents"][0] if res["documents"] else []


def build_answer(question, docs, tenant_id):
    """检索结果拼进 Prompt：只依据资料回答，末尾列引用编号"""
    context = "\n\n".join(f"[资料{i+1}] {d}" for i, d in enumerate(docs))
    prompt = (
        "你是企业内部智能助手。请只依据下面资料回答问题，资料没有提到就明确回答不知道。"
        "回答末尾列出用到的资料编号。\n\n"
        f"资料：\n{context}\n\n问题：{question}\n回答："
    )
    resp = client.chat.completions.create(
        model="qwen-plus",
        messages=[{"role": "user", "content": prompt}]
    )
    return resp.choices[0].message.content

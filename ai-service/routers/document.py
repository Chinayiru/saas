# routers/document.py 文档上传 / 删除
import os
import uuid
from fastapi import APIRouter, UploadFile, File, Form
from services import rag_service
from config import UPLOAD_DIR

router = APIRouter(prefix="/api/document")
os.makedirs(UPLOAD_DIR, exist_ok=True)


@router.post("/upload")
def upload(file: UploadFile = File(...), tenant_id: int = Form(1)):
    doc_id = str(uuid.uuid4())
    path = os.path.join(UPLOAD_DIR, f"{doc_id}_{file.filename}")
    with open(path, "wb") as f:
        f.write(file.file.read())
    chunks = rag_service.save_document(doc_id, path, tenant_id)
    return {"doc_id": doc_id, "file_name": file.filename, "chunk_count": chunks}


@router.delete("/{doc_id}")
def remove(doc_id: str):
    rag_service.delete_document(doc_id)
    return {"deleted": doc_id}

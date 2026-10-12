# config.py
import os
from dotenv import load_dotenv

load_dotenv()

DASHSCOPE_API_KEY = os.getenv("DASHSCOPE_API_KEY")
DASHSCOPE_BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1"   # 注意：无空格！
CHAT_MODEL    = os.getenv("CHAT_MODEL", "qwen-plus")
EMBED_MODEL   = os.getenv("EMBED_MODEL", "text-embedding-v3")
CHROMA_PATH   = os.getenv("CHROMA_PATH", "./kb_data")
UPLOAD_DIR    = os.getenv("UPLOAD_DIR", "./uploads")

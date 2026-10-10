# main.py
from fastapi import FastAPI
from routers import chat

app = FastAPI(title="AI Service")
app.include_router(chat.router)

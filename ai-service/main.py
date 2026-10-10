# main.py
from fastapi import FastAPI
from routers import chat
import uvicorn

app = FastAPI(title="AI Service")
app.include_router(chat.router)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)

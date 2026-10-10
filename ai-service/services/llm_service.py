# services/llm_service.py
from openai import OpenAI
from config import DASHSCOPE_API_KEY, DASHSCOPE_BASE_URL, CHAT_MODEL

client = OpenAI(api_key=DASHSCOPE_API_KEY, base_url=DASHSCOPE_BASE_URL)


def chat(messages):
    resp = client.chat.completions.create(model=CHAT_MODEL, messages=messages)
    return resp.choices[0].message.content

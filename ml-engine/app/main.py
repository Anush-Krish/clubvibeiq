from fastapi import FastAPI
from app.api.v1.api_v1 import router as api_router

app = FastAPI(title="ClubVibeIQ ML Engine")

app.include_router(api_router, prefix="/ml/v1")

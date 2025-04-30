from fastapi import FastAPI
from app.api.routes import router

app = FastAPI(title="ClubVibeIQ ML Engine")

app.include_router(router)

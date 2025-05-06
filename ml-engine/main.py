from fastapi import FastAPI
from app.api.routes import router

app = FastAPI(title="ClubVibeIQ ML Engine")

async def infer_songs(data: InfereceRequest):
    return get_crowd_recommendations(data.songs)

app.include_router(api_router, prefix="/ml/v1")

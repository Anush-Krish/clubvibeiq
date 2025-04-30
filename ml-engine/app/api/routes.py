from fastapi import APIRouter
from pydantic import BaseModel

router = APIRouter()

class InputData(BaseModel):
    features: list[float]

@router.post("/predict")
async def predict(data: InputData):
    # dummy prediction logic
    return {"prediction": sum(data.features)}

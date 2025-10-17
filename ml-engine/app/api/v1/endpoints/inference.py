import uuid

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.schemas.inference import InferenceResponse, UserInference
from app.services import inference as infer_service
from app.services import enhanced_infer as enhanced_infer_service
from fastapi import Query
from typing import List

router = APIRouter()


@router.post("", response_model=InferenceResponse)
def get_infer(user_infer_list: List[UserInference]):
    return infer_service.get_inference(user_infer_list)

@router.post("/enhanced", response_model=InferenceResponse)
def get_enhanced_infer(user_infer_list: List[UserInference]):
    return enhanced_infer_service.enhanced_recommend(user_infer_list)


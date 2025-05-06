from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.db.session import get_db
from app.schemas.inference import InferenceResponse
from app.crud import user as crud_user

router = APIRouter()

# endpoints of crud
@router.get("/", response_model=InferenceResponse)
def get_infer(user: UserCreate, db: Session = Depends(get_db)):
    return crud_user.create_user(db, user)

# @router.get("/{user_id}", response_model=UserResponse)
# def get_user(user_id: int, db: Session = Depends(get_db)):
#     db_user = crud_user.get_user_by_id(db, user_id)
#     if not db_user:
#         raise HTTPException(status_code=404, detail="User not found")
#     return db_user
#
# @router.get("/", response_model=list[UserResponse])
# def list_users(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
#     return crud_user.get_all_users(db, skip, limit)
#
# @router.delete("/{user_id}")
# def delete_user(user_id: int, db: Session = Depends(get_db)):
#     if not crud_user.delete_user(db, user_id):
#         raise HTTPException(status_code=404, detail="User not found")
#     return {"msg": "User deleted"}
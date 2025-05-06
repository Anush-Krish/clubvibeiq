from pydantic import BaseModel, EmailStr

# class UserBase(BaseModel):
#     name: str
#     email: EmailStr
#
# class UserCreate(UserBase):
#     pass
#
# class UserResponse(UserBase):
#     id: int
#
#     class Config:
#         orm_mode = True

## model response for dj
class UserInference(BaseModel):
    clubId: uuid.UUID
    songs: List[Songs]

class InferenceResponse(BaseModel):
    pass


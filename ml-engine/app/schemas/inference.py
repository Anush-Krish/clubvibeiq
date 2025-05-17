from pydantic import EmailStr
from app.schemas.base import CamelModel



class UserInference(CamelModel):
   # club_id: uuid.UUID
    top_tracks: List[str]
    top_artists: List[str]
    artist_genres: List[str]
    top_playlist_tracks: List[str]


class InferenceResponse(CamelModel):
    suggested_tracks: List[str]
    message: str


class Config:
    orm_mode = True

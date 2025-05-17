from typing import List, Optional
from app.schemas.base import CamelModel
from pydantic import BaseModel

class UserInference(BaseModel):
    topTracks: Optional[List[str]] = None
    playlistSongs: List[str]
    topArtists: Optional[List[str]] = None
    artistGenres: Optional[dict] = None

class InferenceResponse(CamelModel):
    suggested_tracks: List[str]
    message: str
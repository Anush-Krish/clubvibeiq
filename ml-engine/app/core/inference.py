from app.core.recommender import get_majority_songs, recommend_similar_songs
from app.schemas.inference import UserInference as MusicLibrary
from app.core.vectorizer import embed_songs, get_average_vector
from typing import List


def run_inference(music_libraries: List[MusicLibrary]) -> List[str]:
    """
    Run the inference process to recommend songs based on multiple music libraries
    """
    top_songs = get_majority_songs(music_libraries)
    
    if not top_songs:
        return []
    
    song_embeddings = embed_songs(top_songs)
    
    club_vector = get_average_vector(song_embeddings)
    
    recommended_songs = recommend_similar_songs(club_vector, song_embeddings, top_k=10)
    
    return recommended_songs




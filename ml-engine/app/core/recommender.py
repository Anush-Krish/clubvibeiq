from collections import Counter
from typing import List
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

from app.schemas.inference import UserInference as MusicLibrary
from app.core.vectorizer import embed_songs, get_average_vector


def get_majority_songs(music_libraries: List[MusicLibrary], top_n=50) -> List[str]:
    song_counter = Counter()
    for lib in music_libraries:
        songs = (lib.playlistSongs or []) + (lib.topTracks or [])
        song_counter.update(songs)
    return [song for song, _ in song_counter.most_common(top_n)]


def recommend_similar_songs(club_vector: np.ndarray, all_song_embeddings: dict[str, np.ndarray], top_k=10) -> List[str]:
    if club_vector is None or not all_song_embeddings:
        return []

    song_names = list(all_song_embeddings.keys())
    song_vectors = np.stack([all_song_embeddings[song] for song in song_names])

    similarities = cosine_similarity([club_vector], song_vectors)[0]
    top_indices = similarities.argsort()[::-1][:top_k]
    return [song_names[i] for i in top_indices]

from music_library import MusicLibrary
from app.core.recommender import get_majority_songs, recommend_similar_songs


from app.schemas import UserInference as MusicLibrary
from app.core.vectorizer import embed_songs, get_average_vector


top_songs = get_majority_songs(music_libraries)


song_embeddings = embed_songs(top_songs)


club_vector = get_average_vector(song_embeddings)


recommended_songs = recommend_similar_songs(club_vector, song_embeddings, top_k=10)

print("🎵 Recommended Songs:", recommended_songs)

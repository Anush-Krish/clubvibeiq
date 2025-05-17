from typing import List
from app.schemas.inference import UserInference, InferenceResponse

from app.core.recommender import get_majority_songs, recommend_similar_songs
from app.core.vectorizer import embed_songs, get_average_vector


def get_inference(user_infer_list: List[UserInference]) -> InferenceResponse:
    # Get majority songs from active users
    top_songs = get_majority_songs(user_infer_list)

    #  Get vector embeddings of those songs
    song_embeddings = embed_songs(top_songs)

    # Average the vectors to form the club's taste vector
    club_vector = get_average_vector(song_embeddings)

    #Recommend similar songs basically cosine similarity
    recommended_songs = recommend_similar_songs(club_vector, song_embeddings, top_k=10)

    return {
        "suggested_tracks": recommended_songs,
        "message": "Inference completed successfully."
    }

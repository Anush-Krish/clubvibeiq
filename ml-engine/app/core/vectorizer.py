from sentence_transformers import SentenceTransformer
import numpy as np

model = SentenceTransformer('all-MiniLM-L6-v2')


def embed_songs(song_names: list[str]) -> dict[str, np.ndarray]:
    embeddings = model.encode(song_names, show_progress_bar=True)
    return dict(zip(song_names, embeddings))


def get_average_vector(embeddings: dict[str, np.ndarray]) -> np.ndarray:
    return np.mean(list(embeddings.values()), axis=0) if embeddings else np.zeros(384)

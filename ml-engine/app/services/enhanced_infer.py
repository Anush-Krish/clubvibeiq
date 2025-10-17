import os
from typing import List
import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans
from sklearn.metrics.pairwise import cosine_similarity
from collections import Counter
from app.schemas.inference import UserInference as MusicLibrary


class ClubRecommender:
    def __init__(self, spotify_csv_path: str = None):
        # Use default path if not provided, or check environment variable
        if spotify_csv_path is None:
            spotify_csv_path = os.getenv("SPOTIFY_CSV_PATH", "SpotifyAudioFeaturesApril2019.csv")
        
        # Load dataset
        self.spotify_data = pd.read_csv(spotify_csv_path)
        self.audio_features = ['danceability', 'energy', 'valence', 'tempo',
                               'acousticness', 'instrumentalness', 'liveness', 'loudness']
        self.scaler = StandardScaler()
        self.kmeans = KMeans(n_clusters=10, random_state=42)

        # Scale and cluster
        scaled_features = self.scaler.fit_transform(self.spotify_data[self.audio_features])
        clusters = self.kmeans.fit_predict(scaled_features)
        self.spotify_data['cluster'] = clusters

    def get_majority_songs(self, music_libraries: List[MusicLibrary], top_n=50) -> List[str]:
        song_counter = Counter()
        for lib in music_libraries:
            songs = (lib.playlistSongs or []) + (lib.topTracks or [])
            song_counter.update(songs)
        return [song for song, _ in song_counter.most_common(top_n)]

    def enhanced_recommend(self, music_libraries: List[MusicLibrary], top_k=10):
        top_songs = self.get_majority_songs(music_libraries)
        
        # First try to match by track_id (for backwards compatibility)
        user_tracks_by_id = self.spotify_data[self.spotify_data['track_id'].isin(top_songs)]
        
        # Also try to match by track_name (since backend sends names instead of IDs)
        user_tracks_by_name = self.spotify_data[self.spotify_data['track_name'].isin(top_songs)]
        
        # Combine both results (union of matches)
        user_tracks = pd.concat([user_tracks_by_id, user_tracks_by_name]).drop_duplicates()
        
        if user_tracks.empty:
            return []

        # Find dominant cluster
        dominant_cluster = user_tracks['cluster'].mode().iloc[0] if not user_tracks['cluster'].mode().empty else 0
        cluster_songs = self.spotify_data[self.spotify_data['cluster'] == dominant_cluster]

        # Compute cluster similarity
        user_profile = user_tracks[self.audio_features].mean().values.reshape(1, -1)
        user_profile_scaled = self.scaler.transform(user_profile)
        cluster_features_scaled = self.scaler.transform(cluster_songs[self.audio_features].values)
        similarities = cosine_similarity(user_profile_scaled, cluster_features_scaled)[0]

        cluster_songs_copy = cluster_songs.copy()
        cluster_songs_copy['similarity'] = similarities
        
        # Remove tracks that were in the original user library (whether matched by ID or name)
        # Get track IDs that match the original track names
        tracks_by_name = self.spotify_data[self.spotify_data['track_name'].isin(top_songs)]
        original_track_ids_from_names = set(tracks_by_name['track_id'])
        
        # Also include original IDs if they exist in the dataset
        original_track_ids_from_ids = set(top_songs) if all(t in self.spotify_data['track_id'].values for t in top_songs) else set()
        
        # Combine original track IDs to avoid in recommendations
        all_original_track_ids = original_track_ids_from_names.union(original_track_ids_from_ids)
        
        recommendations = cluster_songs_copy[~cluster_songs_copy['track_id'].isin(all_original_track_ids)]

        return recommendations.nlargest(top_k, 'similarity')[
            ['track_id', 'track_name', 'artist_name', 'similarity']
        ].rename(columns={'artist_name': 'artists'}).to_dict('records')


# Global instance for API use
_recommender = None


def get_recommender(csv_path: str = None):
    """
    Get or create the recommender instance, handling cases where the CSV file may not be available initially
    """
    global _recommender
    if _recommender is None:
        try:
            _recommender = ClubRecommender(csv_path)
        except FileNotFoundError:
            # If CSV file is not found, we could handle it gracefully in a production app
            # For now, just re-raise the error since the file is required
            raise
    return _recommender


def enhanced_recommend(music_libraries: List[MusicLibrary], top_k: int = 10, csv_path: str = None):
    """
    Standalone function that provides enhanced recommendations, compatible with API endpoint
    """
    from app.schemas.inference import InferenceResponse
    
    recommender = get_recommender(csv_path)
    result = recommender.enhanced_recommend(music_libraries, top_k)
    
    # Extract just the track IDs for the response (to match expected format)
    if result:
        track_ids = [item['track_id'] for item in result]  # Return just the track IDs
    else:
        track_ids = []
    
    # Return the expected InferenceResponse format with both track IDs and names
    if result:
        import json
        # Create JSON strings containing both track ID and name
        track_info = [json.dumps({"track_id": item['track_id'], "track_name": item['track_name']}) for item in result]
    else:
        track_info = []
    
    return InferenceResponse(
        suggested_tracks=track_info,
        message=f"Enhanced recommendations generated successfully. Found {len(track_info)} track(s)."
    )


# Example usage function (not executed when imported)
def example_usage():
    try:
        recommender = get_recommender("SpotifyAudioFeaturesApril2019.csv")
        # This would require actual music_libraries data
        # music_libraries = [...]
        # recommended = recommender.enhanced_recommend(music_libraries)
        # print("🎵 Recommended Songs:", recommended)
        pass
    except FileNotFoundError:
        print("⚠️  CSV file not found for example usage")

# ClubVibeIQ 🎧

**Status:** Building (Stage: Microservice Architecture)

ClubVibeIQ is an intelligent music recommendation system designed to understand club audiences in real time by analyzing their music preferences and suggesting crowd-based music insights to DJs. The system leverages a microservice architecture using Spring Boot, FastAPI, and (soon) a React frontend.

---

## 🔧 Architecture Overview

- **Backend (Spring Boot)**  
  - Handles user authentication and Spotify OAuth integration  
  - Manages user preferences and club data  
  - Provides user music library data to the ML engine for prediction
  - Manages club entities and user registration

- **ML Engine (FastAPI)**  
  - Consumes user song data from the backend  
  - Implements two recommendation algorithms:
    - **Basic Inference**: Cosine similarity using sentence transformers
    - **Enhanced Inference**: K-Means clustering + cosine similarity on Spotify audio features
  - Generates personalized music recommendations based on crowd preferences
  - Uses Spotify audio features dataset for enriched recommendations

- **Frontend (React)** *(coming soon)*  
  - QR-based club entry system  
  - Interactive DJ dashboard for viewing crowd insights  
  - User-facing interaction to connect Spotify and give consent

---

## 🏗️ Current Project Structure

```
clubvibeiq/
├── backend/                          # Java Spring Boot service
│   ├── .gitattributes
│   ├── .gitignore
│   ├── build.gradle                 # Gradle build configuration
│   ├── gradlew, gradlew.bat         # Gradle wrapper scripts
│   ├── settings.gradle              # Gradle settings
│   ├── src/
│   │   └── main/
│   │       ├── java/com/clubvibeiq/backend/
│   │       │   ├── ClubVibeIQBackendApplication.java    # Main application entry point
│   │       │   ├── club/
│   │       │   │   └── entity/Club.java                 # Club entity model
│   │       │   ├── config/
│   │       │   │   └── WebClientConfig.java             # HTTP client configuration
│   │       │   ├── enums/GenderType.java                # User gender enum
│   │       │   ├── external/spotify/
│   │       │   │   ├── SpotifyAuthController.java       # Spotify OAuth endpoints
│   │       │   │   ├── SpotifyAuthService.java          # Spotify authentication service
│   │       │   │   └── SpotifyService.java              # Spotify API client
│   │       │   ├── preference/
│   │       │   │   ├── controller/CrowdPreferenceController.java  # Crowd preference API
│   │       │   │   ├── dto/
│   │       │   │   │   ├── PreferenceResponseDto.java   # Preference response data transfer object
│   │       │   │   │   └── UserPreferenceDto.java       # User preference DTO
│   │       │   │   ├── entity/UserPreference.java       # User preference entity
│   │       │   │   ├── mapper/UserPreferenceMapper.java # DTO to entity mapping
│   │       │   │   ├── repository/UserPreferenceRepository.java  # JPA repository
│   │       │   │   └── service/CrowdPreferenceService.java       # Crowd preference business logic
│   │       │   ├── usermanagement/
│   │       │   │   ├── controller/UserController.java   # User management endpoints
│   │       │   │   ├── dto/
│   │       │   │   │   ├── UserRequestDto.java          # User registration request DTO
│   │       │   │   │   └── UserResponseDto.java         # User registration response DTO
│   │       │   │   ├── entity/User.java                 # User entity model
│   │       │   │   ├── mapper/UserMapper.java           # User DTO mapping
│   │       │   │   ├── repository/UserRepository.java   # User JPA repository
│   │       │   │   └── service/UserService.java         # User business logic
│   │       │   └── utils/
│   │       │       ├── ExteralApiUtil.java              # External API utilities
│   │       │       ├── GenericResponse.java             # Standard API response wrapper
│   │       │       └── model/
│   │       │           ├── BaseDto.java                 # Base data transfer object
│   │       │           ├── BaseEntity.java              # Base entity model
│   │       │           └── MusicLibrary.java            # Music library data structure
│   │       └── resources/
│   │           ├── application.properties              # Main application config
│   │           ├── application-dev.properties          # Development config
│   │           ├── application-prod.properties         # Production config
│   │           └── localhost.p12                       # SSL certificate for HTTPS
│   └── build/                         # Compiled application
├── ml-engine/                       # Python ML service
│   ├── __init__.py
│   ├── app/
│   │   ├── api/
│   │   │   └── v1/
│   │   │       └── endpoints/
│   │   │           └── inference.py   # API inference endpoints
│   │   ├── core/                      # ML core algorithms (vectorizer, recommender)
│   │   ├── schemas/
│   │   │   └── inference.py          # API data validation schemas
│   │   └── services/
│   │       ├── enhanced_infer.py     # Enhanced recommendation algorithm
│   │       └── inference.py          # Basic recommendation algorithm
│   ├── SpotifyAudioFeaturesApril2019.csv  # Spotify audio features dataset
│   ├── specs.md                       # ML engine specifications
│   ├── requirements.txt               # Python dependencies
│   └── venv/                          # Python virtual environment
└── project.md                        # Comprehensive project documentation
```

---

## 🚀 User Flow

1. User enters a club and scans a QR code.
2. They're redirected to the backend where they authenticate with **Spotify**.
3. Backend fetches Spotify data (top tracks, playlist songs, favorite artists) for **one-time inference**.
4. Data is sent to the ML Engine via the `/infer/enhanced` endpoint.
5. ML Engine analyzes crowd preferences and returns track recommendations with both IDs and names.
6. DJs get live audience intelligence and suggested tracks for optimal crowd engagement.

---

## 🧠 ML Algorithms

### Basic Inference (Cosine Similarity)
- Uses sentence transformers to create embeddings from track names
- Averages user's top track embeddings to create taste profile
- Computes cosine similarity for recommendations

### Enhanced Inference (Clustering + Similarity)
- **K-Means Clustering**: Groups songs into 10 clusters based on audio features
- **Audio Features**: danceability, energy, valence, tempo, acousticness, instrumentalness, liveness, loudness
- **Dominant Cluster Identification**: Finds cluster that best represents user preferences
- **Similarity Matching**: Computes cosine similarity within the dominant cluster
- **Rich Output**: Returns both track IDs and names for comprehensive recommendations

---

## ⚠️ Data Usage Disclaimer

> **We do not use any Spotify data for training ML models.**  
> Spotify data is only used for **one-time inference** when users are actively present in a club session.  
> Our ML model is trained on publicly available Spotify audio features dataset, ensuring privacy compliance.

---

## 🛠️ Tech Stack

- **Backend**: Java Spring Boot, PostgreSQL, JPA/Hibernate, Lombok, MapStruct
- **ML Engine**: Python, FastAPI, Scikit-learn, Pandas, NumPy
- **Frontend**: React (planned)
- **Authentication**: Spotify OAuth 2.0
- **APIs**: Spotify Web API, RESTful services
- **Data**: PostgreSQL database, Spotify audio features dataset
- **Deployment**: Docker, AWS (planned)

---

Stay tuned for more updates as we build ClubVibeIQ into the future of smart clubs!

 

# ClubVibeIQ 🎧

**Status:** Building (Stage: Microservice Architecture)

ClubVibeIQ is an intelligent music insight system designed to understand club audiences in real time by analyzing their music preferences and suggesting crowd-based insights to DJs. The system leverages a microservice architecture using Spring Boot, FastAPI, and (soon) a React frontend.

---

## 🔧 Architecture Overview

- **Backend (Spring Boot)**  
  - Handles user authentication and Spotify OAuth integration  
  - Fetches user's most played tracks, top artists, albums (only for inference)  
  - Provides user data to the ML engine for prediction

- **ML Engine (FastAPI)**  
  - Consumes user song data from the backend  
  - Uses a pre-trained model (trained on public multilingual music datasets)  
  - Generates predictions and insights about the club's current crowd

- **Frontend (React)** *(coming soon)*  
  - QR-based club entry system  
  - Interactive DJ dashboard for viewing crowd insights  
  - User-facing interaction to connect Spotify and give one-time consent

---

## 🚀 User Flow

1. User enters a club and scans a QR code.
2. They're redirected to the backend where they authenticate with **Spotify**.
3. Backend fetches Spotify data for **only one-time inference**.
4. Data is sent to the ML Engine.
5. ML Engine predicts and returns insights about the music taste of the crowd.
6. DJs get live audience intelligence and suggested genres/tracks.

---

## ⚠️ Disclaimer

> **We do not use any Spotify data for training the ML model.**  
> Spotify data is only used for **one-time inference** when the user is actively present in a club session.  
> Our ML model is trained on publicly available music datasets, including a diverse range of Indian languages like Hindi, Punjabi, Bhojpuri, and English.

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot (Java)
- **ML Engine**: FastAPI (Python)
- **Frontend**: React (setup pending)
- **Authentication**: Spotify OAuth
- **Deployment**: Docker, AWS (planned)

---

## 📂 Repositories

- `clubvibeiq-backend` – Spring Boot service for auth + Spotify data
- `ml-engine` – FastAPI service for ML-based crowd inference
- `clubvibeiq-frontend` *(coming soon)* – React app for DJ dashboard and user QR flow

---

Stay tuned for more updates as we build ClubVibeIQ into the future of smart clubs!

 

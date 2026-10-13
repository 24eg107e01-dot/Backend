# 🚀 Intelligent Student Performance & Career Path Predictor — Backend

Enterprise-grade **Spring Boot 3** REST API powering automated student performance forecasting, academic risk detection, and career trajectory recommendations.

## 🛠️ Architecture & Tech Stack
- **Framework:** Spring Boot 3.2.4 (Java 17/21)
- **Security:** Spring Security 6 with JWT Token Authentication (`jjwt`)
- **Persistence:** Spring Data JPA / Hibernate (H2 for zero-setup & PostgreSQL for production)
- **Machine Learning Core:**
  - **Ensemble Random Forest:** 50 Bagged Trees modeling threshold risk factors (attendance penalty curves, continuous assessment vulnerabilities)
  - **Multi-Layer Perceptron (Deep ANN):** 3-Layer feedforward neural network for continuous GPA regression
  - **Cosine Vector Alignment:** Matches student competency vectors against 7 industry career benchmarks
- **Cloud Deployment:** Docker multi-stage build configured for **Render**

## 🌐 REST Endpoints
- `GET /api/health` — Service health check (Render probe)
- `POST /api/auth/login` — JWT authentication
- `POST /api/auth/register` — User registration
- `POST /api/predict/gpa-and-career` — Run ML prediction & risk analysis
- `GET /api/predict/history` — Student prediction history
- `GET /api/advisor/metrics` — Advisory dashboard telemetry
- `GET /api/advisor/alerts` — Early intervention alert stream
- `PATCH /api/advisor/alerts/{id}` — Update counseling status

## 🚀 1-Click Deploy to Render
1. Create a **New Web Service** on [Render](https://render.com).
2. Connect this repository (`https://github.com/24eg107e01-dot/Backend.git`).
3. Select **Docker** environment (Render automatically detects `Dockerfile`).
4. Port is dynamically configured to `$PORT` (default 8080).

# QuantityMeasurementApp

A **Spring Boot microservices** application for unit conversion and quantity arithmetic — with JWT authentication, Google OAuth2, and a full operation history. Deployed on **Railway** with a **Vercel** frontend.

---

## 🔗 Live Demo

| Service | URL |
|---|---|
| Frontend | [quantity-measurement-app-frontend-puce.vercel.app](https://quantity-measurement-app-frontend-puce.vercel.app) |
| Backend | [Railway Deployment](https://railway.com/project/473c2b87-8b35-4841-9077-3f684c124589?environmentId=122c79a8-8af8-43e6-bf56-e9944b6c5a0a) |
| Render Deployment

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────┐
│                  Frontend (Vite/React)               │
│         localhost:5173 / Vercel                      │
└─────────────────────┬───────────────────────────────┘
                      │  HTTP
┌─────────────────────▼───────────────────────────────┐
│              API Gateway   :8080                     │
│   • JWT validation (X-User-Name header forwarding)   │
│   • Request logging                                  │
│   • CORS                                             │
└──────┬───────────────────────┬──────────────────┬───┘
       │                       │                  │
┌──────▼──────┐  ┌─────────────▼──────┐  ┌───────▼──────┐
│ Auth Service│  │ Quantity Service   │  │History Service│
│   :8082     │  │      :8081         │  │    :8083      │
│ MySQL DB    │  │    MySQL DB        │  │   MySQL DB    │
└─────────────┘  └────────────────────┘  └──────────────┘
       │                  │                       │
       └──────────────────┴───────────────────────┘
                          │
              ┌───────────▼───────────┐
              │   Eureka Server :8761  │
              │  (Service Discovery)  │
              └───────────────────────┘
```

---

## 📦 Microservices

| Service | Port | Description |
|---|---|---|
| `api-gateway` | 8080 | Single entry point, JWT filter, routing |
| `auth-service` | 8082 | Register, login, Google OAuth2, JWT issuance |
| `quantity-service` | 8081 | Unit conversion + arithmetic operations |
| `history-service` | 8083 | Per-user operation history (save/fetch/clear) |
| `eureka-server` | 8761 | Service registry |

---

## ✨ Features

- **Unit Conversion** — Length, Weight, Temperature, Volume across 25+ units
- **Arithmetic on Quantities** — Add, subtract, multiply, divide, compare values across different but compatible units
- **JWT Authentication** — Stateless, 1-hour tokens
- **Google OAuth2** — Login via Google account
- **Operation History** — Every authenticated conversion/operation is saved and retrievable
- **Caching** — Conversions cached with Spring Cache for faster repeated lookups
- **Swagger UI** — Interactive API docs on every service
- **Dockerized** — Each service has a `Dockerfile`; deployable on Railway

---

## 🔧 Supported Units

| Category | Units |
|---|---|
| **Length** | INCH, FEET, YARD, METER, CENTIMETER, KILOMETER, MILLIMETER, MILE |
| **Weight** | GRAM, KILOGRAM, POUND, OUNCE, TON |
| **Temperature** | CELSIUS, FAHRENHEIT, KELVIN |
| **Volume** | MILLILITER, LITER, GALLON, CUP |

---

## 📡 API Reference

All requests go through the **API Gateway** at port `8080`.

### Auth Service — `/api/auth`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | ❌ | Register a new user |
| `POST` | `/api/auth/login` | ❌ | Login, get JWT token |
| `POST` | `/api/auth/logout` | ✅ Bearer | Logout (token invalidation) |
| `GET` | `/api/auth/profile` | ✅ Bearer | Get current user profile |
| `GET` | `/api/auth/health` | ❌ | Service health check |

**Register / Login request body:**
```json
{ "username": "john_doe", "password": "SecurePass123", "email": "john@example.com" }
```

**Login response:**
```json
{
  "token": "eyJ...",
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER",
  "provider": "local",
  "id": 1,
  "createdAt": "2025-01-01T10:00:00",
  "message": "Success"
}
```

---

### Quantity Service — `/api/quantity`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/quantity/convert` | ❌ (optional) | Convert between units |
| `POST` | `/api/quantity/arithmetic/add` | ✅ Bearer | Add two quantities |
| `POST` | `/api/quantity/arithmetic/subtract` | ✅ Bearer | Subtract quantities |
| `POST` | `/api/quantity/arithmetic/multiply` | ✅ Bearer | Multiply by scalar |
| `POST` | `/api/quantity/arithmetic/divide` | ✅ Bearer | Divide by scalar |
| `POST` | `/api/quantity/arithmetic/compare` | ✅ Bearer | Compare two quantities |
| `GET` | `/api/quantity/all` | ✅ Bearer | Get all saved operations |
| `GET` | `/api/quantity/{id}` | ✅ Bearer | Get operation by ID |
| `DELETE` | `/api/quantity/{id}` | ✅ Bearer | Delete operation by ID |
| `GET` | `/api/quantity/health` | ❌ | Health check |

**Convert example:**
```json
// Request
{ "value": 5, "fromUnit": "FEET", "toUnit": "METER" }

// Response
{ "result": 1.524, "from": "FEET", "to": "METER", "value": 5.0, "operation": "CONVERT", "category": "LENGTH" }
```

**Arithmetic add example:**
```json
// Request
{ "value1": 1, "unit1": "FEET", "value2": 12, "unit2": "INCHES", "resultUnit": "FEET" }

// Response
{ "result": 2.0, "operation": "ADD", "value1": 1.0, "unit1": "FEET", "value2": 12.0, "unit2": "INCHES", "resultUnit": "FEET" }
```

**Compare example:**
```json
// Request
{ "value1": 2, "unit1": "METER", "value2": 100, "unit2": "CENTIMETER" }

// Response
{ "result": "GREATER", "operation": "COMPARE", "value1": 2.0, "unit1": "METER", "value2": 100.0, "unit2": "CENTIMETER" }
```

---

### History Service — `/api/history`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/history/save` | ✅ Bearer | Save a history entry |
| `GET` | `/api/history/my` | ✅ Bearer | Get current user's history |
| `GET` | `/api/history/user/{username}` | ✅ Bearer | Get history for a specific user |
| `DELETE` | `/api/history/clear` | ✅ Bearer | Clear current user's history |
| `GET` | `/api/history/all` | ✅ Bearer | Get all history (admin) |
| `GET` | `/api/history/health` | ❌ | Health check |

---

## 🚀 Running Locally

### Prerequisites

- Java 17+
- Maven
- MySQL (running on port 3306)
- Git

### 1. Clone the repo

```bash
git clone https://github.com/<your-username>/QuantityMeasurementApp.git
cd QuantityMeasurementApp
```

### 2. Create databases

```sql
CREATE DATABASE auth_db;
CREATE DATABASE quantity_db;
CREATE DATABASE history_db;
```

### 3. Configure each service

Update `application.properties` in each service with your local DB credentials and environment variables:

**auth-service:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auth_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# Google OAuth (optional for local dev)
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8082/login/oauth2/code/google

frontend.url=http://localhost:5173
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

**quantity-service & history-service:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quantity_db   # or history_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

### 4. Start services (in this order)

```bash
# 1. Eureka Server
cd eureka-server && mvn spring-boot:run

# 2. Auth Service
cd auth-service && mvn spring-boot:run

# 3. Quantity Service
cd quantity-service && mvn spring-boot:run

# 4. History Service
cd history-service && mvn spring-boot:run

# 5. API Gateway
cd api-gateway && mvn spring-boot:run
```

All traffic goes through: `http://localhost:8080`

---

## 🐳 Docker

Each service has a `Dockerfile`. Build and run individually:

```bash
cd auth-service
docker build -t auth-service .
docker run -p 8082:8082 --env-file .env auth-service
```

---

## ☁️ Deploying to Railway

Each service has a `railway.json` config. Required environment variables per service:

### auth-service
| Variable | Description |
|---|---|
| `JDBC_DATABASE_URL` | Full JDBC URL from Railway MySQL |
| `MYSQLUSER` | DB username |
| `MYSQLPASSWORD` | DB password |
| `GOOGLE_CLIENT_ID` | Google OAuth client ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth client secret |
| `AUTH_SERVICE_URL` | Public URL of this service |
| `FRONTEND_URL` | Vercel frontend URL |
| `EUREKA_URL` | Eureka service URL |

### quantity-service / history-service
| Variable | Description |
|---|---|
| `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE` | Railway MySQL connection details |
| `MYSQLUSER`, `MYSQLPASSWORD` | DB credentials |
| `EUREKA_URL` | Eureka service URL |

### api-gateway
| Variable | Description |
|---|---|
| `AUTH_SERVICE_URL` | Internal URL of auth-service |
| `QUANTITY_SERVICE_URL` | Internal URL of quantity-service |
| `HISTORY_SERVICE_URL` | Internal URL of history-service |

---

## 📖 Swagger / API Docs

Once running, Swagger UI is available at:

| Service | URL |
|---|---|
| Auth | `http://localhost:8082/swagger-ui.html` |
| Quantity | `http://localhost:8081/swagger-ui.html` |
| History | `http://localhost:8083/swagger-ui.html` |
| Eureka Dashboard | `http://localhost:8761` |

---

## 🛡️ Authentication Flow

```
1. User POSTs to /api/auth/login
2. Auth Service validates credentials → issues JWT (1 hour)
3. Client stores JWT and sends it as: Authorization: Bearer <token>
4. API Gateway validates JWT → extracts username → forwards as X-User-Name header
5. Downstream services trust X-User-Name (no re-validation needed)
```

**Google OAuth flow:**
```
1. User visits /oauth2/authorization/google (via API Gateway → Auth Service)
2. Google redirects back to /login/oauth2/code/google
3. Auth Service creates/finds user → issues JWT → redirects to frontend with token
```

---

## 🗂️ Project Structure

```
QuantityMeasurementApp/
├── api-gateway/
│   ├── src/main/java/com/riddhi/api_gateway/
│   │   ├── filter/
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── LoggingFilter.java
│   │   └── util/JwtUtil.java
│   └── src/main/resources/application.yml
│
├── auth-service/
│   ├── src/main/java/com/riddhi/auth_service/
│   │   ├── controller/AuthController.java
│   │   ├── config/SecurityConfig.java
│   │   ├── entity/User.java
│   │   ├── service/AuthService.java
│   │   └── security/JwtFilter.java
│   └── src/main/resources/application.properties
│
├── quantity-service/
│   ├── src/main/java/com/riddhi/quantity_service/
│   │   ├── controller/QuantityController.java
│   │   ├── service/QuantityService.java        ← Core conversion logic
│   │   ├── dto/  (ConvertRequest, ArithmeticRequest, ...)
│   │   └── entity/QuantityOperation.java
│   └── src/main/resources/application.properties
│
├── history-service/
│   ├── src/main/java/com/riddhi/history_service/
│   │   ├── controller/HistoryController.java
│   │   ├── entity/OperationHistory.java
│   │   └── service/HistoryService.java
│   └── src/main/resources/application.properties
│
└── eureka-server/
    └── src/main/resources/application.properties
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Security | Spring Security + JWT (jjwt 0.11.5) + OAuth2 |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Database | MySQL (per-service) |
| ORM | Spring Data JPA / Hibernate |
| Caching | Spring Cache (simple, in-memory) |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven |
| Containerization | Docker |
| Cloud Deployment | Railway (backend) + Vercel (frontend) |

---

## 👩‍💻 Author

**Riddhi Srivastava**

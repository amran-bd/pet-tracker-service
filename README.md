# Pet Tracker Service

![Java](https://img.shields.io/badge/java-24%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)
![Docker](https://img.shields.io/badge/docker-ready-blueviolet)

A RESTful API service for tracking pets with H2 database integration.

## Features

- CRUD operations for pet tracking
- H2 database with web console
- Docker containerization
- Health monitoring endpoints
- Swagger API documentation

## API Documentation

### Base URL
`http://localhost:8080/api/v1/pets/trackers`

### Endpoints

| Method | Path                  | Description                |
|--------|-----------------------|----------------------------|
| POST   | `/`                   | Create new pet tracker     |
| GET    | `/`                   | List all pets (paginated)  |
| GET    | `/{id}`               | Get pet by ID              |
| GET    | `/petType/{petType}`  | Filter by type (CAT/DOG)   |
| GET    | `/statics`            | Get pets outside zone      |

## Curl Example
### 1. Create a Pet Tracker (POST)
   ```bash
        curl --location 'http://localhost:8080/api/v1/pets/trackers' \
        --header 'Content-Type: application/json' \
        --data '{
            "ownerId": 1,
            "petType": "CAT",
            "trackerType": "BIG",
            "inZone": false,
            "lostTracker": false
        }'
  ```
### 2. Get All Pets (GET)
   ```bash
      curl --location 'http://localhost:8080/api/v1/pets/trackers'
  ```
### 3. Get Pet by ID (GET)
   ```bash
      curl --location 'http://localhost:8080/api/v1/pets/trackers/1'
  ```
### 4. Get Pets by Type (GET)
   ```bash
      curl --location 'http://localhost:8080/api/v1/pets/trackers/petType/DOG'
  ```

### 5. Get Pets Outside Zone (GET)
   ```bash
      curl --location 'http://localhost:8080/api/v1/pets/trackers/statics'
  ```
## Prerequisites

- Docker 20.10+
- Docker Compose 2.0+
- JDK 24 (optional for local dev)

## Running with Docker

1. **Build and start containers**:
   ```bash
   docker-compose up -d --build

## Access services

- **Application**: http://localhost:8080
- **Coverage Report**: http://localhost:8081
- **H2 Console**: http://localhost:8080/h2-console
    - **JDBC URL**: `jdbc:h2:mem:pet_trackers`
    - **Username**: `sa`
    - **Password**: `test1373`

1. **Stop Docker Container**:
   ```bash
   docker-compose down

## Local Development
- **Build and Run with Maven**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
- **View Code Coverage Report**:
   ```bash
   target/site/jacoco/index.html
- **Monitoring**:
    - **Health check**:
      ```bash
      http://localhost:8080/actuator/health=
    - **Metrics**:
      ````bash
      http://localhost:8080/actuator/metrics
    - **Swagger UI**:
      ```bash
      http://localhost:8080/api/v1/pet/tracker/swagger-ui/index.html
  
## Project Structure

```
pet-tracker-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tractive/pet/tracker/
|   |   |       ├── constant/
│   │   │       ├── controller/
|   |   |       ├── Entity/
|   |   |       ├── enums/
|   |   |       ├── exception/
|   |   |       ├── mapper/
|   |   |       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       ├── swagger/
|   |   |       ├── util/
│   │   │       └── PetTrackerApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-local.yml
│   │       └── application-sit.yml
│   │       └── application-prod.yml
│   │       └── application-uat.yml
|   └── test/
|       ├── intregration
|       ├── uint
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```
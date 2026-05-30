# PlantsLibrary 🌿

PlantsLibrary is a demo application for managing indoor plants and rooms.
It is built as a hexagonal (ports & adapters) Spring Boot application
with MongoDB as the persistence layer.

The project was created as part of an engineering assignment focused on:

- working with MongoDB and Spring Data MongoDB,
- designing a clean domain model and use cases,
- exposing a REST API with pagination and filtering,
- writing integration & unit tests with Testcontainers,
- measuring test coverage with JaCoCo.

---

## Architecture

The project follows a hexagonal architecture:

- **domain.model** – core business entities (`Plant`, `Room`, `TemperatureComfort`, `SunlightLevel`).
- **domain.usecase** – application services / use cases (`CreatePlantUseCase`, `GetPlantsQueryUseCase`, `UpdatePlantConditionsUseCase`).
- **ports.in** – incoming ports (interfaces used by controllers).
- **ports.out** – outgoing ports (interfaces implemented by adapters).
- **adapters.in.web** – REST controllers and web mappers.
- **adapters.out.mongo** – MongoDB adapters and Spring Data repositories.
- **adapters.out.mongo.document** – MongoDB documents (`PlantDocument`, `RoomDocument`).

MongoDB is accessed via Spring Data repositories; the application talks only to
the `*RepositoryPort` interfaces from the domain layer.

---

## Technology Stack

- Java 21
- Spring Boot 3.5.x
- Spring Web / Spring Data MongoDB / Bean Validation
- MongoDB 7.x
- Testcontainers (MongoDB)
- JUnit 5, MockMvc, AssertJ
- JaCoCo (code coverage)
- Maven

---

## System Requirements

- Java **17+** (project uses Java 21)
- Maven **3.9+**
- Docker (for Testcontainers and optional local MongoDB)
- Git

---

## Running MongoDB locally

You can either use a local MongoDB installation or a Docker container.

### Option 1 – Docker

```bash
docker run -d --name plants-mongo \
  -p 27017:27017 \
  mongo:7
```

### Option 2 - If you have a docker-compose.yml with Mongo:

docker compose up -d mongo

---

### Configuration

Application is configured via application.yml.

Example application.yml:

spring:
data:
mongodb:
uri: mongodb://localhost:27017/plants-library
database: plants-library
# connection pool
connection-pool:
max-size: 50
min-size: 5
max-connection-idle-time: 60000

server:
port: 8080

---

### Building and Running the Application

Clone the repository and build the project:

git clone https://github.com/NadiaGrempka/PlantsLibrary.git
cd PlantsLibrary
mvn clean install

Run the application:

mvn spring-boot:run

The app will start on: http://localhost:8080

---

### REST API
Plants

Base path: /api/plants

Create plant
POST /api/plants
Content-Type: application/json


Request body:

{
"name": "Monstera",
"hydrationLevel": 60,
"humidityLevel": 50,
"sunlightLevel": "MEDIUM",
"fertilizerNeeded": false,
"currentTemperature": 22.5,
"roomId": "living-room"
}

Get plants (list, filter, paging)
GET /api/plants?roomId=living-room&page=0&size=20&sort=name,asc

Query parameters:

roomId (optional) – filter by room.

page (optional, default 0) – page number.

size (optional, default 20) – page size.

sort (optional) – e.g. name,asc.

Response: Spring Data Page<PlantResponse> structure:

{
"content": [
{
"id": "...",
"name": "Monstera",
"hydrationLevel": 60,
"humidityLevel": 50,
"sunlightLevel": "MEDIUM",
"fertilizerNeeded": false,
"currentTemperature": 22.5,
"temperatureComfort": "OK",
"roomId": "living-room"
}
],
"pageable": { ... },
"totalElements": 1,
"totalPages": 1,
"number": 0,
"size": 20
}

Get plant by id
GET /api/plants/{id}


200 OK – plant found.

404 Not Found – plant does not exist.

Update plant conditions
PUT /api/plants/{id}
Content-Type: application/json


Request body (partial update – all fields optional):

{
"hydrationLevel": 70,
"humidityLevel": 55,
"currentTemperature": 24.0,
"fertilizerNeeded": true
}


200 OK – updated plant.

404 Not Found – plant does not exist.

Delete plant
DELETE /api/plants/{id}


204 No Content – deleted.

404 Not Found – plant does not exist.

Get plant names for a room
GET /api/plants/names?roomId=living-room


Response:

[
{ "id": "123", "name": "Monstera" },
{ "id": "456", "name": "Ficus" }
]


---


### Tests and Code Coverage

To run all tests:

mvn clean test


To run tests and generate JaCoCo coverage report:

mvn clean test jacoco:report

---

### Error Handling

The API uses a global @ControllerAdvice with custom error responses:

- 400 Bad Request – validation errors

- 404 Not Found – resource not found (PlantNotFoundException, etc.)

- 500 Internal Server Error – unexpected server errors

Each error response contains:

- timestamp

- status

- errorCode

- message

- path

Running Integration Tests with Testcontainers

The integration tests start a real MongoDB 7 container via Testcontainers.
Docker must be running locally.

Example command:

mvn test -DskipUnitTests=false

---

### License
Project created for educational purposes.

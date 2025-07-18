# TODO.Analytics-Service

This is the **Analytics Microservice** for the TODO application. It provides insights into user productivity based on task activity. It exposes summary and weekly analytics endpoints and records analytic history for future expansion.

---

## 🧹 Tech Stack

* [Java 21](https://openjdk.org/projects/jdk/21/)
* [Spring Boot 3.5](https://spring.io/projects/spring-boot)
* Spring Web + JPA + H2 (in-memory)
* Asynchronous processing with `@Async`
* Docker-ready

---

## 📁 Project Structure

```
src/
├── controller        # REST API endpoints
├── data
│   ├── entity        # JPA entity for storing history
│   └── repository    # Spring Data repository for persistence
├── integration       # Client interface for TaskService (mocked)
├── mapper            # DTO ↔ Entity transformation logic
├── model             # Domain models (DTOs)
├── services          # Business logic
```

---

## 🔐 Authentication

This service currently **assumes authentication is handled upstream**, typically by the API Gateway (YARP). All endpoints require a valid `userId` parameter.

> 🔒 In the future, Zero Trust validation and JWT introspection may be integrated directly into the service.

---

## 📘 API Overview

Base path: *No prefix required* (typically routed behind an API Gateway)

### 📊 `GET /summary`

Returns a global summary of a user's task activity.

**Query Parameters**:

* `userId` (UUID) — Required

**Response**:

```json
{
  "userId": "a1b2c3d4-...-e5f6g7",
  "createdTasks": 20,
  "completedTasks": 15,
  "completionRate": 0.75,
  "firstActivity": "2024-01-01T00:00:00Z",
  "lastActivity": "2024-07-15T00:00:00Z"
}
```

---

### 📅 `GET /weekly`

Returns a user's task statistics for a specific week.

**Query Parameters**:

* `userId` (UUID) — Required
* `weekStartUtc` (ISO-8601 DateTime) — Required

**Response**:

```json
{
  "userId": "a1b2c3d4-...-e5f6g7",
  "dailyStats": [
    { "date": "2024-07-08T00:00:00Z", "created": 3, "completed": 2 },
    ...
  ]
}
```

---

## 📂 Persistence

All analytics summaries are stored in the `AnalyticHistory` table for later tracking or retrospective analysis. This uses JPA + an H2 in-memory database (for development).

---

## 🐳 Docker Support

This service includes a production-ready `Dockerfile`.

**Build & Run**:

```bash
docker build -t todo-analytics .
docker run -p 8080:8080 todo-analytics
```

> Alternatively, this service can be orchestrated using `docker-compose` or Kubernetes via the shared infrastructure repository.

---

## 🧪 Testing

> 🚧 **No tests yet.** Unit and integration tests are planned for future development.

---

## 🚀 Development

**Run locally:**

```bash
./mvnw spring-boot:run
```

Or use an IDE with Spring Boot support.

---

## 🔗 Related Repositories

This service is part of the [TODO Microservice Architecture](https://github.com/Abstractize):

* `todo-gateway` — API gateway using YARP
* `todo-task-service` — Task management in .NET
* `todo-auth-service` — Authentication & identity (C#)
* `todo-web` — Angular frontend
* `todo-common` — Shared models and contracts
* `todo-infrastructure` — Deployment (Yelm, Docker Compose, etc.)

---

## 📄 License

MIT — see `LICENSE` file.

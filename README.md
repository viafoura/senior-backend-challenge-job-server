# Async Job Server

A Spring Boot service to submit, process, and retrieve asynchronous jobs with a mock external processor.

---

## Table of Contents

- [Requirements](#requirements)
- [Running the Service](#running-the-service)
- [Running Tests](#running-tests)
- [Design Notes](#design-notes)
- [Assumptions](#assumptions)

---

## Requirements

- Java 17
- Maven
- MySQL 8 (or Docker)
- Docker (optional, for running mock external service)

---

## Running the Service

1. **Start mock service, DB and Spring Boot app**:

   ```bash
    docker-compose up --build
       ```


   The service runs on `http://localhost:8080`.

2. **Submit a job**:

   ```bash
   POST /jobs
   Content-Type: application/json

   {
       "userId": "11111111-1111-1111-1111-111111111111",
       "payload": "example job data"
   }
   ```

3. **Retrieve a job**:

   ```bash
   GET /jobs/{jobId}
   ```

---

## Running Tests

Run all unit tests:

```bash
./mvnw test
```

Tests cover:

- Job submission
- Job processing (mocked)
- Job retrieval and DTO mapping

---

## Design Notes

- **Architecture**: Hexagonal / Ports & Adapters.  
  - `JobService` handles business logic.  
  - `JobRepository` and `UserRepository` abstract persistence.  
  - REST controllers handle inbound requests.

- **Async handling**:  
  - Spring `@Async` with `ThreadPoolTaskExecutor` (`jobExecutor` bean).  
  - Jobs are submitted and processed asynchronously without blocking REST calls.

- **Failure strategies**:  
  - External service timeouts handled via `WebClient.timeout(Duration.ofSeconds(10))`.  
  - Errors logged; retries could be added for failed external calls.  
  - Invalid input (missing `userId` or job fields) throws `IllegalArgumentException`.

- **Configuration**:  
  - External processor URL configurable in `application.yml` (`external.process.url`).  
  - Default thread pool: 5 core, 10 max, 50 queue capacity.

---

## Assumptions

- Users are preloaded in the database or via a `CommandLineRunner`.  
- Each job is associated with a single user.  
- External processor always returns a JSON with fields `jobId`, `value`, and `status`.  
- Job results are simple string values (mocked for now).  
- Service does not handle authentication or authorization yet.


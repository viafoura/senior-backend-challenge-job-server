# Take-Home Challenge: Async Job Server 

## Overview

Build a job server that allows users to submit asynchronous jobs, processes them by calling an external service, and stores results in a database. The goal is to evaluate your skills in:

- Java
- Clean architecture (Hexagonal / Ports & Adapters)
- SOLID principles
- Async / parallel processing
- Database modeling with associations
- Dockerization

---

## Instructions

1. Build the java artifact by running the command

```bash
./mvnw clean install -DskipTests
```

2. To run the whole system, you can use docker compose to run all 3 services: Mysql database, Mock External Service, and the Job Server

```bash
docker-compose up -d
```

3. There are some integration bash scripts in `/scripts` that you can copy (or run) to submit jobs to the job-server

## Key Design Decisions

1. **Framework**: Spring Boot 4.x with Java 25
2. **Database**: Mysql with Spring Data JPA
3. **Async Processing**: Use Virtual threads instead of Spring Webflux because it requires a bit of a learning curve and time is a factor 
4. **Migration**: Opt-out of using liquibase or flyway for initializing seed data. Instead, created 2 sql scripts that does exactly what I need
5. **Testing**: Opt to write bash scripts for integration tests rather than write extensive *IT.java tests because I needed a quick way to verify that data flow is working

## Functional User Stories

### 1. Submit Job
- **Story:** As a user, I want to submit a job with parameters, so that it will be processed asynchronously.  
- **Acceptance Criteria:**  
  - Submission immediately returns a `jobId` and `status=PENDING`.  
  - Job is processed in the background without blocking the API request.  
  - The job must call an external endpoint:
    ```
    POST http://mock-external:8081/process
    Content-Type: application/json
    Body: { "jobId": "<your-job-id>" }
    ```
  - The endpoint returns a delayed random result; your service should store it in the database.

### 2. Retrieve Job Status / Result
- **Story:** As a user, I want to query the status and result of a submitted job.  
- **Acceptance Criteria:**  
  - Response includes `jobId`, `status` (`PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`), and any results or errors.  
  - If tasks are part of the job, they should be returned with their own `status` and metadata.

### 3. Linked Entities
- **Story:** As a user, I want jobs to be associated with my account and optionally a project.  
- **Acceptance Criteria:**  
  - Jobs reference a valid user (mandatory) and project (optional).  
  - Users and projects can be assumed to already exist in the database.  
  - Provide SQL insert statements (or migrations) for sample users and projects.  
  - Job retrieval returns associated user and project information.

### 4. Async Processing
- **Story:** As a system, jobs may involve calling a slow external service.  
- **Acceptance Criteria:**  
  - Multiple jobs can run in parallel without blocking API requests.  
  - Failures, timeouts, or slow responses in one job do not block others.  
  - Job results returned from `/process` must be persisted in the database.

---

## Non-Functional Requirements / Guidelines

### Architecture
- Write the project in Java in the framework of your choice
- Follow Hexagonal / Ports & Adapters principles.  
- Apply SOLID principles throughout (SRP, OCP, LSP, ISP, DIP).

### Persistence
- Use a relational database (MySQL or Postgres).  
- Model relationships using primary keys and foreign keys.  
- Provide insert data for users and projects.

### Async / Concurrency
- Jobs must be processed in parallel (worker pool, virtual threads, or asynchronous futures).  
- Handle errors, retries, or timeouts gracefully.

### Dockerization
- Provide a `Dockerfile` for your service.  
- Provide a `docker-compose.yml` to run:
  - DB (MySQL/Postgres)  
  - Mock external service at `http://mock-external:8081/process` (delayed random JSON response)

### Testing
- Provide unit tests for at least:
  - Job submission (happy path)  
  - Retrieving a job and its results

---

## Deliverables

1. Forked GitHub repository with:
   - Source code  
   - `Dockerfile` and `docker-compose.yml` modified with the candidate's service  
   - DB migrations and insert data for sample users/projects  
   - Unit tests
2. `README.md` with:
   - Instructions to run the service and tests  
   - Design notes (architecture decisions, async handling, failure strategies)  
   - Any assumptions made

---

## Optional Bonus
- Metrics/logging for jobs processed per minute or failure rates.  
- Generated OpenAPI 3 specification from code, or generate code from a spec

---

## Estimated Completion Time
~4–6 hours


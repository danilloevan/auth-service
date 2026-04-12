# Auth Service - Agent Documentation

This document describes the overall architecture, technology stack, and containerization setup for the **Auth Service** microservice.

## Technology Stack and Versions

This is a modern Java application currently structured as a Spring Boot microservice.

- **Java Version:** 25
- **Spring Boot Version:** 4.0.5
- **Build Tool:** Maven

### Core Dependencies

According to the `pom.xml`, the following key dependencies are in use:

- `spring-boot-starter-webmvc` - Web capabilities for exposing REST APIs.
- `spring-boot-starter-data-jpa` - Data access layer.
- `spring-boot-starter-security` - General application security.
- `spring-boot-starter-security-oauth2-resource-server` - OAuth2 implementation for authenticating requests.
- `postgresql` - Target database engine.
- `lombok` - Boilerplate reduction.
- `spring-boot-starter-actuator` - Application metrics and health checks.

## Development Standards

### Test Driven Development (TDD)

We strictly follow **Test Driven Development (TDD)** for every feature. 

- **Test-First Requirement:** Every feature that we make needs to have tests written **first**.
- **Continuous Validation:** Features must be validated by tests throughout the development process.
- **Mocking:** For testing, we use **Mockito** to mock dependencies and isolate logic.

### Architectural Principles

We maintain high-quality code by adhering to the following industry-standard principles:

- **SOLID:** We follow the five SOLID principles of object-oriented design to ensure our code is robust, flexible, and maintainable.
- **Clean Code:** Code must be readable, well-organized, and expressive. We prioritize clarity over cleverness.
- **Clean Architecture:** We maintain a clear separation of concerns between our domain logic, external interfaces, and infrastructure.
- **KISS (Keep It Simple, Stupid):** This is our most important principle. We avoid over-engineering and strive for the simplest possible solution that solves the problem effectively.

## Deployment & Docker Configuration (AWS Prepared)

The application has been prepared to run in containerized environments like AWS Elastic Container Service (ECS) or AWS Elastic Kubernetes Service (EKS).

Two files have been provided to aid containerization:

1. `Dockerfile`
2. `docker-compose.yml`

### Dockerfile Details

The `Dockerfile` employs a **multi-stage build pattern**:
- **Stage 1 (Build):** Uses `maven:3.9-eclipse-temurin-25` to build the jar package. This skips testing (`-DskipTests`) locally while caching the dependencies.
- **Stage 2 (Runtime):** Uses the minimal `eclipse-temurin:25-jre-alpine` runtime. It creates a special `spring` user (non-root) to run the application, which is considered a security best practice for any target runtime like AWS.

### Docker Compose Details

The `docker-compose.yml` provides a localized replica of the AWS environment configuration, defining two services:

1. **db**: A standard `postgres:15-alpine` container configured with a Docker volume (`pgdata`) to simulate stateful database storage. It implements a healthcheck to ensure smooth startup behavior.
2. **auth-service**: The Spring Boot application. 
   - It is set to wait for the database service to attain a 'healthy' state before launching (`depends_on: condition: service_healthy`).
   - It is configured with environment variables to hook up properly to the database container (e.g. `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`).

#### AWS specific Notes:
- To prepare for AWS ECS deployment, the `docker-compose.yml` file has comments highlighting block definitions like `logging:` configured to point to `awslogs`. This is the standard procedure to integrate Docker logs to AWS CloudWatch.
- Both the application and the database run on a dedicated Docker bridge network (`auth-network`). This closely mirrors standard Virtual Private Cloud (VPC) isolation in AWS.

## Local Development Guide

For active development, there are two common approaches:

### 1. Full Environment (Docker Only)
This is best for testing the production-like setup.
```powershell
docker compose up -d --build
```
The application will be available at `http://localhost:8080` and the database at `localhost:5432`.

### 2. Fast Development (DB in Docker, App in IDE)
This is the recommended way for active coding to avoid rebuilding the image for every change.

1. **Start only the database:**
   ```powershell
   docker compose up -d db
   ```
2. **Run the Spring Boot application:**
   - From your IDE (e.g., IntelliJ, VS Code)
   - Or via CLI: 
     ```powershell
     ./mvnw spring-boot:run
     ```
   
> [!TIP]
> Since the `docker-compose.yml` uses default environment variables that match the DB container, the app will connect automatically to `localhost:5432` if run locally.

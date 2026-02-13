# BinItRight Web App

Spring Boot backend for BinItRight. This service provides authentication, scan API integration, questionnaire-related endpoints, bin/location services, and supporting business APIs used by the Android app.

## 1. Quick Start (Professor Reproduction)

### Prerequisites
- JDK 17
- Maven 3.9+

### Run locally
```bash
mvn spring-boot:run
```

Default local URL:
- `http://localhost:8080`

## 2. Test and Coverage

### Unit tests
```bash
mvn test
```

### Coverage (JaCoCo)
```bash
mvn clean test jacoco:report
```

Coverage outputs:
- XML: `target/site/jacoco/jacoco.xml`
- HTML: `target/site/jacoco/index.html`

## 3. Project Structure (high level)
- `src/main/java/tech3/binitright/config/`
  - security and global app config
- `src/main/java/tech3/binitright/controller/`
  - REST controllers
- `src/main/java/tech3/binitright/service/`
  - business logic and scan orchestration
- `src/main/java/tech3/binitright/model/`
  - entities, requests, responses
- `src/main/java/tech3/binitright/repository/`
  - data persistence layer
- `src/test/java/tech3/binitright/`
  - unit tests for controllers/services/utils

## 4. Notes
- This repo is focused on backend APIs consumed by the mobile app.
- For CI/Sonar, keep unit-test and JaCoCo output paths unchanged.
- Environment-specific secrets should be provided by CI/environment variables, not hardcoded.
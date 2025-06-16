# Pet Management System

A RESTful API service for managing pets, built with Spring Boot.

## Technical Stack

- Java 17
- Spring Boot 3.x
- Maven
- H2 Database (dev)
- OpenAPI/Swagger Documentation
- JUnit5 / Mockito for testing

## Features

- CRUD operations for pets
- Pagination and sorting
- Input validation
- Custom field trimming annotation
- Comprehensive error handling with ControllerAdvice
- Environment-specific configurations
- Logging with Log4j2 and custom log format with correlation ID
- API documentation with OpenAPI at /swagger-ui.html

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.8+
- IDE (IntelliJ IDEA recommended)

### Build & Run

Build
```
mvn clean install
```

Run (dev mode)
```
mvn spring-boot:run -Dspring.profiles.active=dev
```

Run tests
```
mvn test
```
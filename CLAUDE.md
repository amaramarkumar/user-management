# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project state

This is a Spring Boot 3.5.15 / Java 17 application with a MySQL-backed REST API for user management. The application includes:

- Entity: `User` (with fields id, name, email)
- Repository: `UserRepository` (Spring Data JPA)
- Service: `UserService` (business logic)
- Controller: `UserController` (REST endpoints for CRUD operations)
- DTOs: `UserRequest` and `UserResponse` for input/output validation and transformation
- Exception handling: `ResourceNotFoundException`, `DuplicateResourceException`, and a `GlobalExceptionHandler`
- OpenAPI documentation: springdoc-openapi is configured, providing Swagger UI at `/swagger-ui.html` and OpenAPI spec at `/v3/api-docs`
- Lombok: used to reduce boilerplate (annotations like `@Data`, `@Builder`, `@RequiredArgsConstructor`)
- Spring Cloud: the application is a Eureka client (for service discovery)

The dependency set includes Spring Boot Starters for Data JPA, Validation, WebMvc, Springdoc OpenAPI, and Spring Cloud Netflix Eureka Client.

## Commands

The Maven wrapper is checked in — use it (no system Maven required).

```powershell
.\mvnw.cmd spring-boot:run          # run the app (starts on http://localhost:8080)
.\mvnw.cmd test                     # run all tests
.\mvnw.cmd test -Dtest=ClassName    # run a single test class
.\mvnw.cmd test -Dtest=ClassName#methodName   # run a single test method
.\mvnw.cmd clean package            # build the executable jar into target/
.\mvnw.cmd clean package -DskipTests
```

**Note for Eureka**: For local development, a Eureka server must be running on port 8761 (default). You can start one using Spring Cloud Netflix Eureka Server or use the default zone configured in `application.properties`.

## Architecture & conventions

- **Package root:** `com.example.user_management` — note the underscore. `com.example.user-management` is invalid as a Java package, so the hyphen was replaced.
- **Persistence:** Spring Data JPA over MySQL. The app expects a running MySQL at `localhost:3306` with database `mydb` (credentials `root`/`mysql` in `application.properties`). `spring.jpa.hibernate.ddl-auto=update` means Hibernate auto-creates/updates tables from `@Entity` classes on startup — schema is driven by the entities, not migration scripts.
- **API docs:** springdoc-openapi is on the classpath, so once REST controllers exist, Swagger UI is served at `/swagger-ui.html` and the OpenAPI spec at `/v3/api-docs` with no extra config.
- **Lombok:** enabled and configured as an annotation processor in `pom.xml`. Use Lombok annotations (`@Data`, `@Getter`, `@Builder`, `@RequiredArgsConstructor`, etc.) on new classes to match the intended style; it is excluded from the packaged jar.
- **Layered architecture:** The application follows a typical layered structure:
  - `controller`: REST endpoints (handles HTTP requests/responses)
  - `service`: business logic (transactions, validation, orchestration)
  - `repository`: data access interfaces (extending JpaRepository)
  - `entity`: JPA entities (mapped to database tables)
  - `dto`: data transfer objects (for request/payload validation and response shaping)
  - `exception`: custom exceptions and global exception handler
- **Validation:** Bean Validation (Jakarta Validation) is used via `@Valid` on controller method arguments; constraints are defined on DTO fields.
- **Error handling:** Custom exceptions (`ResourceNotFoundException`, `DuplicateResourceException`) are thrown by the service and handled by `@ControllerAdvice` (`GlobalExceptionHandler`) to return appropriate HTTP responses.
- **Service discovery:** The application registers with a Eureka server (configured in `application.properties`). For local development, ensure a Eureka instance is available at `http://localhost:8761/eureka/`.

## Notes

- `spring.jpa.show-sql=true` is on, so SQL is logged to the console during development.
- Running the app or the `@SpringBootTest` context-load test requires a reachable MySQL instance with the configured credentials, or they will fail at startup.
- The H2 database is not used; the application relies on MySQL for persistence.
- When adding new endpoints, consider adding corresponding OpenAPI annotations (as seen in `UserController`) to keep the documentation accurate.
- The application uses builder pattern (via Lombok's `@Builder`) for constructing entities and DTOs where appropriate.
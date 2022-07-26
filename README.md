# SafetyNet API
A simple API created with Spring Boot for learning purposes.

### Behaviour
1. Reads a static file with fictional details about fire stations, persons and their medical records.
2. Exposes 3 CRUD endpoints to modify those 3 entities
3. Exposes additional endpoints for different queries
4. Does not have persistence

### Prerequisites
- Java 11

### Testing
To generate the test coverage reports, run:
```
mvn test
```
The following routes are generated in the project:
- `target/site/surefire-report.html`
- `target/site/jacoco/index.html`

For additional CSS formatting for the SureFire report, run:
```
mvn site 
```

### API documentation
After running the application, OpenAPI descriptions are in:
http://localhost:8080/swagger-ui.html
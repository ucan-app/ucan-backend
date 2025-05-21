# ucan-backend

### Quickstart
- Clone this repository
- Run Docker Desktop
- Run `./gradlew bootRun`
- Start making requests to `localhost:8080`

### Technology Needed
- Your favorite Java code editor, [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) or [VSCode](https://code.visualstudio.com/download) are recommended.
- [Docker Desktop](https://docs.docker.com/get-started/introduction/get-docker-desktop/) for local development
- [pgAdmin 4](https://www.pgadmin.org/download/) to view database state
- [act](https://github.com/nektos/act) to test CI pipelines locally

### Commands
- **Running backend:** `./gradlew bootRun` to run the app locally at `localhost:8080`
- **Formatting code:** `./gradlew spotlessApply` to format code (build will fail if unformatted)
- **Building and testing:** `./gradlew build` to build the app, running all tests
- **Testing singular test:** `./gradlew test --tests "ClassName.testName"` to run a single test
- **Testing CI locally:** ```act --env TESTCONTAINERS_HOST_OVERRIDE=`ipconfig getifaddr en0` -W .github/workflows/ci.yml``` to run the CI workflow locally for testing ([github issue](https://github.com/nektos/act/issues/501))
- **Getting docker port:** `docker port ucan-backend-postgres-1` to find the port that the local db is running on, for use in pgAdmin 4

<ins>***Docker desktop must be running for most of these commands***</ins>

### Processes
- Version Control: GitHub
- Bug Tracking: GitHub Issues
- Build System: Gradle
- Testing: JUnit 5 + Mockito + Spring TestContainers
- CI: GitHub Actions

### Operational Backend Use Cases
- Creating and logging into an account
- Adding badges to an account
- Viewing and editing profile info
- Creating and viewing posts, comments, and replies

### Architecture
```
backend
  config
  gateway
    controller
      ModuleOneController.java
      ModuleTwoController.java
    dto
      moduleone
        RequestDTO.java
      moduletwo
  module
    internal
    DomainEvent.java
    DomainAPI.java
    DomainDTO.java
```

We are using Spring Modulith. The `gateway` package holds the REST controllers. The modules may be implemented with any internal architecture, but must have top-level interfaces, dtos, and events that are accessible by other modules. Prefer using asynchronous (event-based) over synchronous (method call) communication.

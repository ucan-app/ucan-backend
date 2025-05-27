<!--
backend-dev-docs.md
-->

# 💾 Backend Developer Guidelines

## How to Obtain Source Code:

    `$ git clone https://github.com/ucan-app/ucan-backend`

## Directory Layout

`src/test/java/com/ucan/backend` contains tests

`src/main/resources` contains config settings

`src/main/java/com/ucan/backend` contains:

- `config/` – Shared config files (e.g., security, database)
- `gateway/controller/` – REST entry points for modules
- `module/` – All other folders, each contains modular business logic
  - Each module has its own `internal/`, `DomainAPI`, `DomainDTO`, and `DomainEvent`

## How to Build the Software

- Make sure Java 21 is installed.
- Format code using Spotless by running `./gradlew spotlessApply` (build will fail if code is not formatted)
- Build code using `./gradlew build`, which runs all tests (unit + integration) as well

## How to Test the Software

First ensure that Docker is running, otherwise the tests cannot run.

### Run all tests

`$ ./gradlew test`

### Run a specific test

`$ ./gradlew test --tests "ClassName.testName"`

### Test CI locally

`` $ act --env TESTCONTAINERS_HOST_OVERRIDE=`ipconfig getifaddr en0` -W .github/workflows/ci.yml ``

## How to Add New Tests

- Unit tests go in `src/test/java`
- Use `JUnit 5`, `Mockito`, and `Spring TestContainers`
- Name convention: `ClassNameTest.java`
  For CI to pass, all tests must be green and formatted

## How to Build a Release of the Software

- Ensure all tests pass: `./gradlew build`
- Update version in `build.gradle.kts`
- Create a new `release/tag` in GitHub with changelog and version

# Space Of Peoples

A Java 25 Spring Boot service with a React worker directory.

## Run

```sh
cd app-service && ./mvnw spring-boot:run
```

The welcome endpoint is available at `http://localhost:8080/api/hello`.

## Run with Docker

```sh
docker compose up --build
```

The service is available at `http://localhost:8080/api/hello`. Stop it with `docker compose down`.
The PostgreSQL initialization script creates the `workers` table when the data volume is first initialized. The database image does not rerun initialization scripts for an existing volume.
The React worker directory is available at `http://localhost:5173` and fetches records from the JDBC API.

## Test

```sh
cd app-service && ./mvnw test
```

## Package

```sh
cd app-service && ./mvnw package
```
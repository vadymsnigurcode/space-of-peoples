# Space Of Peoples

A Java 25 application built with Spring Boot and Maven.

## Run

```sh
./mvnw spring-boot:run
```

The welcome endpoint is available at `http://localhost:8080/api/hello`.

## Run with Docker

```sh
docker compose up --build
```

The service is available at `http://localhost:8080/api/hello`. Stop it with `docker compose down`.
The PostgreSQL initialization script creates the `workers` table when the data volume is first initialized. The database image does not rerun initialization scripts for an existing volume.

## Test

```sh
./mvnw test
```

## Package

```sh
./mvnw package
```
# Run locally

## Requirements
- Java 21 or higher

## Profiles
- Development profile (default)
  - Uses an in-memory H2 database
- Production profile (`prod`)
  - Uses a PostgreSQL database. Ensure PostgreSQL is running and accessible on `localhost:5432`.

## Configure environment variables
- Rename the file `.env.example` to `.env` and set the required environment variables.

## On Windows
- Build
```bash
./mvnw.cmd clean install
```
- Run (development profile)
```bash
./mvnw.cmd spring-boot:run
```
- Run (production profile)
```bash
./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

## On Linux or macOS
- Build
```bash
./mvnw clean install
```
- Run (development profile)
```bash
./mvnw spring-boot:run
```
- Run (production profile)
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

# Run using Docker

## Requirements
- Docker (or any containerization platform of your choice)
- Docker Compose

## Configure environment variables
- Rename the file `./secrets/postgres-passwd.txt.example` to `./secrets/postgres-passwd.txt` and set the PostgreSQL password.
- Rename the file `.env.example` to `.env` and set the required environment variables. Make sure the `DB_PASSWORD` variable matches the password set in `./secrets/postgres-passwd.txt`.

## Build and Run (production profile only)
- Production profile
```bash
docker-compose up --build
```
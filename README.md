# Run locally

## Requirements
- Java 21 or higher

## Profiles
- Development profile (default)
  - Uses an in-memory H2 database which defaults to url `h2:mem:`.
- Production profile (`prod`)
  - Uses a PostgreSQL database which defaults to url `localhost:5432`.

## Configure environment variables
- Rename the file `.env.example` to `.env` and set the `DB_PASSWORD` environment variable. Optionally you can set `DB_HOST` and `DB_PORT`.
- The database connection will  with username `admin` and database name `dietiestatesdb`.

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
- Rename the file `./secrets/postgres-passwd.txt.example` to `./secrets/postgres-passwd.txt` and set the database password.

## Build and Run
```bash
docker-compose up --build
```
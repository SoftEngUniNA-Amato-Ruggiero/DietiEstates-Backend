# Run locally

## Requirements
- Java 21 or higher
- A running PostgreSQL database with GIS extension (for production profile)

## Profiles
- Development profile (default)
- Production profile (`prod`)

## Configure environment variables
- Rename the file `.env.example` to `.env` and set the `DB_PASSWORD` environment variable. Optionally you can set `DB_HOST` (default: localhost) and `DB_PORT` (default: 5432).
- The database connection will use username `admin` and database name `dietiestatesdb`.

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
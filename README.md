# Run natively

## Requirements
- Java 21 or higher
- A running PostgreSQL database with GIS extension

## Configure environment variables
Rename the file `.env.example` to `.env` and set the following environment variables:
- `DB_PASSWORD` is the only mandatory variable to configure and has no default value. 
- `DB_USER` (default: `admin`).
- `DB_HOST` (default: `localhost`).
- `DB_PORT` (default: `5432`).
- `DB_NAME` (default: `dietiestatesdb`).
- `FRONTEND_URL` (default: `http://localhost:4200`).

## On Windows
- Build
```bash
.\mvnw.cmd clean install
```
- Run
```bash
.\mvnw.cmd spring-boot:run
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

# Run using Docker

## Requirements
- Docker (or any containerization platform of your choice)
- Docker Compose

## Configure environment variables
Rename the file `./secrets/postgres-passwd.txt.example` to `./secrets/postgres-passwd.txt` and set the database password.
The other environment variables can be set in the `docker-compose.yml` file if you want to override the default values.

## Build and Run
```bash
docker-compose up --build
```
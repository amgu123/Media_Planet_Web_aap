# Media Planet Full-Stack Application

A modern, premium admin dashboard built with Spring Boot 3.2, Angular 17, and MySQL.

## Features

- **JWT Authentication**: Secure login with token-based authentication.
- **Premium Admin UI**: Modern glassmorphism design with Bootstrap 5.
- **CRUD Management**:
  - Languages
  - Markets
  - Genres
  - Job Machines
  - Resource Paths (mapped to Job Machines)
- **Dockerized**: Easy deployment using Docker Compose.

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2, Spring Security, Hibernate, JPA, MySQL.
- **Frontend**: Angular 17, Bootstrap 5, Bootstrap Icons, SCSS.
- **Deployment**: Docker, Docker Compose, Nginx.

## Prerequisites

- Docker and Docker Compose installed.
- (Optional) Java 17+ and Node.js 20+ if running without Docker.

## Getting Started

### Using Docker (Recommended)

1. Clone the repository.
2. Navigate to the root directory.
3. Run the following command:
   ```bash
   docker-compose up --build
   ```
4. Access the application:
   - **Frontend**: http://localhost
   - **Backend API**: http://localhost:8080/api

### Default Credentials

- **Username**: `admin`
- **Password**: `admin`

## Development Setup

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
```

## Database Initialization
The database is automatically initialized via the `database/init.sql` script when using Docker Compose. It creates the necessary tables and seeds a default admin user.

# Training Course BDM

Application for managing student lists and groups. This backend system provides an API for
user management, authentication, and organization.

## Table of Contents

- [Overview](#overview)
- [Technologies](#technologies)
- [Features](#features)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Authentication](#authentication)

## Overview

This application serves as a backend management system. It allows administrators and users to create and manage lists of students, organize them into groups, and handle user authentication and authorization.

## Technologies

- **Java 17**
- **Spring Boot**
- **Spring Security** with JWT authentication
- **Spring Data JPA** for database operations
- **PostgreSQL** for production database
- **H2 Database** for testing
- **JUnit** for testing

## Features
- User Management
- List Management
- Group Management
- Student Management
- Mailing for activating a user account

## API Endpoints

### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Authenticate and receive JWT token
- `GET /api/auth/logout` - Logout and invalidate token

### User Management
- `GET /api/users` - Get all users (Admin only)
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/{id}/gdpr` - Get user's GDPR status
- `POST /api/users/{id}/gdpr` - Update user's GDPR status
- `DELETE /api/users/{id}` - Delete user (Admin only)

### List Management
- `GET /api/lists` - Get all lists (Admin only)
- `POST /api/lists/createList` - Create a new list
- `GET /api/lists/{id}` - Get list by ID
- `GET /api/lists/searchList?name={name}` - Search lists by name
- `GET /api/lists/{id}/allStudents` - Get all students in a list
- `PUT /api/lists/{id}/updateNameList` - Update list name
- `GET /api/lists/getAllMyList` - Get all lists for current user
- `DELETE /api/lists/{id}` - Delete a list
- `POST /api/lists/{id}/addGroups` - Add groups to a list
- `POST /api/lists/{id}/addStudent` - Add a student to a list

## Project Structure

The app follows a standard Spring Boot project structure:

```
training-course-bdm/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── bdm/
│   │   │               ├── config/       # Configuration classes
│   │   │               ├── controller/   # REST controllers
│   │   │               ├── dto/          # Data Transfer Objects
│   │   │               ├── exception/    # Custom exceptions
│   │   │               ├── mapper/       # Object mappers
│   │   │               ├── model/        # Entity classes
│   │   │               ├── repository/   # Data repositories
│   │   │               ├── service/      # Business logic
│   │   │               └── utils/        # Utility classes
│   │   └── resources/  # Application properties and resources
│   └── test/           # Test classes
└── pom.xml            # Maven configuration
```

## Authentication

The application uses JSON Web Token / JWT for authentication:

1. Users register or login through the auth endpoints
2. Upon successful authentication, a JWT token is provided as a cookie
3. This token must be included in following requests
4. Token expiration and validation are handled automatically


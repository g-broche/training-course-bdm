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

This application serves as a backend management system. It allows administrators and users to create and manage 
lists of students, organize them into groups, and handle user authentication and authorization.

## Technologies

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Security** with JWT authentication
- **PostgreSQL** for production database
- **H2 Database** for testing
- **JUnit** for testing
- **Springdoc OpenAPI** for API documentation
- **Spring Mail** for email functionality

## Features

- **User Management**
  - User registration and authentication
  - Role-based access control
  - GDPR compliance features
  - Account activation via email

- **List Management**
  - Create, read, update, and delete lists
  - Search lists by name
  - Associate students with lists

- **Group Management**
  - Create and manage student groups
  - Add groups to lists

- **Student Management**
  - Add and manage student information
  - Associate students with lists and groups

- **Email Notifications**
  - Account activation emails
  - Password reset functionality

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

### Test Environment

The test environment uses H2 in-memory database:

```properties
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
```

Run tests with:
```bash
mvn test
```

### API Documentation

The API documentation is available at:
- http://localhost:8080/swagger-ui.html (when running locally)


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

### Group Management
- `GET /api/groups` - Get all groups
- `POST /api/groups` - Create a new group
- `GET /api/groups/{id}` - Get group by ID
- `PUT /api/groups/{id}` - Update a group
- `DELETE /api/groups/{id}` - Delete a group

### Student Management
- `GET /api/students` - Get all students
- `POST /api/students` - Create a new student
- `GET /api/students/{id}` - Get student by ID
- `PUT /api/students/{id}` - Update a student
- `DELETE /api/students/{id}` - Delete a student

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

The application uses JSON Web Token (JWT) for authentication:

1. Users register or login through the auth endpoints
2. Upon successful authentication, a JWT token is provided as a cookie
3. This token must be included in following requests
4. Token expiration and validation are handled automatically

### Security Features

- Password encryption
- Role-based access control
- Token-based authentication
- CORS configuration for frontend access

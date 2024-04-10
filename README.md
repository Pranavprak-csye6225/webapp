# Web Application README

## Introduction
This repository contains the codebase for a web application developed using Java Spring Boot and MySQL database to meet the requirements outlined in the assignment specifications. The application focuses on implementing RESTful APIs, database bootstrapping, authentication with basic token-based authentication, and continuous integration using GitHub Actions.

## Pre-requisites
- Language: Java
- Framework: Spring Boot
- Database: MySQL
- ORM: Hibernate
- Authentication: Basic Authentication
- Continuous Integration: GitHub Actions

## Setting Up the Development Environment
1. Clone the repository from the forked repository:
   ```bash
   git clone git@github.com:Pranavprak/webapp.git
   cd webapp
   ```
2. Ensure you have Java Development Kit (JDK) and MySQL installed.
3. Configure MySQL database and update application properties accordingly

## Configuration
- Database Configuration: Ensure MySQL database is running and update `application.properties` file with the correct database connection details.
- Environment Variables: Locally store the database username and password

## Running the Application
1. Start the MySQL database if not already running
2. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Continuous Integration
Continuous integration is set up on using GitHub Actions. A workflow is triggered on each pull request, ensuring that code compiles successfully before merging.

## Database Bootstrapping
The application automatically bootstraps the MySQL database at startup. It creates or updates schema, tables, indexes, etc., based on the defined entities using Hibernate.

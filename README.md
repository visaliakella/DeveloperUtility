# CommonExceptionHandling

## Description
A Spring Boot application for managing exceptions with AOP (Aspect-Oriented Programming). This project includes features like logging, exception handling, and caching.

## Features
- **AOP for Logging**: Using Aspect-Oriented Programming to log method execution details.
- **Exception Handling**: Centralized exception handling with custom responses.
- **Caching**: Implemented caching to decrease the load on the database and manage exception counts.
- **Performance Monitoring**: Updating the daily exception count to the database to monitor application performance.
- **Execution Time Logging**:Prints before and after method execution, logging the execution time, which helps in performance monitoring and optimization.

# Contact
- **Author**: Akella Visalakshi
- **Email**: visaliakella786@gmail.com
- **LinkedIn**:www.linkedin.com/in/akella-visalakshi-859052189


## Technologies Used
- Java
- Spring Boot
- Spring AOP
- Spring Data JPA
- Hibernate
- MySQL
- SLF4J
- Lombok
- Maven

## Project Structure
```plaintext
CommonExceptionHandling/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── finsol/
│   │   │   │   │   ├── main/
│   │   │   │   │   │   ├── aspects/
│   │   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   │   └── MathOperationsController.java
│   │   │   │   │   │   │   ├── model/
│   │   │   │   │   │   │   │   └── ExceptionCount.java
│   │   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   │   └── Exception_Repo.java
│   │   │   │   │   │   │   ├── utility/
│   │   │   │   │   │   │   │   └── DevUtility.java
│   │   │   │   │   │   └── MainUtility.java
│   │   ├── resources/
│   │   │   └── application.properties
├── .gitignore
├── README.md
├── pom.xml

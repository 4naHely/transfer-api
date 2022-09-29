<h1 align="center">Transfer API</h1>
<h4 align="center"> 
	🚧  Transfer API 🚀 In progress...  🚧
</h4>

<p align="center">
 <a href="#about">About</a> •
 <a href="#how-it-works">How it works</a> • 
 <a href="#tech-stack">Tech Stack</a> • 
 <a href="#improves">Improves</a> • 
 <a href="#author">Author</a>

</p>

---
## About
 REST API that manages the transfer operation between different accounts. 


### Features

- [x] Register an account
- [x] List the accounts registered
- [x] Transfer operation
- [x] Get all transfers related to an account

---
## How it works
### Pre-requisites
Before you begin, you will need to have the following tools installed on your machine:
- Gradle
- JDK 1.8+

or 
- Docker

### Running server with Gradle
```bash
# Builds the application
$ ./gradlew clean build
or
$ gradle clean build

# Run the application 
$ java -jar ./build/libs/transferapi-0.0.1-SNAPSHOT.jar;
# The server will start at port: 8080 - go to http://localhost:8080
```

### Running server with Docker
```bash
# Build and run the application 
$ make
or 
$ make docker_build_run
# The server will start at port: 8080 - go to http://localhost:8080

# Stop docker container
$ make docker_stop
```

[![Run in Postman](https://run.pstmn.io/button.svg)](https://god.gw.postman.com/run-collection/7515737-8442108b-5b10-4cbb-9ee6-5b6e778259af?action=collection%2Ffork&collection-url=entityId%3D7515737-8442108b-5b10-4cbb-9ee6-5b6e778259af%26entityType%3Dcollection%26workspaceId%3D07984745-b6b8-4b2e-b1e6-d99f4a384c52)

### Running tests
```bash
# Run unit tests
$ ./gradlew test

# Run integration tests
$ ./gradlew iT
```

### Basic API Information
Application starts on localhost port 8080 [http://localhost:8080/api] 

| Method | Path                                        | Usage                          |
|--------|---------------------------------------------|--------------------------------|
| POST   | /api/clientAccount/register                 | register a new account         |
| GET    | /api/clientAccount/accounts                 | retrieve all accounts          |
| GET    | /api/clientAccount/accounts/{accountNumber} | retrieve a specific account    |
| POST   | /api/transfers                              | register a new transfer        |
| GET    | /api/transfers/{accountNumber}              | retrieve all account transfers |


### HTTP Status
```python
200 OK: The request has succeeded
201 OK: New resource has been created
400 Bad Request: The request was invalid
409 Conflict: The request has some conflict
404 Not Found: The resource wasn't found
500 Internal Server Error: The server encountered an unexpected condition
```

---
## Tech Stack
The following tools were used in the construction of the project:
- **Java 11**
- **Gradle**
- **RESTful API**
- **Spring Boot**
- **Spring Data JPA**
- **Lombok**
- **H2 in memory Database**
- **Junit4 and Mockito**
- **RestAssured**


---
## Improves

- Implement redis as a queue for "lock" the accounts that are concurrent to avoid conflicts. For example, if a coming
transfer operation has an account that is already on redis (with a TTL as database) then it should wait a bit and retried
the operation.
- Use some ordered queue feature for resilience, like AWS SQS, in case of some problem with server the data won't be lost.
- Delete and Update operations

---
## Author
Made with love by Ana Hely C. T. Kataoka 👋🏽 
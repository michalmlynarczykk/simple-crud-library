# Simple CRUD library
This project is a simple REST API for a library that I have created using Spring Boot. It could perform create, read, update, and delete actions on the MySQL database.
While building this project I decided to add basic authentication (role-based) using Spring Security. Tests for the application were written in BDD style.

## Technologies used for this project
- Java 17
- Maven
- Spring Boot 2.7.5
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL 8 
- H2 Database (test scope In-Memory database)
- JUnit 5
- Mockito
- AssertJ

## How to run
### Requirements and configuration
- MySQL 8 database
- Postman 

In MySQL create the database with the name "library", default name and password are set to "root" and "password" if you would like to change it, go to:
*src/main/resources/application.properties* and change this two fields: *spring.datasource.username* and *spring.datasource.password*. 

Now using Postman you could play around with API. By default this application has two users:
- reader (username = "reader" password = "password")
- admin (username = "admin" password = "password")
 
Every API endpoint is secured, so you have to use the Basic Authentication option in Postman and log in with one of the accounts. Admin has permission to perform every action (CRUD), Reader could only read the content.
### Usage example
Request:
**Method: *GET* url: *http://localhost:8080/api/library/books* (logged as reader)**

Response Header: Status 200 OK

Response Body:
```json
[
    {
        "id": 1,
        "title": "In Desert and Wilderness",
        "authorFirstName": "Henryk",
        "authorLastName": "Sienkiewicz",
        "publicationYear": 1911
    },
    {
        "id": 2,
        "title": "Quo Vadis",
        "authorFirstName": "Henryk",
        "authorLastName": "Sienkiewicz",
        "publicationYear": 1896
    },
    {
        "id": 3,
        "title": "Pan Tadeusz",
        "authorFirstName": "Adam",
        "authorLastName": "Mickiewicz",
        "publicationYear": 1834
    }
]
```
Request:
**Method: *POST* url: *http://localhost:8080/api/library/books* (logged as admin)**

Request Body:
```json
{
    "title": "In Desert and Wilderness",
    "authorFirstName": "Henryk",
    "authorLastName": "Sienkiewicz",
    "publicationYear": 1911
}
```
Response Header: Status 201 Created

Response Body:
```json
{
    "id": 6,
    "title": "In Desert and Wilderness",
    "authorFirstName": "Henryk",
    "authorLastName": "Sienkiewicz",
    "publicationYear": 1911
} 
```

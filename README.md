
## Inventory Management REST API

This is a lightweight Java-based Inventory Management system built with a layered architecture (Controller, Service, DAO).
It exposes REST APIs to manage items and their stock levels.
The application uses a Restlet server for handling HTTP requests and MySQL for persistent storage.
Structured logging is enabled using SLF4J and Logback.
It is designed to demonstrate clean separation of concerns and scalable backend design.

## Features

- Create new inventory items
- Fetch all items or a specific item by ID
- Add or remove stock for an item
- Persistent storage using MySQL
- Structured logging for startup, requests, and errors
- Proper HTTP status codes and error handling

## Project Structure

- `controller` – REST endpoints (`ItemResource`)
- `service` – Business logic (`ItemService`)
- `dao` – Database access layer (`ItemDao`)
- `model` – Domain models (`Item`, `ItemStatus`)
- `resources/logback.xml` – Logging configuration

## Supported REST API Endpoints

| Method | Endpoint                     | Description                          |
|--------|------------------------------|--------------------------------------|
| GET    | `/items`                      | Get list of all items                 |
| GET    | `/items/{id}`                 | Get a specific item by ID              |
| POST   | `/items`                      | Create a new item                      |
| PATCH  | `/items/{id}/add`             | Increase stock of an item               |
| PATCH  | `/items/{id}/remove`          | Decrease stock of an item               |

## Response Codes

- `200 OK` – Successful fetch or update
- `201 Created` – Item successfully created
- `400 Bad Request` – Validation or input error
- `404 Not Found` – Item does not exist
- `500 Internal Server Error` – Unexpected server failure

## Project Architecture
<img width="1669" height="885" alt="image" src="https://github.com/user-attachments/assets/f992abc3-3a59-410a-bdc5-40a8f8acc473" />

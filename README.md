
## Inventory Management REST API

This is a lightweight Java-based Inventory Management system built with a layered architecture (Controller, Service, DAO).
It exposes REST APIs to manage items and their stock levels.
The application uses a Restlet server for handling HTTP requests and MySQL for persistent storage.
Structured logging is enabled using SLF4J and Logback.
It is designed to demonstrate clean separation of concerns and scalable backend design.

## Project Architecture
<img width="1669" height="885" alt="image" src="https://github.com/user-attachments/assets/f992abc3-3a59-410a-bdc5-40a8f8acc473" />

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

## Demo Screenshots

<img width="1312" height="544" alt="image" src="https://github.com/user-attachments/assets/fefd3e8a-0553-4555-bad2-c96059e4cfce" />
<img width="632" height="793" alt="image" src="https://github.com/user-attachments/assets/ccfc8256-7eaf-431e-853c-962a480f0011" />
<img width="657" height="865" alt="image" src="https://github.com/user-attachments/assets/113f1444-78e6-4f9c-80a0-521099c30ff8" />
<img width="444" height="651" alt="image" src="https://github.com/user-attachments/assets/ab87cb2d-9816-4851-bc23-cc1f59435591" />
<img width="487" height="669" alt="image" src="https://github.com/user-attachments/assets/c9821fbb-802d-4df7-b9f6-54d5bc720e90" />



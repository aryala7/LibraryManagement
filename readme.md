# Library Management System

This project is a Library Management System built using Java, Spring Boot, and Maven. It provides functionalities to manage library operations such as managing baskets, orders, and products.

## Technologies Used

- Java
- Spring Boot
- Maven
- PostgreSQL
- Docker
- Adminer

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- Docker
- Docker Compose

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/Aryalav/library-management-system.git
cd library-management-system
```

### Build the Project

```sh
mvn clean install
```

### Running the Application

#### Using Docker Compose

1. Start the services using Docker Compose:

    ```sh
    docker-compose up
    ```

2. The application will be available at `http://localhost:8080`.

3. Adminer will be available at `http://localhost:8081` for database management.

#### Without Docker

1. Update the `application.properties` file with your PostgreSQL database configuration.

2. Run the application:

    ```sh
    mvn spring-boot:run
    ```

## API Endpoints

### Basket Management

- **Create Basket**: `POST /baskets`
- **Add Item to Basket**: `POST /baskets/{basketId}/items`
- **Remove Item from Basket**: `DELETE /baskets/{basketId}/items/{itemId}`
- **Clear Basket**: `DELETE /baskets/{basketId}/clear`

### Order Management

- **Create Order**: `POST /orders`
- **Get Order by ID**: `GET /orders/{orderId}`
- **Update Order**: `PUT /orders/{orderId}`
- **Delete Order**: `DELETE /orders/{orderId}`

## Database Schema

The project uses PostgreSQL as the database. The schema includes tables for users, products, baskets, orders, and their relationships.

## Docker Configuration

The `docker-compose.yml` file includes configurations for the following services:

- **PostgreSQL**: Database service
- **Adminer**: Database management tool
- **App**: Spring Boot application

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

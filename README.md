# SMS Provider Service

A Spring Boot backend for managing clients, balances, and simulated SMS sending.

## Technologies
Java, Spring Boot, PostgreSQL, Spring Data JPA, Maven, Lombok.

## Features
- Client registration and login
- Balance top-up and transaction history
- SMS sending with database-configured pricing
- SMS history, filtering, and pagination
- Admin client management, including block/unblock
- Integration with KUN.UZ

Balance updates and transaction records are saved atomically.

## Notes
SMS delivery is simulated. JWT authentication and admin authorization are not implemented yet.

# 🚗 car-sharing-app
This project is an online management system for car sharing that solves the problems of outdated manual processes. The system allows users to register, browse available cars in real time, make reservations, and pay not only with cash but also with credit cards. Service administrators can easily track rentals, payments, and overdue returns. Users also receive instant notifications about successful bookings, payment confirmations, and overdue reminders. This solution streamlines operations, increases transparency, and improves the overall user experience.

---
## Technology Stack

| Layer                 | Tech Stack                     |
|-----------------------|--------------------------------|
| **Language**          | Java 21                        |
| **Framework**         | Spring Boot                    |
| **Security**          | Spring Security + JWT          |
| **Data Access**       | Spring Data JPA + Hibernate    |
| **Database**          | MySQL (configurable)           |
| **API Documentation** | Swagger (Springdoc OpenAPI)    |
| **Validation**        | Spring Validation              |
| **Testing**           | JUnit, Mockito                 |
| **Dev Tools**         | Lombok, MapStruct              |
| **Auth**              | JWT Token-based authentication |
| **Notification**      | Telegram API                   |
| **Payment**           | Stripe API                     |
---

## Project structure
![structure](https://i.ibb.co/4nNbv0z0/2025-09-19-165547254.png)

---

## Features
### Authentication Controller
| Method | Endpoint                | Description                | Access |
| ------ | ----------------------- |----------------------------| ------ |
| `POST` | `/auth/login`        | Authenticate and get token | Public |
| `POST` | `/auth/registration` | Register a new user        | Public |

### Car Controller
| Method   | Endpoint       | Description                               | Role          |
|----------| -------------- |-------------------------------------------|---------------|
| `GET`    | `/cars`      | viewing the list of all cars information | USER/ADMIN    |
| `GET`    | `/cars/{id}` | viewing all information by car id                   | USER/ADMIN    |
| `POST`   | `/cars`      | creating a new car                             | ADMIN         |
| `PATCH`  | `/cars/{id}` | updating a car invention by id                      | ADMIN         |
| `DELETE` | `/cars/{id}` | deleting the car by id                             | ADMIN         |

### User Controller
| Method  | Endpoint           | Description          | Role         |
|---------|--------------------|----------------------|--------------|
| `GET`   | `/users/me`        | get my profile info  | USER/ADMIN   |
| `PATCH` | `/users/me`        | update profile info  | USER/ADMIN   |
| `PUT`   | `/users/{id}/role` | update user role     | ADMIN        |

### Rentals Controller
| Method  | Endpoint                               | Description                                                          | Role         |
|---------|----------------------------------------|----------------------------------------------------------------------|--------------|
| `POST`  | `/rentals `                            | add a new rental                                                     | USER/ADMIN   |
| `GET`   | `/rentals/?user_id=...&is_active=...`  | get rentals by user ID and whether the rental is still active or not | USER/ADMIN   |
| `GET`   | `/rentals/{id}`                        | get rentals by ID                                                    | USER/ADMIN         |
| `POST`  | `/rentals/{id}/return `                | set actual return date                                               | USER/ADMIN   |

### Payments Controller (Stripe)
| Method    | Endpoint                  | Description                                                        | Role         |
|-----------|---------------------------|--------------------------------------------------------------------|--------------|
| `GET`     | `/payments/?user_id=... ` | get payments by user ID                                            | USER/ADMIN   |
| `POST`    | `/payments`               | create payment session                                             | USER/ADMIN   |
| `GET`     | `/payments/success/`      | check successful Stripe payments (Endpoint for stripe redirection) | USER/ADMIN         |
| `GET`     | `/rentals/{id}/return `   | return payment paused message (Endpoint for stripe redirection)    | USER/ADMIN   |

## API using instructions
### Requirements
- **Java 17+**
- **Maven**
- **Docker**
- **Telegram on your PC or phone (optional)**

### Step 1: Clone the repository

```bash
  git clone https://github.com/Apat1ya/car-sharing-app
  cd your_repo_name
```
### Step 2: Environment Configuration
Create a .env file in the project root (See `.env.example` for a sample.):
```bash
# === MySQL Configuration ===
MYSQLDB_DATABASE=database_name
MYSQLDB_USER=user_name
MYSQLDB_ROOT_PASSWORD=password
        
MYSQLDB_LOCAL_PORT=3305
MYSQLDB_DOCKER_PORT=3306

# === Spring Boot App Ports ===
SPRING_LOCAL_PORT=8081
SPRING_DOCKER_PORT=8080.

# === JWT Configuration ===
JWT_EXPIRATION=5
JWT_SECRET=secretKey

# === Telegram Configuration ===
TELEGRAM_BOT_TOKEN=token
```
These variables are used in `docker-compose.yml` and the Spring Boot configuration.
### Step 3: Start Containers
```bash
docker-compose up --build
```
The app will be available at:
API: `http://localhost:8080`

### Step 4: Stop Containers
```bash
docker-compose down
```
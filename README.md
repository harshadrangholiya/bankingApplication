# Banking Application

A **Spring Boot-based banking application** that provides REST APIs for user registration, authentication, account management, transactions, and reports.

---

## Features

- User registration with roles (CUSTOMER, ADMIN)
- JWT-based authentication and authorization
- Account management (create account, view balance)
- Deposit and withdrawal operations
- Transaction history retrieval
- Monthly transaction reports with total deposits and withdrawals
- Customer address management
- Global exception handling
- Secure password hashing using BCrypt

---

## Tech Stack

- Java 21
- Spring Boot 3.5.6
- Spring Security (JWT)
- Hibernate & JPA
- MySQL
- Maven
- Lombok
- RESTful APIs

---

## Setup & Installation

1. **Clone the repository:**
    git clone https://github.com/harshadrangholiya/bankingApplication
    git clone https://github.com/harshadrangholiya/banking-app

3. **Build the project:**
    mvn clean install

4. **Run the application:**
    mvn spring-boot:run

5. Application will run on `http://localhost:8080`.

---

## Environment Variables

- `spring.datasource.url` → Database connection URL
- `spring.datasource.username` → Database username
- `spring.datasource.password` → Database password
- `app.jwt.secret` → Secret key for JWT signing
- `app.jwt.expiration` → JWT expiration time in milliseconds

---

## API Endpoints

### Authentication

- **Register:** `POST /auth/register`
- **Login:** `POST /auth/login`

### Accounts

- **Create Account:** `POST /accounts/create/{customerId}?accountType=TYPE`
- **Get Balance:** `GET /accounts/balance/{accountNumber}`

### Transactions

- **Deposit:** `POST /transactions/deposit`
- **Withdraw:** `POST /transactions/withdraw`
- **Transaction History:** `GET /transactions/history/{accountNumber}`

### Customer Addresses

- **Add Address:** `POST /customers/{customerId}/addAddress`
- **Get Addresses:** `GET /customers/{customerId}/getAddresses`

### Reports

- **Monthly Report:** `GET /reports/generate-report?month=MM&year=YYYY`


## Database Schema

- **Customer**: id, username, password, fullName, email, phoneNumber, createdAt, roles
- **Role**: id, name
- **Account**: id, accountNumber, accountType, balance, createdAt, customerId
- **Transaction**: id, type, amount, description, transactionTime, accountId
- **CustomerAddress**: id, addressLine1, addressLine2, city, state, postalCode, country, addressType, customerId

## Generating Javadoc

Javadoc can be generated for all classes and packages in the project to provide API documentation.

### Using Command Line

1. Open a terminal or command prompt.
2. Navigate to the project root directory.
3. Run the following command (adjust paths according to your setup):

javadoc -d docs -sourcepath src/main/java -subpackages com.example.banking

## Usage

- Use **Postman** to test APIs.
- Include JWT token in `Authorization` header for secured endpoints:

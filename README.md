# Simple Banking Backend (MVP)

A minimal, modular, portfolio-quality Spring Boot banking backend using JDocs for innovative, model-free JSON handling.

## Features
- Register and login users
- Create accounts
- Check balance
- Deposit, withdraw, and transfer funds
- All account data handled as JSON (no POJOs)
- In-memory storage for local demo

## Prerequisites
- Java 21+
- Maven

## Setup & Run
1. Clone the repo and navigate to this folder:
   ```sh
   git clone https://github.com/kkalchake/banking-backend.git
   cd banking-backend
   ```
2. Build and run:
   ```sh
   mvn clean package
   mvn spring-boot:run

## Example API Usage (with curl)
- Register:
  ```sh
  curl -X POST "http://localhost:8080/api/register?username=yourusername&password=yourpassword"
  ```
- Login:
  ```sh
  curl -X POST "http://localhost:8080/api/login?username=yourusername&password=yourpassword"
  ```
- Create account:
  ```sh
  curl -X POST "http://localhost:8080/api/account?username=yourusername"
  # returns account number
  ```
- Check balance:
  ```sh
  curl "http://localhost:8080/api/balance?accNum=afc37e9d-6dab-4ae5-9e4d-f69c4ec4f799"
  ```
- Deposit:
  ```sh
  curl -X POST "http://localhost:8080/api/deposit?accNum=afc37e9d-6dab-4ae5-9e4d-f69c4ec4f799=1000"
  ```
- Withdraw:
  ```sh
  curl -X POST "http://localhost:8080/api/withdraw?accNum=19430ac1-163b-4385-8db2-f5d96bc0b812&amount=1000&amount=5000"
  ```
- Transfer:
  ```sh
  curl -X POST "http://localhost:8080/api/transfer?from=<from-acc>&to=<to-acc>&amount=10"
  ```

## Notes
- All data is in-memory and resets on restart.
- JDocs is used for all account data (see `SimpleBankingApp.java`).
- No database or extra config needed.

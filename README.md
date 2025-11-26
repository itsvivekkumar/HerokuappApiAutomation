Restful Booker API Automation

This project includes automated API test scripts for the Restful Booker application using Java, Rest Assured, and Maven.
The objective of this automation suite is to validate end-to-end API functionality and ensure high reliability and performance of booking-related services.

🔍 Key Features

Covers CRUD operations on Booking APIs

Validations for request & response specifications

Token-based authentication handling

Reusable payload builders and test utilities

Assertions on response status codes and response data

🧪 Tools & Technologies
Programming Language-	Java
API Automation Framework-	Rest Assured
Build Tool-	Maven
Test Execution-	TestNG  
Version Control	Git & GitHub
📁 Project Structure
src
 ├── test
 │   ├── java
 │       ├── tests → Test classes
 │    
 └── pom.xml → Dependencies & build integrations

▶️ How to Run the Tests

Clone the repository

git clone https://github.com/itsvivekkumar/<your-repo>.git
Navigate to project directory
cd <your-repo>
Execute test suite using Maven
mvn clean test

📌 API Under Test

Base URL: https://restful-booker.herokuapp.com

Functionality tested:

Create Booking
Retrieve Booking
Update Booking
Partial Update
Delete Booking
Auth Token Generation

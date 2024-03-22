# Task Management
## Project Description
The Task management project is a robust system designed to handle authentication and authorization seamlessly, employing a Role-Based Access Control (RBAC) approach with three distinct roles: ADMIN, USER, and SUPER_ADMIN. The primary functionalities revolve around task management, offering various APIs for creating, updating, and deleting tasks, as well as retrieving tasks for a specific user. Additionally, the system provides advanced features such as filtering, sorting, and pagination to enhance user experience and efficiency.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [How to Run the Code](#how-to-run-the-code)
3. [Seeder setup Explanation](#seeder-setup-explanation)
4. [Database Schema](#database-schema)
5. [Code flow](#code-flow)
6. [Functionalities and API endpoints](#functionalities-and-api-endpoints)
7. [Project Structure](#project-structure)
8. [Unit test case](#unit-test-cases)


# Prerequisites

Before you run the Task Management project, ensure that you have the following tools installed on your machine:

## 1. IntelliJ IDEA
- You'll need IntelliJ IDEA, an integrated development environment (IDE) for Java development.
- **Download:** [IntelliJ IDEA](https://www.jetbrains.com/idea/)

## 2. MySQL
- Ensure MySQL is installed on your machine. MySQL is used for database management.
- **Download:** [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

## 3. MySQL Workbench
- MySQL Workbench is essential for accessing databases running on your local MySQL instances via a graphical user interface (GUI).
- **Download:** [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)

## 4. Postman
- Postman is a popular API client used for testing APIs. It facilitates API development and testing.
- **Download:** [Postman](https://www.postman.com/downloads/)

## 5. Java Development Kit (JDK)
- You'll need the Java Development Kit (JDK) installed on your machine to compile and run Java applications.
- **Download:** [Java SE Development Kit](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

Make sure to have these prerequisites installed and configured correctly before running the project.


## How to Run the Code:

### 1. Clone this repository and navigate to the project folder:

   ```sh
   git clone [https://github.com/Amalvc/Apiwiz-assignment.git]
   ```

### 2. Open in IntelliJ or Spring Tool Suite (STS):
- Open IntelliJ IDEA or Spring Tool Suite.
- Import the project by selecting the project directory.
- Allow the IDE to download dependencies and configure the project.

### 3. Create Database

To set up the database for the project, follow these steps:

#### Using MySQL Workbench
1. Open MySQL Workbench.
2. Create a new database for the project. You can give any name to the database.

#### Using Command Line
1. Open your command line interface.
2. Log in to MySQL using the following command:
    ```bash
    mysql -u root -p
    ```
3. Enter your password when prompted.
4. Run the following command to create a new database (replace "apiwiz" with your desired database name):
    ```sql
    create database apiwiz;
    ```

Make sure to replace "apiwiz" with your preferred database name.

### 4. Update application.properties:
- Modify the `application.properties` file in the project's `src/main/resources` directory.
- Add your database username, password, and the name of the database you created:
  ```properties
      server.port=9900
      spring.datasource.url=jdbc:mysql://localhost:3306/database_name
      spring.datasource.username=root
      spring.datasource.password=your_password
      spring.jpa.hibernate.ddl-auto=true
      spring.security.user.name=custom-username
      spring.security.user.password=custom-password
      spring.jpa.properties.hibernate.hbm2ddl.auto=update

      auth.token.expirationInMils=3600000
      auth.token.jwtSecret=secret_key
  ```

### 5. Run the Application in IntelliJ or Spring Tool Suite:
- In your IDE, locate the main application class (typically named `Application` or similar).
- Right-click on the main class and select "Run" or "Debug" to start the application.
- Wait for the application to start. You should see console output indicating that the application has started successfully.

## Seeder Setup Explanation

- Seeder for roles such as USER, SUPER_ADMIN, and ADMIN is added in the codebase.
- A seeder for super admin credentials is also included. The email,password for the super admin credential is provided in the codebase, and it can be changed as needed.
- When the server starts for the first time:
  - The role seeder will run and store role information (USER, SUPER_ADMIN, ADMIN) in the database.
  - Subsequently, the super admin credentials seeder will run and save the super admin's credentials in the database.
- Prioritization is employed in the seeding process:
  - Role seeder runs first to ensure roles are saved in the database.
  - Once roles are saved, the super admin credentials seeder executes to store the super admin's credentials.
- This approach ensures that roles are saved before super admin credentials, maintaining data integrity and consistency.

  

## Database Schema

### User Table

| Field        | Type         |
|--------------|--------------|
| id           | bigint       |
| created_at   | datetime(6)  |
| email        | varchar(255) |
| first_name   | varchar(255) |
| last_name    | varchar(255) |
| password     | varchar(255) |
| role         | bigint       |
| updated_at   | datetime(6)  |

### Role Table

| Field         | Type         |
|---------------|--------------|
| id            | bigint       |
| name          | varchar(255) |

### Task Table

| Field        | Type         |
|--------------|--------------|
| id           | bigint       |
| title        | varchar(255) |
| description  | varchar(255) |
| start_date   | datetime     |
| due_date     | datetime     |
| status       | enum         |

## Code Flow

**1. Initially automatically seed super admin credential**

**2. Any one can register new account, but all registered users have Role USER**

**3. Super admin can promote a user to ADMIN role; there is a separate API for doing this**

**4. Admin and super admin can create a new task for a user; initially, the status of the task is pending**

**5. Admin and super admin can update the status of a task**

**6. Everyone can fetch tasks, but users can only fetch their tasks; however, admins can access all tasks**

**7. Pagination, sorting, filtering are added for the get task API; multiple filters and sorts can be added in ascending or descending order**

**8. Admins and super admins can perform task deletion**


## Functionalities and API Endpoints

1. **Register User or Signup**

    - Description: Users can create an account. Upon registration, the role assigned to this account is USER.
    
    - API Endpoint: `/api/auth/signup`
      
    - Input and response:         
      ![new signup](https://github.com/Amalvc/WishListManagement/assets/88286507/369a375f-191e-4518-8c9a-d9e620060494)

      ![signup response](https://github.com/Amalvc/WishListManagement/assets/88286507/580b2bef-3cef-4359-83e7-ec4aa5584676)


2. **Login**

    - Description: Users and Admins can log in with their credentials. If the credentials are incorrect, an invalid credential exception will be thrown. Upon successful login, a JWT token will be generated and appended with the response.
    
    - API Endpoint: `/api/auth/login`
    
    - Inputand response:
      ![login request](https://github.com/Amalvc/WishListManagement/assets/88286507/a089dafe-b0f6-4fb7-99df-b3ddcad8958d)

      ![login response](https://github.com/Amalvc/WishListManagement/assets/88286507/d5cf73b9-2c25-4e94-b1af-33f6f9c1675e)
3. **Update User Role to ADMIN**

    - Description: Super_admins have the power to promote any user to ADMIN role. This API facilitates that action.
    
    - API Endpoint: `/api/auth/assign-admin/{userId}`
    
    - Input and response:
     

4. **Create Task**

    - Description: Allows the creation of a new task for a particular user. Admins and super_admins can perform this action.
    
    - API Endpoint: `/api/tasks/create`
    - Input and response:
      ![create tassk req](https://github.com/Amalvc/WishListManagement/assets/88286507/dfb2076f-eb7e-4b7d-b24f-0ac6a6b644d2)

      ![create task response](https://github.com/Amalvc/WishListManagement/assets/88286507/a807c8e6-b1b2-41d6-9ba9-45c3d36ad51a)


5. **Get Task (Pagination, Filtering, Sorting)**

    - Description: Fetches tasks for a specific user. Admins and super_admins can fetch all tasks, while users can only access their own. Supports pagination, sorting, and filtering by date, title, and status.
    
    - API Endpoint: `/api/tasks`
    
    - Input and response:
      ![get without req](https://github.com/Amalvc/WishListManagement/assets/88286507/a0d80813-62e2-42bd-9a30-890a61c2c96f)
  
      ![get with req](https://github.com/Amalvc/WishListManagement/assets/88286507/a2247f7d-068b-4469-9388-28d43ffea3bd)

      ![new get response](https://github.com/Amalvc/WishListManagement/assets/88286507/f73ac3d6-e8b1-4d76-bd69-948c17dc37bf)


6. **Update Task Status**

    - Description: Updates the status of a task or its details. Admins and super_admins can access this API.
    
    - API Endpoint: `/api/tasks/update/{id}`
    
    - Input and response:
      ![update status](https://github.com/Amalvc/WishListManagement/assets/88286507/973fc415-6b91-4075-ac78-02ad6a7b1fed)
      
      ![update task full](https://github.com/Amalvc/WishListManagement/assets/88286507/bc67da2d-16ef-491e-8427-6f8205264291)

      ![delete response](https://github.com/Amalvc/WishListManagement/assets/88286507/310cc9b0-18a7-4a8e-b411-b6904c48efe8)

7. **Delete Task**

    - Description: Deletes a task. Admins and super_admins have the authority to perform this action.
    
    - API Endpoint: `/api/tasks/delete/{id}`

    - Input and response:
     delete: ![Screenshot (18)](https://github.com/Amalvc/WishListManagement/assets/88286507/b8d912dd-27d6-411a-a232-c405678fb3d8)



## Project Structure

```Structure
|-- src
| |-- main
| | |-- java
| | | |-- com.algo.algo
| | | | |-- Controller
| | | | |-- Convertor
| | | | |-- Database Seeder
| | | | |-- Dto
| | | | |-- Enum
| | | | |-- Exception
| | | | |-- Model
| | | | |-- Repository
| | | | |-- Security
| | | | |-- Service
| | | | | |-- AgloApplication
| |-- resources
| | |-- application.properties
|-- test
| |-- java
| | |-- com.aglo.aglo
| | | | |-- Controller
| | | | |-- Repository
| | | | |-- Service
| | | | | |-- AgloApplicationTests.java
|-- README.md
|-- pom.xml
```
## Unit test cases

Unit test cases have been created to ensure the functionality and reliability of various components within the application. These tests cover the controller, service, and repository layers of the application.

### Steps to Run Test Cases

1. **Open IntelliJ IDEA**: Launch IntelliJ IDEA IDE on your machine.

2. **Navigate to Test Directory**:
   - In the Project Explorer, navigate to the `src/test/java` directory. This directory contains all the unit test files.

3. **Select Test Type**:
   - Inside the `src/test/java` directory, you'll find three folders: `controller`, `service`, and `repository`. These folders contain tests for different layers of the application.
   - Choose the folder corresponding to the type of tests you want to run. For example:
     - If you want to run controller tests, navigate to the `controller` folder.
     - If you want to run service tests, navigate to the `service` folder.
     - If you want to run repository tests, navigate to the `repository` folder.

4. **Run Test Files**:
   - Inside each folder, you'll find test files corresponding to different components or functionalities of your application.
   - You can run individual test files by selecting them and clicking the "Run" button.
   - Alternatively, if you want to run entire test suites, you can select the main test file (e.g., `WishlistApplicationTests`) and click the "Run" button.

5. **Review Test Results**:
   - After running the tests, IntelliJ IDEA will display the test results in the console.
   - Ensure that all tests pass without any failures or errors. If any test fails, review the error messages to identify and fix issues in your code.

By following these steps, you can efficiently run and validate the unit test cases for your application, ensuring its robustness and reliability.

# Thank You


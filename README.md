# 🧠 ATM Simulation Project
The ATM Simulation Project is a comprehensive Java-based application designed to simulate the functionality of an Automated Teller Machine (ATM). This project aims to provide a realistic and interactive experience, allowing users to perform various transactions such as deposits, withdrawals, and balance inquiries. The application is built using a multi-layered architecture, incorporating data access objects (DAOs), entity classes, and graphical user interface (GUI) components.

## 🚀 Features
- **User Authentication**: Secure login functionality to ensure only authorized users can access their accounts.
- **Account Management**: Users can create, update, and delete their accounts, as well as perform transactions such as deposits and withdrawals.
- **Transaction History**: A record of all transactions performed by the user, providing a clear audit trail.
- **Balance Inquiry**: Users can check their current account balance at any time.
- **GUI Interface**: An intuitive and user-friendly graphical interface makes it easy for users to navigate and perform actions.

## 🛠️ Tech Stack
- **Java**: The primary programming language used for developing the application.
- **Java Swing**: For creating the graphical user interface (GUI) components.
- **MySQL**: The database management system used for storing user and transaction data.
- **JDBC**: Java Database Connectivity API for interacting with the MySQL database.
- **Hibernate**: An object-relational mapping (ORM) tool for simplifying database interactions.

## 📦 Installation
To set up the ATM Simulation Project, follow these steps:
1. **Prerequisites**: Ensure you have Java Development Kit (JDK) 8 or later installed on your system.
2. **Database Setup**: Install MySQL and create a new database for the project.
3. **Project Import**: Import the project into your preferred Java IDE (e.g., Eclipse, IntelliJ IDEA).
4. **Dependency Management**: Use Maven or Gradle to manage project dependencies.
5. **Configuration**: Update the database connection settings in the `DatabaseConnection.java` file to match your MySQL database credentials.

## 💻 Usage
1. **Run the Application**: Execute the `ATMMain.java` file to launch the application.
2. **Login**: Enter your username and password to access the main menu.
3. **Perform Transactions**: Navigate through the GUI to perform various transactions such as deposits, withdrawals, and balance inquiries.

## 📂 Project Structure
```markdown
ATM Simulation Project
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── ATMMain.java
│   │   │   ├── DatabaseConnection.java
│   │   │   ├── DatabaseInitializer.java
│   │   │   ├── AccountDAO.java
│   │   │   ├── UserDAO.java
│   │   │   ├── TransactionDAO.java
│   │   │   ├── Account.java
│   │   │   ├── User.java
│   │   │   ├── Transaction.java
│   │   │   ├── TransactionType.java
│   │   │   ├── MainMenuFrame.java
│   │   │   ├── LoginFrame.java
│   │   ├── resources
│   │   │   ├── database.properties
│   │   │   ├── gui.css
│   ├── test
│   │   ├── java
│   │   │   ├── AccountTest.java
│   │   │   ├── UserTest.java
│   │   │   ├── TransactionTest.java
│   ├── pom.xml (if using Maven) or build.gradle (if using Gradle)
└── README.md
```


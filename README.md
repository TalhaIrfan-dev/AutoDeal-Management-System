# 🚘 AutoDeal Management System — Java Swing Desktop Application

## 📑 Table of Contents
- [Summary](#brief-one-line-Summary)
- [Overview](#overview)
- [Problem Statement](#problem-statement)
- [Dataset](#dataset)
- [Tools & Technologies](#tools--technologies)
- [Features & Methods](#methods)
- [Key Highlights](#Key-Highlights)
- [Dashboard / Model / Output](#dashboard--model--output)
- [How to Run the Project](#how-to-run-this-project)
- [Results & Conclusion](#results--conclusion)
- [Future Improvements](#future-work)
- [Author](#-author) 

---

## Brief One-Line Summary
A Java Swing–based desktop application built in Java for managing customers, vehicles, and sales dealings with MySQL database integration using JDBC.

---

## Overview

The Auto Deal Management System is a feature-rich desktop application developed as a **Second Semester Java Final Project** for the **Object-Oriented Programming (OOP)** course. It is designed to digitally manage and streamline the operations of automobile rental and selling businesses.

The system simulates real-world dealership operations by providing a professional **Java Swing–based graphical user interface (GUI)** and modular functionality. It includes dedicated modules for:
- Vehicle management
- Customer management
- Sales and dealing transactions

The application uses **JDBC for database connectivity**, allowing secure and persistent storage of data in a relational database. Its modular, class-based design follows object-oriented principles, making the system easy to understand, maintain, and extend for future enhancements.

---

## Problem Statement
Traditional automobile dealerships often rely on manual record-keeping or scattered digital files to manage customers, vehicles, and sales transactions. These methods are inefficient, error-prone, and make it difficult to track inventory, maintain accurate customer data, and manage sales records effectively.

This project addresses these challenges by providing:
- **Customer Management:** Centralized storage and management of customer information  
- **Vehicle Inventory Management:** Efficient tracking of vehicle details and availability  
- **Sales & Dealing Management:** Organized handling of sales transactions  
- **Data Persistence:** Secure storage using a relational database  
- **User-Friendly Interface:** A graphical desktop application that simplifies daily operations  

---

## Dataset

The dataset for this project is dynamically generated and maintained by the application using a relational database MYSQL. All data is stored persistently and accessed through JDBC.

The system manages the following datasets:
- **Customer Data**: Customer name, contact information, and address
- **Vehicle Data**: Vehicle model, brand, and price details
- **Dealings Data**: Transaction records linking customers with vehicles

The database structure ensures data consistency and supports efficient storage and retrieval of information.

---

## Tools & Technologies

- **Programming Language:** Java (JDK 8+)  
- **GUI Framework:** Java Swing  
- **Database:** MySQL  
- **Database Connectivity:** JDBC  
- **JDBC Driver:** MySQL Connector/J  
- **IDE:** IntelliJ IDEA / Eclipse / NetBeans  
- **Version Control:** Git & GitHub  
- **Operating System:** Windows / Linux / macOS  

---

## Methods

### System Design
- Object-Oriented Programming principles (Encapsulation, Inheritance, Polymorphism, Abstraction)
- Modular class-based architecture with clear separation of responsibilities
- Three-tier structure: GUI layer, business logic layer, and data access layer

### Core Functionality
- Customer, vehicle, and dealings management using CRUD operations
- Event-driven programming with Java Swing (ActionListeners)
- Centralized menu-based navigation

### Data Handling
- JDBC-based database connectivity
- Structured SQL operations for data persistence
- Reusable database connection through a dedicated connection class
- Input validation and basic error handling using dialogs

---

## Key Highlights

- Role-based user access with secure session handling  
- Customer, vehicle, and dealings management using a modular design  
- Interactive Java Swing GUI with intuitive, menu-driven navigation  
- Real-time database synchronization using JDBC  
- Input validation and business rule enforcement for data integrity  
- Efficient handling of sales and transaction records  
- User-friendly feedback through alerts and confirmation dialogs  
- Clean, readable, and well-structured code suitable for academic evaluation  

---

## Dashboard / Model / Output

### Application UI Preview
The application provides a professional **Java Swing–based graphical user interface** for managing automobile dealership operations. It includes dedicated screens for:
- User login and role-based access
- Vehicle management
- Customer management
- Sales and dealings operations

Each module offers structured forms, tabular data views, validation feedback, and smooth navigation through a centralized dashboard.

### Video Demonstration
A complete video walkthrough of the application is included in this repository, demonstrating:
- Customer and vehicle management workflows
- Sales and dealings processing
- Real-time database interaction using JDBC
- GUI responsiveness and dialog-based user feedback

📹 **Video Demo:**  
[Click here to watch the demo](Visual_Representation/AutoDeal_Demo.mp4)

---

## How to Run This Project

### Prerequisites
- Java JDK 8 or higher
- MySQL Server (or any JDBC-supported relational database)
- MySQL Connector/J (JDBC Driver)
- Any Java IDE (IntelliJ IDEA, Eclipse, or NetBeans)

### Steps to Run
1. Clone the repository
   ```bash
      git clone https://github.com/TalhaIrfan-dev/AutoDeal-Management-System.git
    ```
2. Open the project in your preferred Java IDE  
3. Create a MySQL database (e.g., `AutoDealDB`)  
4. Run the provided SQL scripts:
   * `VRS ( Vehicle Rental & Sales ) Database dump file.sql`
   * `VRS ( Vehicle Rental & Sales ).sql` 
5. Update database credentials in `DatabaseConnection.java`:
   ```java
        String DB_URL = "jdbc:mysql://localhost:3306/AutoDealDB";
        String USER = "root";
        String PASSWORD = "your_password";
   ```
5. Add the MySQL Connector/J JAR to the project classpath (lib folder)  
6. Compile and run `Menue.java`  
7. Use the graphical interface to manage customers, vehicles, and sales dealings  

### Default Login Credentials

| Role  | Username | Password |
|------|----------|----------|
| Admin | admin | admin |

---

## Results & Conclusion

The AutoDeal Management System successfully delivers a complete desktop-based solution for managing automobile dealership operations. The application efficiently handles customer records, vehicle inventory, and sales/dealing transactions through an intuitive Java Swing–based graphical interface with real-time database synchronization.

The project demonstrates effective use of **Object-Oriented Programming principles**, **Java Swing for GUI development**, and **JDBC for reliable database connectivity**. All core functionalities, including CRUD operations, validation, and transaction handling, performed as expected during testing.

Overall, the system meets its academic objectives by translating theoretical OOP concepts into a practical, real-world application. It provides a solid foundation for further enhancements and serves as a reliable, maintainable, and user-friendly dealership management solution.

---

## Future Work

- Implement secure user authentication and role-based access control  
- Add reporting features with PDF/CSV export support  
- Enhance search, filtering, and analytics capabilities  
- Improve UI/UX with themes and accessibility options  
- Refactor the system using a full MVC architecture  
- Develop a web or mobile version for remote access  
- Integrate online payment and notification services  

---

## 👤 Author

**Talha Irfan**  
*BS Software Engineering — Sukkur IBA University*  
- 💼 LinkedIn: https://www.linkedin.com/in/talha-irfan-dev/
- 🌐 GitHub: https://github.com/TalhaIrfan-dev  
- ✉️ Email: talhairfanchoudry@gmail.com  
- 📍 Location: Sukkur, Pakistan

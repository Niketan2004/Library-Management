# Library Management System

## Introduction
This project is a command-line interface (CLI) application developed using Java and JDBC (Java Database Connectivity) for managing a library's book inventory and issuing/returning books. The system allows users to perform various operations such as adding books, issuing books to borrowers, returning books, and displaying available books.

## Features
- **Add Books**: Allows users to add new books to the library inventory by providing the book name and author details.
- **Issue Books**: Enables users to issue books to borrowers by specifying the book ID and borrower details. Checks availability and updates book status accordingly.
- **Return Books**: Facilitates the return of issued books. Updates the book status upon return.
- **View Available Books**: Displays the list of available books in the library inventory.

## Technologies Used
- **Java**: The core programming language used for application logic.
- **JDBC (Java Database Connectivity)**: Used for connecting to and interacting with the MySQL database.
- **MySQL Database**: Stores information about books, borrowers, and issuance records.

## How to Use
1. Ensure you have JDK (Java Development Kit) installed on your system.
2. Set up a MySQL database named 'demo' with the required tables (`books` and `issuance`) using the provided SQL scripts.
3. Update the database connection details (URL, username, and password) in the Java code (`Library.java`).
4. Compile the Java code using `javac Library.java`.
5. Run the compiled Java program using `java Library`.

## About Database

# Tables:

# 1.books:


book_id (INT, PRIMARY KEY, AUTO_INCREMENT): Unique identifier for each book.
book_name (VARCHAR): Name of the book.
author (VARCHAR): Author of the book.
status (VARCHAR): Status of the book (e.g., 'available', 'issued').


# 2.issuance:

- **Columns:**
issue_id (INT, PRIMARY KEY, AUTO_INCREMENT): Unique identifier for each issuance record.
book_id (INT, FOREIGN KEY): Foreign key referencing the book_id in the books table.
issuer_name (VARCHAR): Name of the borrower who issued the book.
issuer_contact_details (VARCHAR): Contact details of the borrower.
issued_date (DATETIME): Date and time when the book was issued.
status (VARCHAR): Status of the issuance (e.g., 'issued', 'returned').

# Relationships:
The books table has a one-to-many relationship with the issuance table based on the book_id column, as one book can be issued multiple times.

## Queries:- 
- **Create books table**
CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    book_name VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'available'
);

-- **Create issuance table**
CREATE TABLE issuance (
    issue_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT,
    issuer_name VARCHAR(255) NOT NULL,
    issuer_contact_details VARCHAR(255) NOT NULL,
    issued_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'issued',
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);


## Project Structure
- `Library.java`: Main Java file containing the CLI application logic.
- `Books.java`: Class file handling book-related operations.
- `README.md`: Project documentation providing an overview, features, usage instructions, and technology details.

## Contributors
- [Niketan]

Feel free to contribute to the project by adding new features, fixing bugs, or improving documentation!


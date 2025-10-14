# NovaBook Library Management System

A layered Java 17 application with JOptionPane UI, built in NetBeans 27.  
Implements JDBC repositories with a Singleton connection manager, secure password hashing, and custom exception handling.

## Key Features

- **User Management**: Multi-user system with Admin and Member roles
- **Security**: BCrypt password hashing and role-based access control
- **Book Operations**: Browse, search, and manage book inventory
- **Lending System**: Borrow, return, and track book loans
- **Member Management**: Registration and profile management
- **Admin Features**: System configuration and advanced management options

---

## 🧩 Project Structure

```
com.codeup.novabook
├─ domain/           # Domain models (POJOs)
│  ├─ Book.java
│  ├─ Member.java
│  ├─ Lending.java
│  └─ User.java
├─ exceptions/       # Custom exceptions
│  └─ DatabaseException.java
├─ repository/       # Repository interfaces
│  ├─ BookRepository.java
│  ├─ MemberRepository.java
│  ├─ LendingRepository.java
│  └─ UserRepository.java
├─ repository/jdbc/  # JDBC implementations
│  ├─ BookRepositoryJDBC.java
│  ├─ MemberRepositoryJDBC.java
│  ├─ LendingRepositoryJDBC.java
│  └─ UserRepositoryJDBC.java
├─ service/          # Service interfaces
│  ├─ BookService.java
│  ├─ MemberService.java
│  ├─ LendingService.java
│  └─ UserService.java
├─ service/impl/     # Service implementations
│  ├─ BookServiceImpl.java
│  ├─ MemberServiceImpl.java
│  ├─ LendingServiceImpl.java
│  └─ UserServiceImpl.java
├─ connection/       # Database connection (Singleton)
│  ├─ ConnectionFactory.java
│  └─ TestConnection.java
├─ infra/config/     # Configuration
│  └─ AppConfig.java
├─ ui/               # JOptionPane UI layer
│  └─ NovaBookUI.java
└─ NovaBook.java     # Main application class
```

---

## ⚙️ Requirements

- **JDK:** 17  
- **IDE:** NetBeans 27 (recommended)  
- **Database:** MySQL 8.0+  
- **Maven:** for dependency management and build
- **MySQL Connector/J:** 8.0+

---

## 🚀 Run the Application

### 1. Database Setup

First, create the database and tables:

```sql
-- Run the SQL script in src/main/java/com/codeup/novabook/db/NovaBookDBSchema.sql
CREATE DATABASE IF NOT EXISTS novabook_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE novabook_db;

-- The script will create all necessary tables and user
```

### 2. Configuration

Configure the database connection in `src/main/resources/app.props`:

```properties
db.vendor=mysql
db.host=localhost
db.port=3306
db.name=novabook_db
db.user=root
db.password=Qwe.123*
db.useSSL=false
```

### 3. Build and Run

#### Using Maven:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.codeup.novabook.NovaBook"
```

#### Using NetBeans:
1. Open the project in NetBeans 27
2. Right-click on the project → Run Project
3. Or run the `NovaBook` main class directly

#### Using Command Line:
```bash
# Compile
javac -cp "target/classes:lib/*" src/main/java/com/codeup/novabook/*.java

# Run
java -cp "target/classes:lib/*" com.codeup.novabook.NovaBook
```

---

## 🎯 Features

### Book Management
- ✅ Add new books with ISBN validation
- ✅ Search books by title, author, or availability
- ✅ Update book information
- ✅ Remove books from library
- ✅ Stock management

### Member Management
- ✅ Register new members (Regular/Premium)
- ✅ Search members by name
- ✅ Update member information
- ✅ Deactivate/activate members
- ✅ Role management

### Lending Management
- ✅ Lend books to members
- ✅ Return books
- ✅ Track active lendings
- ✅ View overdue lendings
- ✅ Extend lending periods
- ✅ Automatic fine calculation

### User Management
- ✅ Register system users (User/Admin)
- ✅ User authentication
- ✅ Update user profiles
- ✅ Deactivate users
- ✅ Role-based access control

### Reports & Statistics
- ✅ Library statistics
- ✅ Member statistics
- ✅ Lending statistics
- ✅ Overdue tracking

---

## 🧱 Design Patterns Used

### 1. Layered Architecture
- **Domain Layer:** Entity classes (Book, Member, Lending, User)
- **Repository Layer:** Data access interfaces and JDBC implementations
- **Service Layer:** Business logic interfaces and implementations
- **UI Layer:** JOptionPane-based user interface

### 2. Singleton Pattern
- `ConnectionFactory` ensures only one database connection instance
- Thread-safe implementation with double-checked locking

### 3. Repository Pattern
- Abstracts data access operations
- Easy to switch between different data sources
- Clean separation of concerns

### 4. Custom Exception Handling
- `DatabaseException` for database-related errors
- Proper error propagation and handling

---

## 🔧 Technical Details

### Database Schema
- **Users Table:** System users with roles and access levels
- **Book Table:** Library books with ISBN, title, author, and stock
- **Member Table:** Library members with roles and status
- **Lending Table:** Book lending transactions with due dates

### Key Components
- **ConnectionFactory:** Singleton database connection manager
- **AppConfig:** Configuration management using Properties
- **Repository Interfaces:** Define data access contracts
- **Service Layer:** Business logic and validation
- **UI Layer:** User-friendly JOptionPane dialogs

### Validation Rules
- Email format validation
- ISBN format validation
- Stock cannot be negative
- Member/user name validation
- Business rule enforcement (max books per member, etc.)

---

## 📝 Usage Examples

### Adding a Book
1. Select "Book Management" → "Add Book"
2. Enter ISBN, title, author, and stock
3. System validates ISBN format and saves book

### Lending a Book
1. Select "Lending Management" → "Lend Book"
2. Enter member ID, book ID, and lending days
3. System checks availability and creates lending record

### Viewing Statistics
1. Select "Reports" → "Library Statistics"
2. View total books, members, users, and lendings

---

## 🐛 Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check database credentials in `app.props`
- Ensure `novabook_db` database exists
- Run the schema script to create tables

### Compilation Issues
- Ensure JDK 17 is installed
- Check MySQL Connector/J is in classpath
- Verify all dependencies are available

### Runtime Issues
- Check database connection
- Verify user permissions
- Review error messages in console

---

## 📘 Author

**Developed by Adrián Gutiérrez**

---

## � Recent Updates

### Security Enhancements (October 2025)
- Implemented BCrypt password hashing with work factor 12
- Added salted password storage
- Improved password verification process

### System Improvements
- Fixed loan management functionality
- Updated method naming for consistency (lending → loan)
- Improved user session handling
- Enhanced input validation
- Fixed date handling in database operations

### Bug Fixes
- Resolved date casting issues in lending operations
- Fixed user session management
- Corrected method name inconsistencies
- Improved error handling and user feedback

---

## �📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
```

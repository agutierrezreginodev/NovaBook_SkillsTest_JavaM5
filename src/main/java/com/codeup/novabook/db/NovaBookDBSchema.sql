/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Adrián Gutiérrez
 */

CREATE DATABASE IF NOT EXISTS novabook_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE novabook_db;

CREATE USER 'novabook_user'@'localhost' IDENTIFIED BY 'novabook!123';
GRANT ALL PRIVILEGES ON novabook_db.* TO 'novabook_user'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    role ENUM('USER','ADMIN','STAFF','MEMBER') NOT NULL DEFAULT 'USER',
    access_level ENUM('READ_ONLY','READ_WRITE', 'MANAGE') NOT NULL DEFAULT 'READ_WRITE',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    stock INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS member (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    role ENUM('REGULAR','PREMIUM') NOT NULL DEFAULT 'REGULAR',
    access_level ENUM('READ_ONLY','READ_WRITE', 'MANAGE') NOT NULL DEFAULT 'READ_WRITE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS lending (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT,
    book_id INT,
    lending_date DATE,
    due_date DATE,
    returned BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);

-- Insert test users with BCrypt-hashed passwords
-- Passwords are:
-- admin@123 -> $2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewqhrPtR6sGdVL6m
-- librarian@123 -> $2a$12$QOpvpqz7zIHrlzx5QY1OY.XWFSBExX9/g5vCQWoS0k4oKUfvc3.TO
-- member@123 -> $2a$12$bVUBXkqxw0Y1RPXpygL5puL8/36HXJ1j9D3v7J/BT7LQYhXqgHX.C
INSERT INTO users (name, email, password, phone, role, access_level, active, deleted, created_at, updated_at) VALUES 
('System Admin', 'admin@novabook.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewqhrPtR6sGdVL6m', '555-0100', 'ADMIN', 'MANAGE', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Library Staff', 'librarian@novabook.com', '$2a$12$QOpvpqz7zIHrlzx5QY1OY.XWFSBExX9/g5vCQWoS0k4oKUfvc3.TO', '555-0101', 'STAFF', 'READ_WRITE', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('John Doe', 'john.doe@email.com', '$2a$12$bVUBXkqxw0Y1RPXpygL5puL8/36HXJ1j9D3v7J/BT7LQYhXqgHX.C', '555-0102', 'MEMBER', 'READ_ONLY', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jane Smith', 'jane.smith@email.com', '$2a$12$bVUBXkqxw0Y1RPXpygL5puL8/36HXJ1j9D3v7J/BT7LQYhXqgHX.C', '555-0103', 'MEMBER', 'READ_ONLY', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob Wilson', 'bob.wilson@email.com', '$2a$12$bVUBXkqxw0Y1RPXpygL5puL8/36HXJ1j9D3v7J/BT7LQYhXqgHX.C', '555-0104', 'MEMBER', 'READ_ONLY', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test members
INSERT INTO member (name, active, deleted, role, access_level, created_at, updated_at)
VALUES 
('John Doe', TRUE, FALSE, 'REGULAR', 'READ_ONLY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jane Smith', TRUE, FALSE, 'PREMIUM', 'READ_ONLY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob Wilson', TRUE, FALSE, 'REGULAR', 'READ_ONLY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert programming and technology books
INSERT INTO book (isbn, title, author, stock, created_at, updated_at)
VALUES 
('978-0134685991', 'Effective Java', 'Joshua Bloch', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('978-0596009205', 'Head First Java', 'Kathy Sierra', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('978-0134757599', 'Core Java Volume I', 'Cay S. Horstmann', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('978-0596007126', 'Head First Design Patterns', 'Eric Freeman', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('978-0321349606', 'Java Concurrency in Practice', 'Brian Goetz', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('978-0596516680', 'Database Design Patterns', 'Michael Ross', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('978-0137673629', 'Core Python Programming', 'Wesley Chun', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test lendings with different scenarios
-- Current loans
INSERT INTO lending (member_id, book_id, lending_date, due_date, returned, created_at, updated_at)
VALUES
(1, 1, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY), FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY), FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Overdue loans (20 days ago, due 14 days ago)
INSERT INTO lending (member_id, book_id, lending_date, due_date, returned, created_at, updated_at)
VALUES
(1, 3, DATE_SUB(CURRENT_DATE, INTERVAL 20 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 6 DAY), FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 4, DATE_SUB(CURRENT_DATE, INTERVAL 20 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 6 DAY), FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Returned loans
INSERT INTO lending (member_id, book_id, lending_date, due_date, returned, created_at, updated_at)
VALUES
(2, 5, DATE_SUB(CURRENT_DATE, INTERVAL 10 DAY), DATE_ADD(CURRENT_DATE, INTERVAL 4 DAY), TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 6, DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY), DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY), TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Display test account credentials for reference
SELECT '=== Test Account Credentials ===' as '';
SELECT 'Role' as 'Role', 'Email' as 'Email', 'Password' as 'Plain Password' UNION ALL
SELECT 'ADMIN', 'admin@novabook.com', 'admin@123' UNION ALL
SELECT 'STAFF', 'librarian@novabook.com', 'librarian@123' UNION ALL
SELECT 'MEMBER', 'john.doe@email.com', 'member@123' UNION ALL
SELECT 'MEMBER', 'jane.smith@email.com', 'member@123' UNION ALL
SELECT 'MEMBER', 'bob.wilson@email.com', 'member@123';

-- Display data summary
SELECT '=== Test Data Summary ===' as '';
SELECT 'Books Available:', COUNT(*) FROM book;
SELECT 'Active Members:', COUNT(*) FROM member WHERE active = TRUE;
SELECT 'Current Loans:', COUNT(*) FROM lending WHERE returned = FALSE;
SELECT 'Overdue Loans:', COUNT(*) FROM lending WHERE returned = FALSE AND due_date < CURRENT_DATE;
SELECT 'Returned Loans:', COUNT(*) FROM lending WHERE returned = TRUE;
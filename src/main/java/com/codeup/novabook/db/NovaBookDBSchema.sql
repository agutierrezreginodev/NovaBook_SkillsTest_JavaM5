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
    role ENUM('USER','ADMIN','MEMBER') NOT NULL DEFAULT 'USER',
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

-- Insert default admin user (password: admin123)
INSERT INTO users (name, email, password, phone, role, access_level, active, deleted, created_at, updated_at)
VALUES ('Admin User', 'admin@novabook.com', 'admin123', '555-0000', 'ADMIN', 'MANAGE', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert some test members (password: pass123)
INSERT INTO users (name, email, password, phone, role, access_level, active, deleted, created_at, updated_at)
VALUES 
    ('John Doe', 'john.doe@email.com', 'pass123', '555-0001', 'MEMBER', 'READ_WRITE', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Jane Smith', 'jane.smith@email.com', 'pass123', '555-0002', 'MEMBER', 'READ_WRITE', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Bob Wilson', 'bob.wilson@email.com', 'pass123', '555-0003', 'MEMBER', 'READ_WRITE', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert corresponding member records
INSERT INTO member (name, active, deleted, role, access_level, created_at, updated_at)
VALUES 
    ('John Doe', TRUE, FALSE, 'REGULAR', 'READ_WRITE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Jane Smith', TRUE, FALSE, 'PREMIUM', 'READ_WRITE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Bob Wilson', TRUE, FALSE, 'REGULAR', 'READ_WRITE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert some sample books
INSERT INTO book (isbn, title, author, stock)
VALUES 
    ('978-0-7475-3269-9', 'Harry Potter and the Philosopher''s Stone', 'J.K. Rowling', 5),
    ('978-0-06-112241-5', 'The Hobbit', 'J.R.R. Tolkien', 3),
    ('978-0-385-47454-5', 'The Da Vinci Code', 'Dan Brown', 4),
    ('978-0-7432-4722-1', 'Angels & Demons', 'Dan Brown', 2),
    ('978-0-316-01202-8', 'Twilight', 'Stephenie Meyer', 3);
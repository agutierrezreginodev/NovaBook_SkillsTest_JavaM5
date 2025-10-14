/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

import java.time.Instant;

/**
 *
 * @author Adrián Gutiérrez
 */
public class Book {

    private int id;
    private String isbn;
    private String title;
    private String author;
    private int stock;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Constructor for Book
     * <p>
     * Creates a book instance with ISBN, title, author, and initial stock.
     * </p>
     * 
     * @param id        ID of the book
     * @param isbn      ISBN of the book
     * @param title     Title of the book
     * @param author    Author of the book
     * @param stock     Stock of the book
     * @param createdAt Created at of the book
     * @param updatedAt Updated at of the book
     */
    public Book(int id, String isbn, String title, String author, int stock, Instant createdAt, Instant updatedAt) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID cannot be less than or equal to 0");
        }
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be less than 0");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at cannot be null");
        }

        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Getter for the id of the book
     * 
     * @return id of the book
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the ISBN of the book
     * 
     * @return ISBN of the book
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Getter for the title of the book
     * 
     * @return title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the author of the book
     * 
     * @return author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Getter for the stock of the book
     * 
     * @return stock of the book
     */
    public int getStock() {
        return stock;
    }

    /**
     * Getter for the created at of the book
     * 
     * @return created at of the book
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Getter for the updated at of the book
     * 
     * @return updated at of the book
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Setter for the id of the book
     * 
     * @param id id of the book
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID cannot be less than or equal to 0");
        }
        this.id = id;
    }

    /**
     * Setter for the ISBN of the book
     * 
     * @param isbn ISBN of the book
     */
    public void setIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        this.isbn = isbn;
    }

    /**
     * Setter for the title of the book
     * 
     * @param title title of the book
     */
    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    /**
     * Setter for the author of the book
     * 
     * @param author author of the book
     */
    public void setAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        this.author = author;
    }

    /**
     * Setter for the stock of the book
     * 
     * @param stock stock of the book
     */
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be less than 0");
        }
        this.stock = stock;
    }

    /**
     * Setter for the created at of the book
     * 
     * @param createdAt created at of the book
     */
    public void setCreatedAt(Instant createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null");
        }
        this.createdAt = createdAt;
    }

    /**
     * Setter for the updated at of the book
     * 
     * @param updatedAt updated at of the book
     */
    public void setUpdatedAt(Instant updatedAt) {
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at cannot be null");
        }
        this.updatedAt = updatedAt;
    }

    /**
     * toString method for the book
     * 
     * @return string representation of the book
     */
    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", isbn=" + isbn + ", title=" + title + ", author=" + author + ", stock=" + stock
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }

}

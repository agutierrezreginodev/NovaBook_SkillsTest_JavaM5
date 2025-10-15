package com.codeup.novabook.exceptions.book;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String identifier) {
        super("Book not found with identifier: " + identifier);
    }
    
    public BookNotFoundException(int id) {
        super("Book not found with ID: " + id);
    }
}
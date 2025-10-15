package com.codeup.novabook.exceptions.book;

public class DuplicateISBNException extends RuntimeException {
    public DuplicateISBNException(String isbn) {
        super("A book with ISBN " + isbn + " already exists.");
    }
}
package com.codeup.novabook.exceptions.lending;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(int bookId) {
        super("Book with ID " + bookId + " is not available for lending (no stock).");
    }
}
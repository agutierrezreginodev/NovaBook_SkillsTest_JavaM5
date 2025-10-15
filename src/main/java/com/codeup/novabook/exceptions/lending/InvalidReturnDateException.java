package com.codeup.novabook.exceptions.lending;

import java.time.LocalDate;

public class InvalidReturnDateException extends RuntimeException {
    public InvalidReturnDateException(LocalDate returnDate, LocalDate lendingDate) {
        super("Return date (" + returnDate + ") cannot be before lending date (" + lendingDate + ")");
    }
}
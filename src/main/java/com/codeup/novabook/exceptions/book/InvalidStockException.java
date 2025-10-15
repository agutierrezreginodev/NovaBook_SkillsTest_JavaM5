package com.codeup.novabook.exceptions.book;

public class InvalidStockException extends RuntimeException {
    public InvalidStockException(String message) {
        super(message);
    }
    
    public static InvalidStockException negativeStock(int stock) {
        return new InvalidStockException("Stock cannot be negative. Provided: " + stock);
    }
    
    public static InvalidStockException insufficientStock(int requested, int available) {
        return new InvalidStockException("Insufficient stock. Requested: " + requested + ", Available: " + available);
    }
}
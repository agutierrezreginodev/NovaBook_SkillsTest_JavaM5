/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.repository;

import com.codeup.novabook.domain.Book;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entity operations.
 * Defines the contract for Book data access operations.
 * 
 * @author Adrián Gutiérrez
 */
public interface BookRepository {
    
    /**
     * Saves a book to the database.
     * 
     * @param book the book to save
     * @return the saved book with generated ID
     */
    Book save(Book book);
    
    /**
     * Finds a book by ID.
     * 
     * @param id the book ID
     * @return Optional containing the book if found, empty otherwise
     */
    Optional<Book> findById(int id);
    
    /**
     * Finds a book by ISBN.
     * 
     * @param isbn the book ISBN
     * @return Optional containing the book if found, empty otherwise
     */
    Optional<Book> findByIsbn(String isbn);
    
    /**
     * Finds all books.
     * 
     * @return list of all books
     */
    List<Book> findAll();
    
    /**
     * Finds books by title containing the given text.
     * 
     * @param title the title text to search for
     * @return list of books matching the title
     */
    List<Book> findByTitleContaining(String title);
    
    /**
     * Finds books by author containing the given text.
     * 
     * @param author the author text to search for
     * @return list of books matching the author
     */
    List<Book> findByAuthorContaining(String author);
    
    /**
     * Finds books with stock greater than the specified amount.
     * 
     * @param minStock the minimum stock amount
     * @return list of books with sufficient stock
     */
    List<Book> findByStockGreaterThan(int minStock);
    
    /**
     * Updates a book.
     * 
     * @param book the book to update
     * @return the updated book
     */
    Book update(Book book);
    
    /**
     * Deletes a book by ID.
     * 
     * @param id the book ID to delete
     * @return true if deletion was successful
     */
    boolean deleteById(int id);
    
    /**
     * Updates the stock of a book.
     * 
     * @param bookId the book ID
     * @param newStock the new stock amount
     * @return true if update was successful
     */
    boolean updateStock(int bookId, int newStock);
    
    /**
     * Checks if a book exists by ISBN.
     * 
     * @param isbn the ISBN to check
     * @return true if book exists
     */
    boolean existsByIsbn(String isbn);
    
    /**
     * Counts the total number of books.
     * 
     * @return the total count of books
     */
    long count();
}

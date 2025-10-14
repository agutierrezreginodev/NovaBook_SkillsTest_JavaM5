/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.service;

import com.codeup.novabook.domain.Book;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Book business logic operations.
 * Defines the contract for Book service operations.
 * 
 * @author Adrián Gutiérrez
 */
public interface BookService {
    
    /**
     * Adds a new book to the library.
     * 
     * @param book the book to add
     * @return the added book
     * @throws IllegalArgumentException if validation fails
     */
    Book addBook(Book book);
    
    /**
     * Finds a book by ID.
     * 
     * @param id the book ID
     * @return Optional containing the book if found
     */
    Optional<Book> findBookById(int id);
    
    /**
     * Finds a book by ISBN.
     * 
     * @param isbn the book ISBN
     * @return Optional containing the book if found
     */
    Optional<Book> findBookByIsbn(String isbn);
    
    /**
     * Gets all books.
     * 
     * @return list of all books
     */
    List<Book> getAllBooks();
    
    /**
     * Searches books by title.
     * 
     * @param title the title to search for
     * @return list of books matching the title
     */
    List<Book> searchBooksByTitle(String title);
    
    /**
     * Searches books by author.
     * 
     * @param author the author to search for
     * @return list of books by the author
     */
    List<Book> searchBooksByAuthor(String author);
    
    /**
     * Gets books with available stock.
     * 
     * @param minStock the minimum stock amount (default: 1)
     * @return list of books with sufficient stock
     */
    List<Book> getAvailableBooks(int minStock);
    
    /**
     * Updates book information.
     * 
     * @param book the book to update
     * @return the updated book
     * @throws IllegalArgumentException if book is invalid
     */
    Book updateBook(Book book);
    
    /**
     * Removes a book from the library.
     * 
     * @param id the book ID to remove
     * @return true if removal was successful
     */
    boolean removeBook(int id);
    
    /**
     * Updates book stock.
     * 
     * @param bookId the book ID
     * @param newStock the new stock amount
     * @return true if update was successful
     * @throws IllegalArgumentException if stock is negative
     */
    boolean updateBookStock(int bookId, int newStock);
    
    /**
     * Decreases book stock by one (for lending).
     * 
     * @param bookId the book ID
     * @return true if stock was decreased successfully
     */
    boolean decreaseStock(int bookId);
    
    /**
     * Increases book stock by one (for returns).
     * 
     * @param bookId the book ID
     * @return true if stock was increased successfully
     */
    boolean increaseStock(int bookId);
    
    /**
     * Validates ISBN format.
     * 
     * @param isbn the ISBN to validate
     * @return true if ISBN format is valid
     */
    boolean isValidIsbn(String isbn);
    
    /**
     * Checks if a book is available for lending.
     * 
     * @param bookId the book ID
     * @return true if book is available
     */
    boolean isBookAvailable(int bookId);
    
    /**
     * Gets book count.
     * 
     * @return the total number of books
     */
    long getBookCount();
}

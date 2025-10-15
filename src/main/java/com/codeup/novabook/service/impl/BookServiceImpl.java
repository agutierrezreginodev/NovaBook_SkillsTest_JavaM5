/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.exceptions.book.DuplicateISBNException;
import com.codeup.novabook.exceptions.book.InvalidStockException;
import com.codeup.novabook.repository.BookRepository;
import com.codeup.novabook.repository.jdbc.BookRepositoryJDBC;
import com.codeup.novabook.service.BookService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service implementation for Book business logic operations.
 * Handles book management, stock control, and availability.
 * 
 * @author Adrián Gutiérrez
 */
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;
    private static final Pattern ISBN_PATTERN = Pattern.compile(
        "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
    );
    
    /**
     * Constructor that initializes the book repository.
     */
    public BookServiceImpl() {
        this.bookRepository = new BookRepositoryJDBC();
    }
    
    private static final Logger LOGGER = Logger.getLogger(BookServiceImpl.class.getName());
    
    public Book addBook(Book book) {
        try {
            validateBook(book);
            
            if (bookRepository.existsByIsbn(book.getIsbn())) {
                throw new DuplicateISBNException(book.getIsbn());
            }
            
            if (book.getStock() < 0) {
                throw InvalidStockException.negativeStock(book.getStock());
            }
            
            // Set timestamps
            Instant now = Instant.now();
            book.setCreatedAt(now);
            book.setUpdatedAt(now);
            
            Book savedBook = bookRepository.save(book);
            LOGGER.log(Level.INFO, "Book added successfully: {0}", book.getIsbn());
            return savedBook;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding book: " + e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public Optional<Book> findBookById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        return bookRepository.findById(id);
    }
    
    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        return bookRepository.findByIsbn(isbn.trim());
    }
    
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    @Override
    public List<Book> searchBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return bookRepository.findByTitleContaining(title.trim());
    }
    
    @Override
    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        return bookRepository.findByAuthorContaining(author.trim());
    }
    
    @Override
    public List<Book> getAvailableBooks(int minStock) {
        if (minStock < 0) {
            throw new IllegalArgumentException("Minimum stock cannot be negative");
        }
        return bookRepository.findByStockGreaterThan(minStock);
    }
    
    @Override
    public Book updateBook(Book book) {
        validateBook(book);
        
        if (book.getId() <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        
        // Check if book exists
        if (!bookRepository.findById(book.getId()).isPresent()) {
            throw new IllegalArgumentException("Book with ID " + book.getId() + " does not exist");
        }
        
        // Check ISBN uniqueness (excluding current book)
        Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn());
        if (existingBook.isPresent() && existingBook.get().getId() != book.getId()) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        
        book.setUpdatedAt(Instant.now());
        return bookRepository.update(book);
    }
    
    @Override
    public boolean removeBook(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        return bookRepository.deleteById(id);
    }
    
    @Override
    public boolean updateBookStock(int bookId, int newStock) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            return bookRepository.updateStock(bookId, newStock);
        }
        
        return false;
    }
    
    @Override
    public boolean decreaseStock(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (book.getStock() > 0) {
                return bookRepository.updateStock(bookId, book.getStock() - 1);
            }
        }
        
        return false;
    }
    
    @Override
    public boolean increaseStock(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            return bookRepository.updateStock(bookId, book.getStock() + 1);
        }
        
        return false;
    }
    
    @Override
    public boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        return ISBN_PATTERN.matcher(isbn.trim()).matches();
    }
    
    @Override
    public boolean isBookAvailable(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        return bookOpt.isPresent() && bookOpt.get().getStock() > 0;
    }
    
    @Override
    public long getBookCount() {
        return bookRepository.count();
    }
    
    /**
     * Validates book data.
     * 
     * @param book the book to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("Book ISBN cannot be null or empty");
        }
        
        if (!isValidIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
        
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be null or empty");
        }
        
        if (book.getStock() < 0) {
            throw new IllegalArgumentException("Book stock cannot be negative");
        }
    }
}

package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.exceptions.book.DuplicateISBNException;
import com.codeup.novabook.exceptions.book.InvalidStockException;
import com.codeup.novabook.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        Instant now = Instant.now();
        testBook = new Book(1, "978-0-13-468599-1", "Test Book", "Test Author", 5, now, now);
    }

    @Test
    void addBook_WithValidBook_ShouldSaveAndReturnBook() {
        // Arrange
        when(bookRepository.existsByIsbn(testBook.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // Act
        Book savedBook = bookService.addBook(testBook);

        // Assert
        assertNotNull(savedBook);
        assertEquals(testBook.getIsbn(), savedBook.getIsbn());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void addBook_WithDuplicateISBN_ShouldThrowDuplicateISBNException() {
        // Arrange
        when(bookRepository.existsByIsbn(testBook.getIsbn())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateISBNException.class, () -> bookService.addBook(testBook));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void addBook_WithNegativeStock_ShouldThrowInvalidStockException() {
        // Arrange
        testBook.setStock(-1);

        // Act & Assert
        assertThrows(InvalidStockException.class, () -> bookService.addBook(testBook));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void findBookById_WithValidId_ShouldReturnBook() {
        // Arrange
        when(bookRepository.findById(1)).thenReturn(Optional.of(testBook));

        // Act
        Optional<Book> result = bookService.findBookById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testBook.getId(), result.get().getId());
    }

    @Test
    void findBookById_WithInvalidId_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.findBookById(0));
        verify(bookRepository, never()).findById(anyInt());
    }

    @Test
    void findBookByIsbn_WithValidIsbn_ShouldReturnBook() {
        // Arrange
        when(bookRepository.findByIsbn(testBook.getIsbn())).thenReturn(Optional.of(testBook));

        // Act
        Optional<Book> result = bookService.findBookByIsbn(testBook.getIsbn());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testBook.getIsbn(), result.get().getIsbn());
    }

    @Test
    void findBookByIsbn_WithNullIsbn_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.findBookByIsbn(null));
        verify(bookRepository, never()).findByIsbn(any());
    }

    @Test
    void searchBooksByTitle_WithValidTitle_ShouldReturnMatchingBooks() {
        // Arrange
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByTitleContaining("Test")).thenReturn(expectedBooks);

        // Act
        List<Book> result = bookService.searchBooksByTitle("Test");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(expectedBooks.size(), result.size());
        assertEquals(expectedBooks.get(0).getTitle(), result.get(0).getTitle());
    }

    @Test
    void searchBooksByTitle_WithEmptyTitle_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.searchBooksByTitle(""));
        verify(bookRepository, never()).findByTitleContaining(any());
    }
}
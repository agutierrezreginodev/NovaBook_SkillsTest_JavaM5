package com.codeup.novabook.ui.components;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.exceptions.book.DuplicateISBNException;
import com.codeup.novabook.exceptions.book.InvalidStockException;
import com.codeup.novabook.service.BookService;
import com.codeup.novabook.service.impl.BookServiceImpl;
import com.codeup.novabook.utils.ErrorHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Instant;

public class BookPanel extends JPanel {
    private final BookService bookService;
    private final BookFormPanel formPanel;
    private final BookTablePanel tablePanel;
    
    public BookPanel() {
        this.bookService = new BookServiceImpl();
        this.formPanel = new BookFormPanel();
        this.tablePanel = new BookTablePanel();
        
        initComponents();
        loadBooks();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add form panel at the top
        formPanel.addActionListener(this::handleAddBook);
        add(formPanel, BorderLayout.NORTH);
        
        // Add table panel in the center
        add(tablePanel, BorderLayout.CENTER);
    }
    
    private void handleAddBook(ActionEvent evt) {
        try {
            String isbn = formPanel.getIsbn();
            String title = formPanel.getTitle();
            String author = formPanel.getAuthor();
            int stock = Integer.parseInt(formPanel.getStock());
            
            // Create book with current timestamp
            Instant now = Instant.now();
            Book book = new Book(0, isbn, title, author, stock, now, now);
            
            bookService.addBook(book);
            
            formPanel.clearForm();
            loadBooks(); // Refresh table
            JOptionPane.showMessageDialog(this, 
                "Libro agregado exitosamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException ex) {
            ErrorHandler.handleError(
                "Error de formato",
                "El stock debe ser un número válido",
                ex
            );
        } catch (DuplicateISBNException ex) {
            ErrorHandler.handleError(
                "Error al agregar libro",
                "Ya existe un libro con ese ISBN",
                ex
            );
        } catch (InvalidStockException ex) {
            ErrorHandler.handleError(
                "Error de stock",
                "El stock no puede ser negativo",
                ex
            );
        } catch (Exception ex) {
            ErrorHandler.handleError(
                "Error inesperado",
                "Ha ocurrido un error al agregar el libro: " + ex.getMessage(),
                ex
            );
        }
    }
    
    private void loadBooks() {
        try {
            java.util.List<Book> books = bookService.getAllBooks();
            tablePanel.updateBooks(books);
        } catch (Exception e) {
            ErrorHandler.handleError(
                "Error al cargar libros",
                "No se pudieron cargar los libros: " + e.getMessage(),
                e
            );
        }
    }
}
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
    private final JButton btnEdit;
    private final JButton btnDelete;
    
    public BookPanel() {
        this.bookService = new BookServiceImpl();
        this.formPanel = new BookFormPanel();
        this.tablePanel = new BookTablePanel();
        this.btnEdit = new JButton("Editar");
        this.btnDelete = new JButton("Eliminar");
        
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

        // Add edit/delete buttons below the table
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(btnEdit);
        actions.add(btnDelete);
        add(actions, BorderLayout.SOUTH);

        // Wire button actions
        btnEdit.addActionListener(this::handleEditBook);
        btnDelete.addActionListener(this::handleDeleteBook);

        // When a row is selected, populate the form for editing
        tablePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Book selected = tablePanel.getSelectedBook();
                if (selected != null) {
                    formPanel.setFormValues(selected.getIsbn(), selected.getTitle(), selected.getAuthor(), selected.getStock());
                    formPanel.setButtonText("Actualizar Libro");
                }
            }
        });
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

    private void handleEditBook(ActionEvent evt) {
        try {
            int selectedId = tablePanel.getSelectedBookId();
            if (selectedId <= 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un libro para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String isbn = formPanel.getIsbn();
            String title = formPanel.getTitle();
            String author = formPanel.getAuthor();
            int stock = Integer.parseInt(formPanel.getStock());

            // Build book with selected ID
            Book book = new Book(selectedId, isbn, title, author, stock, null, Instant.now());

            bookService.updateBook(book);
            formPanel.clearForm();
            formPanel.setButtonText("Agregar Libro");
            loadBooks();
            JOptionPane.showMessageDialog(this, "Libro actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            ErrorHandler.handleError("Error de formato", "El stock debe ser un número válido", ex);
        } catch (Exception ex) {
            ErrorHandler.handleError("Error al actualizar", "No se pudo actualizar el libro: " + ex.getMessage(), ex);
        }
    }

    private void handleDeleteBook(ActionEvent evt) {
        try {
            int selectedId = tablePanel.getSelectedBookId();
            if (selectedId <= 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar el libro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = bookService.removeBook(selectedId);
                if (deleted) {
                    loadBooks();
                    JOptionPane.showMessageDialog(this, "Libro eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el libro", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ErrorHandler.handleError("Error al eliminar", "No se pudo eliminar el libro: " + ex.getMessage(), ex);
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
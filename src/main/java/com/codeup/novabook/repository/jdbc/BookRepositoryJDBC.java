/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.repository.jdbc;

import com.codeup.novabook.connection.ConnectionFactory;
import com.codeup.novabook.domain.Book;
import com.codeup.novabook.exceptions.db.DatabaseException;
import com.codeup.novabook.repository.BookRepository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of BookRepository.
 * Provides database operations for Book entities using JDBC.
 * 
 * @author Adrián Gutiérrez
 */
public class BookRepositoryJDBC implements BookRepository {
    
    private final ConnectionFactory connectionFactory;
    
    /**
     * Constructor that initializes the connection factory.
     */
    public BookRepositoryJDBC() {
        this.connectionFactory = ConnectionFactory.getInstance();
    }
    
    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO book (isbn, title, author, stock, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setInt(4, book.getStock());
            stmt.setTimestamp(5, Timestamp.from(book.getCreatedAt()));
            stmt.setTimestamp(6, Timestamp.from(book.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating book failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Creating book failed, no ID obtained.");
                }
            }
            
            return book;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving book", e);
        }
    }
    
    @Override
    public Optional<Book> findById(int id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Error finding book by ID", e);
        }
    }
    
    @Override
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "SELECT * FROM book WHERE isbn = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Error finding book by ISBN", e);
        }
    }
    
    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
            return books;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all books", e);
        }
    }
    
    @Override
    public List<Book> findByTitleContaining(String title) {
        String sql = "SELECT * FROM book WHERE title LIKE ? ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + title + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
            
            return books;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding books by title", e);
        }
    }
    
    @Override
    public List<Book> findByAuthorContaining(String author) {
        String sql = "SELECT * FROM book WHERE author LIKE ? ORDER BY author";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + author + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
            
            return books;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding books by author", e);
        }
    }
    
    @Override
    public List<Book> findByStockGreaterThan(int minStock) {
        String sql = "SELECT * FROM book WHERE stock > ? ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, minStock);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
            
            return books;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding books by stock", e);
        }
    }
    
    @Override
    public Book update(Book book) {
        String sql = "UPDATE book SET isbn = ?, title = ?, author = ?, stock = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setInt(4, book.getStock());
            stmt.setTimestamp(5, Timestamp.from(book.getUpdatedAt()));
            stmt.setInt(6, book.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Updating book failed, no rows affected.");
            }
            
            return book;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating book", e);
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM book WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting book", e);
        }
    }
    
    @Override
    public boolean updateStock(int bookId, int newStock) {
        String sql = "UPDATE book SET stock = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newStock);
            stmt.setTimestamp(2, Timestamp.from(Instant.now()));
            stmt.setInt(3, bookId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating book stock", e);
        }
    }
    
    @Override
    public boolean existsByIsbn(String isbn) {
        String sql = "SELECT COUNT(*) FROM book WHERE isbn = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Error checking if book exists by ISBN", e);
        }
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM book";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting books", e);
        }
    }
    
    /**
     * Maps a ResultSet row to a Book object.
     * 
     * @param rs the ResultSet
     * @return the mapped Book object
     * @throws SQLException if mapping fails
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("id"),
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getInt("stock"),
            rs.getTimestamp("created_at").toInstant(),
            rs.getTimestamp("updated_at").toInstant()
        );
    }
}

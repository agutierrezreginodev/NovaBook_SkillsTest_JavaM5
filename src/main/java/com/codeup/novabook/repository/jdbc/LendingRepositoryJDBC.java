/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.repository.jdbc;

import com.codeup.novabook.connection.ConnectionFactory;
import com.codeup.novabook.domain.Lending;
import com.codeup.novabook.exceptions.db.DatabaseException;
import com.codeup.novabook.repository.LendingRepository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of LendingRepository.
 * Provides database operations for Lending entities using JDBC.
 * 
 * @author Adrián Gutiérrez
 */
public class LendingRepositoryJDBC implements LendingRepository {
    
    private final ConnectionFactory connectionFactory;
    
    /**
     * Constructor that initializes the connection factory.
     */
    public LendingRepositoryJDBC() {
        this.connectionFactory = ConnectionFactory.getInstance();
    }
    
    @Override
    public Lending save(Lending lending) {
        String sql = "INSERT INTO lending (member_id, book_id, lending_date, due_date, returned, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, lending.getMemberId());
            stmt.setInt(2, lending.getBookId());
            stmt.setTimestamp(3, Timestamp.from(lending.getLendingDate()));
            stmt.setTimestamp(4, Timestamp.from(lending.getDueDate()));
            stmt.setBoolean(5, lending.isReturned());
            stmt.setTimestamp(6, Timestamp.from(lending.getCreatedAt()));
            stmt.setTimestamp(7, Timestamp.from(lending.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating lending failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    lending.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Creating lending failed, no ID obtained.");
                }
            }
            
            return lending;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving lending", e);
        }
    }
    
    @Override
    public Optional<Lending> findById(int id) {
        String sql = "SELECT * FROM lending WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToLending(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Error finding lending by ID", e);
        }
    }
    
    @Override
    public List<Lending> findAll() {
        String sql = "SELECT * FROM lending ORDER BY lending_date DESC";
        List<Lending> lendings = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lendings.add(mapResultSetToLending(rs));
            }
            
            return lendings;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all lendings", e);
        }
    }
    
    @Override
    public List<Lending> findByMemberId(int memberId) {
        String sql = "SELECT * FROM lending WHERE member_id = ? ORDER BY lending_date DESC";
        List<Lending> lendings = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lendings.add(mapResultSetToLending(rs));
                }
            }
            
            return lendings;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding lendings by member ID", e);
        }
    }
    
    @Override
    public List<Lending> findByBookId(int bookId) {
        String sql = "SELECT * FROM lending WHERE book_id = ? ORDER BY lending_date DESC";
        List<Lending> lendings = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lendings.add(mapResultSetToLending(rs));
                }
            }
            
            return lendings;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding lendings by book ID", e);
        }
    }
    
    @Override
    public List<Lending> findByReturned(boolean returned) {
        String sql = "SELECT * FROM lending WHERE returned = ? ORDER BY lending_date DESC";
        List<Lending> lendings = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, returned);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lendings.add(mapResultSetToLending(rs));
                }
            }
            
            return lendings;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding lendings by return status", e);
        }
    }
    
    @Override
    public List<Lending> findOverdueLendings(Instant currentDate) {
        String sql = "SELECT * FROM lending WHERE due_date < ? AND returned = false ORDER BY due_date";
        List<Lending> lendings = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.from(currentDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lendings.add(mapResultSetToLending(rs));
                }
            }
            
            return lendings;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding overdue lendings", e);
        }
    }
    
    @Override
    public List<Lending> findByDueDateBetween(Instant startDate, Instant endDate) {
        String sql = "SELECT * FROM lending WHERE due_date BETWEEN ? AND ? ORDER BY due_date";
        List<Lending> lendings = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.from(startDate));
            stmt.setTimestamp(2, Timestamp.from(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lendings.add(mapResultSetToLending(rs));
                }
            }
            
            return lendings;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding lendings by due date range", e);
        }
    }
    
    @Override
    public Lending update(Lending lending) {
        String sql = "UPDATE lending SET member_id = ?, book_id = ?, lending_date = ?, due_date = ?, " +
                    "returned = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, lending.getMemberId());
            stmt.setInt(2, lending.getBookId());
            stmt.setTimestamp(3, Timestamp.from(lending.getLendingDate()));
            stmt.setTimestamp(4, Timestamp.from(lending.getDueDate()));
            stmt.setBoolean(5, lending.isReturned());
            stmt.setTimestamp(6, Timestamp.from(lending.getUpdatedAt()));
            stmt.setInt(7, lending.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Updating lending failed, no rows affected.");
            }
            
            return lending;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating lending", e);
        }
    }
    
    @Override
    public boolean markAsReturned(int lendingId) {
        String sql = "UPDATE lending SET returned = true, updated_at = ? WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.from(Instant.now()));
            stmt.setInt(2, lendingId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error marking lending as returned", e);
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM lending WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting lending", e);
        }
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM lending";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting lendings", e);
        }
    }
    
    @Override
    public long countActiveLendings() {
        String sql = "SELECT COUNT(*) FROM lending WHERE returned = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting active lendings", e);
        }
    }
    
    @Override
    public long countByMemberId(int memberId) {
        String sql = "SELECT COUNT(*) FROM lending WHERE member_id = ? AND returned = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting lendings by member ID", e);
        }
    }
    
    /**
     * Maps a ResultSet row to a Lending object.
     * 
     * @param rs the ResultSet
     * @return the mapped Lending object
     * @throws SQLException if mapping fails
     */
    private Lending mapResultSetToLending(ResultSet rs) throws SQLException {
        return new Lending(
            rs.getInt("id"),
            rs.getInt("member_id"),
            rs.getInt("book_id"),
            rs.getTimestamp("lending_date").toInstant(),
            rs.getTimestamp("due_date").toInstant(),
            rs.getBoolean("returned"),
            rs.getTimestamp("created_at").toInstant(),
            rs.getTimestamp("updated_at").toInstant()
        );
    }
}

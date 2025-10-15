/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.repository.jdbc;

import com.codeup.novabook.connection.ConnectionFactory;
import com.codeup.novabook.domain.User;
import com.codeup.novabook.exceptions.db.DatabaseException;
import com.codeup.novabook.repository.UserRepository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of UserRepository.
 * Provides database operations for User entities using JDBC.
 * 
 * @author Adrián Gutiérrez
 */
public class UserRepositoryJDBC implements UserRepository {
    
    private final ConnectionFactory connectionFactory;
    
    /**
     * Constructor that initializes the connection factory.
     */
    public UserRepositoryJDBC() {
        this.connectionFactory = ConnectionFactory.getInstance();
    }
    
    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (name, email, password, phone, role, access_level, active, deleted, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getAccessLevel());
            stmt.setBoolean(7, user.isActive());
            stmt.setBoolean(8, user.isDeleted());
            stmt.setTimestamp(9, Timestamp.from(user.getCreatedAt()));
            stmt.setTimestamp(10, Timestamp.from(user.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Creating user failed, no ID obtained.");
                }
            }
            
            return user;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving user", e);
        }
    }
    
    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ? AND deleted = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by ID", e);
        }
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND deleted = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by email", e);
        }
    }
    
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users WHERE deleted = false ORDER BY name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
            return users;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all users", e);
        }
    }
    
    @Override
    public List<User> findByActive(boolean active) {
        String sql = "SELECT * FROM users WHERE active = ? AND deleted = false ORDER BY name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, active);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
            return users;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding users by active status", e);
        }
    }
    
    @Override
    public User update(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, role = ?, " +
                    "access_level = ?, active = ?, updated_at = ? WHERE id = ? AND deleted = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getAccessLevel());
            stmt.setBoolean(7, user.isActive());
            stmt.setTimestamp(8, Timestamp.from(user.getUpdatedAt()));
            stmt.setInt(9, user.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Updating user failed, no rows affected.");
            }
            
            return user;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user", e);
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        String sql = "UPDATE users SET deleted = true, updated_at = ? WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.from(Instant.now()));
            stmt.setInt(2, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user", e);
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND deleted = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Error checking if user exists by email", e);
        }
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM users WHERE deleted = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting users", e);
        }
    }
    
    /**
     * Maps a ResultSet row to a User object.
     * 
     * @param rs the ResultSet
     * @return the mapped User object
     * @throws SQLException if mapping fails
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            rs.getString("role"),
            rs.getString("access_level"),
            rs.getBoolean("active"),
            rs.getBoolean("deleted"),
            rs.getTimestamp("created_at").toInstant(),
            rs.getTimestamp("updated_at").toInstant()
        );
    }
}

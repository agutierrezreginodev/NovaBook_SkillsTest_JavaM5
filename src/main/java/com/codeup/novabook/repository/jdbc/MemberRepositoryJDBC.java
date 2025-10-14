/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.repository.jdbc;

import com.codeup.novabook.connection.ConnectionFactory;
import com.codeup.novabook.domain.Member;
import com.codeup.novabook.exceptions.DatabaseException;
import com.codeup.novabook.repository.MemberRepository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of MemberRepository.
 * Provides database operations for Member entities using JDBC.
 * 
 * @author Adrián Gutiérrez
 */
public class MemberRepositoryJDBC implements MemberRepository {
    
    private final ConnectionFactory connectionFactory;
    
    /**
     * Constructor that initializes the connection factory.
     */
    public MemberRepositoryJDBC() {
        this.connectionFactory = ConnectionFactory.getInstance();
    }
    
    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO member (name, active, deleted, role, access_level, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, member.getName());
            stmt.setBoolean(2, member.isActive());
            stmt.setBoolean(3, member.isDeleted());
            stmt.setString(4, member.getRole());
            stmt.setString(5, member.getAccessLevel());
            stmt.setTimestamp(6, Timestamp.from(member.getCreatedAt()));
            stmt.setTimestamp(7, Timestamp.from(member.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating member failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    member.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Creating member failed, no ID obtained.");
                }
            }
            
            return member;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving member", e);
        }
    }
    
    @Override
    public Optional<Member> findById(int id) {
        String sql = "SELECT * FROM member WHERE id = ? AND deleted = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMember(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Error finding member by ID", e);
        }
    }
    
    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member WHERE deleted = false ORDER BY name";
        List<Member> members = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
            
            return members;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding all members", e);
        }
    }
    
    @Override
    public List<Member> findByNameContaining(String name) {
        String sql = "SELECT * FROM member WHERE name LIKE ? AND deleted = false ORDER BY name";
        List<Member> members = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToMember(rs));
                }
            }
            
            return members;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding members by name", e);
        }
    }
    
    @Override
    public List<Member> findByRole(String role) {
        String sql = "SELECT * FROM member WHERE role = ? AND deleted = false ORDER BY name";
        List<Member> members = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToMember(rs));
                }
            }
            
            return members;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding members by role", e);
        }
    }
    
    @Override
    public List<Member> findByActive(boolean active) {
        String sql = "SELECT * FROM member WHERE active = ? AND deleted = false ORDER BY name";
        List<Member> members = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, active);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToMember(rs));
                }
            }
            
            return members;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding members by active status", e);
        }
    }
    
    @Override
    public List<Member> findByDeleted(boolean deleted) {
        String sql = "SELECT * FROM member WHERE deleted = ? ORDER BY name";
        List<Member> members = new ArrayList<>();
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, deleted);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToMember(rs));
                }
            }
            
            return members;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding members by deleted status", e);
        }
    }
    
    @Override
    public Member update(Member member) {
        String sql = "UPDATE member SET name = ?, active = ?, role = ?, access_level = ?, updated_at = ? " +
                    "WHERE id = ? AND deleted = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, member.getName());
            stmt.setBoolean(2, member.isActive());
            stmt.setString(3, member.getRole());
            stmt.setString(4, member.getAccessLevel());
            stmt.setTimestamp(5, Timestamp.from(member.getUpdatedAt()));
            stmt.setInt(6, member.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Updating member failed, no rows affected.");
            }
            
            return member;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating member", e);
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        String sql = "UPDATE member SET deleted = true, updated_at = ? WHERE id = ?";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.from(Instant.now()));
            stmt.setInt(2, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting member", e);
        }
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM member WHERE deleted = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting members", e);
        }
    }
    
    @Override
    public long countByRole(String role) {
        String sql = "SELECT COUNT(*) FROM member WHERE role = ? AND deleted = false";
        
        try (Connection conn = connectionFactory.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting members by role", e);
        }
    }
    
    /**
     * Maps a ResultSet row to a Member object.
     * 
     * @param rs the ResultSet
     * @return the mapped Member object
     * @throws SQLException if mapping fails
     */
    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        return new Member(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getBoolean("active"),
            rs.getBoolean("deleted"),
            rs.getString("role"),
            rs.getString("access_level"),
            rs.getTimestamp("created_at").toInstant(),
            rs.getTimestamp("updated_at").toInstant()
        );
    }
}

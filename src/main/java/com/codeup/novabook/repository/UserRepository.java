/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.repository;

import com.codeup.novabook.domain.User;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Defines the contract for User data access operations.
 * 
 * @author Adrián Gutiérrez
 */
public interface UserRepository {
    
    /**
     * Saves a user to the database.
     * 
     * @param user the user to save
     * @return the saved user with generated ID
     */
    User save(User user);
    
    /**
     * Finds a user by ID.
     * 
     * @param id the user ID
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findById(int id);
    
    /**
     * Finds a user by email.
     * 
     * @param email the user email
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Finds all users.
     * 
     * @return list of all users
     */
    List<User> findAll();
    
    /**
     * Finds all active users.
     * 
     * @return list of active users
     */
    List<User> findByActive(boolean active);
    
    /**
     * Updates a user.
     * 
     * @param user the user to update
     * @return the updated user
     */
    User update(User user);
    
    /**
     * Deletes a user by ID (soft delete).
     * 
     * @param id the user ID to delete
     * @return true if deletion was successful
     */
    boolean deleteById(int id);
    
    /**
     * Checks if a user exists by email.
     * 
     * @param email the email to check
     * @return true if user exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Counts the total number of users.
     * 
     * @return the total count of users
     */
    long count();
}

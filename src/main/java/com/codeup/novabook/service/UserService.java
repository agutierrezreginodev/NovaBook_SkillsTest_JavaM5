/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.service;

import com.codeup.novabook.domain.User;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for User business logic operations.
 * Defines the contract for User service operations.
 * 
 * @author Adrián Gutiérrez
 */
public interface UserService {
    
    /**
     * Registers a new user with validation.
     * 
     * @param user the user to register
     * @return the registered user
     * @throws IllegalArgumentException if validation fails
     */
    User registerUser(User user);
    
    /**
     * Authenticates a user by email and password.
     * 
     * @param email the user's email
     * @param password the user's password
     * @return Optional containing the user if authentication succeeds
     */
    Optional<User> authenticateUser(String email, String password);
    
    /**
     * Finds a user by ID.
     * 
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> findUserById(int id);
    
    /**
     * Finds a user by email.
     * 
     * @param email the user's email
     * @return Optional containing the user if found
     */
    Optional<User> findUserByEmail(String email);
    
    /**
     * Gets all users.
     * 
     * @return list of all users
     */
    List<User> getAllUsers();
    
    /**
     * Gets all active users.
     * 
     * @return list of active users
     */
    List<User> getActiveUsers();
    
    /**
     * Updates user information.
     * 
     * @param user the user to update
     * @return the updated user
     * @throws IllegalArgumentException if user is invalid
     */
    User updateUser(User user);
    
    /**
     * Deactivates a user (soft delete).
     * 
     * @param id the user ID to deactivate
     * @return true if deactivation was successful
     */
    boolean deactivateUser(int id);
    
    /**
     * Activates a user.
     * 
     * @param id the user ID to activate
     * @return true if activation was successful
     */
    boolean activateUser(int id);
    
    /**
     * Changes user password.
     * 
     * @param userId the user ID
     * @param oldPassword the current password
     * @param newPassword the new password
     * @return true if password change was successful
     */
    boolean changePassword(int userId, String oldPassword, String newPassword);
    
    /**
     * Changes user role (admin only).
     * 
     * @param userId the user ID
     * @param newRole the new role
     * @return true if role change was successful
     */
    boolean changeUserRole(int userId, String newRole);
    
    /**
     * Validates user email format.
     * 
     * @param email the email to validate
     * @return true if email format is valid
     */
    boolean isValidEmail(String email);
    
    /**
     * Gets user count.
     * 
     * @return the total number of users
     */
    long getUserCount();
}

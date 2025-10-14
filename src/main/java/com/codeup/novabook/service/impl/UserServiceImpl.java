/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.User;
import com.codeup.novabook.repository.UserRepository;
import com.codeup.novabook.repository.jdbc.UserRepositoryJDBC;
import com.codeup.novabook.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import com.codeup.novabook.utils.PasswordUtils;

/**
 * Service implementation for User business logic operations.
 * Handles user registration, authentication, and management.
 * 
 * @author Adrián Gutiérrez
 */
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    /**
     * Constructor that initializes the user repository.
     */
    public UserServiceImpl() {
        this.userRepository = new UserRepositoryJDBC();
    }
    
    @Override
    public User registerUser(User user) {
        validateUser(user);
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }
        
        // Hash the password before saving
        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        
        // Set timestamps
        Instant now = Instant.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        
        return userRepository.save(user);
    }
    
    @Override
    public Optional<User> authenticateUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        Optional<User> userOpt = userRepository.findByEmail(email.trim());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isActive() && !user.isDeleted() && 
                PasswordUtils.verifyPassword(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findUserById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        return userRepository.findById(id);
    }
    
    @Override
    public Optional<User> findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return userRepository.findByEmail(email.trim());
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public List<User> getActiveUsers() {
        return userRepository.findByActive(true);
    }
    
    @Override
    public User updateUser(User user) {
        validateUser(user);
        
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        
        // Check if user exists
        if (!userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalArgumentException("User with ID " + user.getId() + " does not exist");
        }
        
        // Check email uniqueness (excluding current user)
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent() && existingUser.get().getId() != user.getId()) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }
        
        user.setUpdatedAt(Instant.now());
        return userRepository.update(user);
    }
    
    @Override
    public boolean deactivateUser(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            user.setUpdatedAt(Instant.now());
            userRepository.update(user);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean activateUser(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(true);
            user.setUpdatedAt(Instant.now());
            userRepository.update(user);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Old password cannot be null or empty");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                user.setUpdatedAt(Instant.now());
                userRepository.update(user);
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean changeUserRole(int userId, String newRole) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (newRole == null || newRole.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        
        if (!newRole.equals("USER") && !newRole.equals("ADMIN") && !newRole.equals("MEMBER")) {
            throw new IllegalArgumentException("Role must be either 'USER', 'ADMIN', or 'MEMBER'");
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(newRole);
            user.setUpdatedAt(Instant.now());
            userRepository.update(user);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    @Override
    public long getUserCount() {
        return userRepository.count();
    }
    
    /**
     * Validates user data.
     * 
     * @param user the user to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("User password cannot be null or empty");
        }
        
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("User phone cannot be null or empty");
        }
        
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("User role cannot be null or empty");
        }
        
        if (user.getAccessLevel() == null || user.getAccessLevel().trim().isEmpty()) {
            throw new IllegalArgumentException("User access level cannot be null or empty");
        }
        
        // Validate role values
        if (!user.getRole().equals("USER") && !user.getRole().equals("ADMIN") && !user.getRole().equals("MEMBER")) {
            throw new IllegalArgumentException("User role must be either 'USER', 'ADMIN', or 'MEMBER'");
        }
        
        // Validate access level values
        if (!user.getAccessLevel().equals("READ_ONLY") && 
            !user.getAccessLevel().equals("READ_WRITE") && 
            !user.getAccessLevel().equals("MANAGE")) {
            throw new IllegalArgumentException("User access level must be 'READ_ONLY', 'READ_WRITE', or 'MANAGE'");
        }
    }
}

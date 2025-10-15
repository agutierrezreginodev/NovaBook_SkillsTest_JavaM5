/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

import java.time.Instant;

/**
 *
 * @author Adrián Gutiérrez
 */
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String accessLevel;
    private boolean active;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Constructor for User
     * <p>
     * Creates a user instance with ID, name, email, password, phone, role, access
     * level, active, deleted, created at, and updated at.
     * </p>
     * 
     * @param id          ID of the user
     * @param name        Name of the user
     * @param email       Email of the user
     * @param password    Password of the user
     * @param phone       Phone of the user
     * @param role        Role of the user
     * @param accessLevel Access level of the user
     * @param active      Active status of the user
     * @param deleted     Deleted status of the user
     * @param createdAt   Created at of the user
     * @param updatedAt   Updated at of the user
     */
    /**
     * Factory method for creating a new user instance that hasn't been saved to the database yet
     */
    public static User createNew(String name, String email, String password, String phone, String role, 
            String accessLevel, boolean active, boolean deleted, Instant createdAt, Instant updatedAt) {
        return new User(-1, name, email, password, phone, role, accessLevel, active, deleted, createdAt, updatedAt);
    }

    public User(int id, String name, String email, String password, String phone, String role, String accessLevel,
            boolean active, boolean deleted, Instant createdAt, Instant updatedAt) {
        // For new unsaved instances, allow id = 0
        if (id >= 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID must be non-negative");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (accessLevel == null || accessLevel.isEmpty()) {
            throw new IllegalArgumentException("Access level cannot be null or empty");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at cannot be null");
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.accessLevel = accessLevel;
        this.active = active;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Getter for the id of the user
     * 
     * @return id of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the name of the user
     * 
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the email of the user
     * 
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for the password of the user
     * 
     * @return password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the phone of the user
     * 
     * @return phone of the user
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Getter for the role of the user
     * 
     * @return role of the user
     */
    public String getRole() {
        return role;
    }

    /**
     * Getter for the access level of the user
     * 
     * @return access level of the user
     */
    public String getAccessLevel() {
        return accessLevel;
    }

    /**
     * Getter for the active status of the user
     * 
     * @return active status of the user
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Getter for the deleted status of the user
     * 
     * @return deleted status of the user
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Getter for the created at of the user
     * 
     * @return created at of the user
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Getter for the updated at of the user
     * 
     * @return updated at of the user
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Setter for the id of the user
     * 
     * @param id id of the user
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID cannot be less than or equal to 0");
        }
        this.id = id;
    }

    /**
     * Setter for the name of the user
     * 
     * @param name name of the user
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    /**
     * Setter for the email of the user
     * 
     * @param email email of the user
     */
    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        this.email = email;
    }

    /**
     * Setter for the password of the user
     * 
     * @param password password of the user
     */
    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }

    /**
     * Setter for the phone of the user
     * 
     * @param phone phone of the user
     */
    public void setPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        this.phone = phone;
    }

    /**
     * Setter for the role of the user
     * 
     * @param role role of the user
     */
    public void setRole(String role) {
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        this.role = role;
    }

    /**
     * Setter for the access level of the user
     * 
     * @param accessLevel access level of the user
     */
    public void setAccessLevel(String accessLevel) {
        if (accessLevel == null || accessLevel.isEmpty()) {
            throw new IllegalArgumentException("Access level cannot be null or empty");
        }
        this.accessLevel = accessLevel;
    }

    /**
     * Setter for the active status of the user
     * 
     * @param active active status of the user
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Setter for the deleted status of the user
     * 
     * @param deleted deleted status of the user
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Setter for the created at of the user
     * 
     * @param createdAt created at of the user
     */
    public void setCreatedAt(Instant createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null");
        }
        this.createdAt = createdAt;
    }

    /**
     * Setter for the updated at of the user
     * 
     * @param updatedAt updated at of the user
     */
    public void setUpdatedAt(Instant updatedAt) {
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at cannot be null");
        }
        this.updatedAt = updatedAt;
    }

    /**
     * toString method for the user
     * 
     * @return string representation of the user
     */
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", phone="
                + phone + ", role=" + role + ", accessLevel=" + accessLevel + ", active=" + active + ", deleted="
                + deleted + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}

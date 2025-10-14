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
public class Member {
    private int id;
    private String name;
    private boolean active;
    private boolean deleted;
    private String role;
    private String accessLevel;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Constructor for Member
     * <p>
     * Creates a member instance with ID, name, active, deleted, role, access level,
     * created at, and updated at.
     * </p>
     * 
     * @param id          ID of the member
     * @param name        Name of the member
     * @param active      Active status of the member
     * @param deleted     Deleted status of the member
     * @param role        Role of the member
     * @param accessLevel Access level of the member
     * @param createdAt   Created at of the member
     * @param updatedAt   Updated at of the member
     */
    /**
     * Factory method for creating a new member instance that hasn't been saved to the database yet
     */
    public static Member createNew(String name, boolean active, boolean deleted, String role, 
            String accessLevel, Instant createdAt, Instant updatedAt) {
        return new Member(-1, name, active, deleted, role, accessLevel, createdAt, updatedAt);
    }

    public Member(int id, String name, boolean active, boolean deleted, String role, String accessLevel,
            Instant createdAt, Instant updatedAt) {
        // For new unsaved instances, allow id = 0
        if (id >= 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID must be non-negative");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
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
        this.active = active;
        this.deleted = deleted;
        this.role = role;
        this.accessLevel = accessLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Getter for the id of the member
     * 
     * @return id of the member
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the name of the member
     * 
     * @return name of the member
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the active status of the member
     * 
     * @return active status of the member
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Getter for the deleted status of the member
     * 
     * @return deleted status of the member
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Getter for the role of the member
     * 
     * @return role of the member
     */
    public String getRole() {
        return role;
    }

    /**
     * Getter for the access level of the member
     * 
     * @return access level of the member
     */
    public String getAccessLevel() {
        return accessLevel;
    }

    /**
     * Getter for the created at of the member
     * 
     * @return created at of the member
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Getter for the updated at of the member
     * 
     * @return updated at of the member
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Setter for the id of the member
     * 
     * @param id id of the member
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for the name of the member
     * 
     * @param name name of the member
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for the active status of the member
     * 
     * @param active active status of the member
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Setter for the deleted status of the member
     * 
     * @param deleted deleted status of the member
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Setter for the role of the member
     * 
     * @param role role of the member
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Setter for the access level of the member
     * 
     * @param accessLevel access level of the member
     */
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Setter for the created at of the member
     * 
     * @param createdAt created at of the member
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Setter for the updated at of the member
     * 
     * @param updatedAt updated at of the member
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * toString method for the member
     * 
     * @return string representation of the member
     */
    @Override
    public String toString() {
        return "Member{" + "id=" + id + ", name=" + name + ", active=" + active + ", deleted=" + deleted + ", role="
                + role + ", accessLevel=" + accessLevel + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}

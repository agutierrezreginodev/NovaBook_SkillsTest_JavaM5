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
public class Lending {

    private int id;
    private int memberId;
    private int bookId;
    private Instant lendingDate;
    private Instant dueDate;
    private boolean returned;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Constructor for Lending
     * <p>
     * Creates a lending instance with ID, member ID, book ID, lending date, due
     * date, returned, created at, and updated at.
     * </p>
     * 
     * @param id          ID of the lending
     * @param memberId    ID of the member
     * @param bookId      ID of the book
     * @param lendingDate Lending date
     * @param dueDate     Due date
     * @param returned    Returned
     * @param createdAt   Created at
     * @param updatedAt   Updated at
     */
    public Lending(int id, int memberId, int bookId, Instant lendingDate, Instant dueDate, boolean returned,
            Instant createdAt, Instant updatedAt) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be less than 0");
        }
        if (memberId <= 0) {
            throw new IllegalArgumentException("Member ID cannot be less than or equal to 0");
        }
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID cannot be less than or equal to 0");
        }
        if (lendingDate == null) {
            throw new IllegalArgumentException("Lending date cannot be null");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at cannot be null");
        }

        this.id = id;
        this.memberId = memberId;
        this.bookId = bookId;
        this.lendingDate = lendingDate;
        this.dueDate = dueDate;
        this.returned = returned;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Getter for the id of the lending
     * 
     * @return id of the lending
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the member id of the lending
     * 
     * @return member id of the lending
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * Getter for the book id of the lending
     * 
     * @return book id of the lending
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Gets the lending date
     * 
     * @return the lending date
     */
    public Instant getLendDate() {
        return lendingDate;
    }

    /**
     * Calculates the number of days this lending is overdue
     * 
     * @return number of days overdue, or 0 if not overdue
     */
    public long getDaysOverdue() {
        if (returned) {
            return 0;
        }
        Instant now = Instant.now();
        if (now.isAfter(dueDate)) {
            return java.time.Duration.between(dueDate, now).toDays();
        }
        return 0;
    }

    /**
     * Getter for the lending date of the lending
     * 
     * @return lending date of the lending
     */
    public Instant getLendingDate() {
        return lendingDate;
    }

    /**
     * Getter for the due date of the lending
     * 
     * @return due date of the lending
     */
    public Instant getDueDate() {
        return dueDate;
    }

    /**
     * Getter for the returned of the lending
     * 
     * @return returned of the lending
     */
    public boolean isReturned() {
        return returned;
    }

    /**
     * Getter for the created at of the lending
     * 
     * @return created at of the lending
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Getter for the updated at of the lending
     * 
     * @return updated at of the lending
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Setter for the id of the lending
     * 
     * @param id id of the lending
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID cannot be less than or equal to 0");
        }
        this.id = id;
    }

    /**
     * Setter for the member id of the lending
     * 
     * @param memberId member id of the lending
     */
    public void setMemberId(int memberId) {
        if (memberId <= 0) {
            throw new IllegalArgumentException("Member ID cannot be less than or equal to 0");
        }
        this.memberId = memberId;
    }

    /**
     * Setter for the book id of the lending
     * 
     * @param bookId book id of the lending
     */
    public void setBookId(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID cannot be less than or equal to 0");
        }
        this.bookId = bookId;
    }

    /**
     * Setter for the lending date of the lending
     * 
     * @param lendingDate lending date of the lending
     */
    public void setLendingDate(Instant lendingDate) {
        if (lendingDate == null) {
            throw new IllegalArgumentException("Lending date cannot be null");
        }
        this.lendingDate = lendingDate;
    }

    /**
     * Setter for the due date of the lending
     * 
     * @param dueDate due date of the lending
     */
    public void setDueDate(Instant dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        this.dueDate = dueDate;
    }

    /**
     * Setter for the returned of the lending
     * 
     * @param returned returned of the lending
     */
    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    /**
     * Setter for the created at of the lending
     * 
     * @param createdAt created at of the lending
     */
    public void setCreatedAt(Instant createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null");
        }
        this.createdAt = createdAt;
    }

    /**
     * Setter for the updated at of the lending
     * 
     * @param updatedAt updated at of the lending
     */
    public void setUpdatedAt(Instant updatedAt) {
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at cannot be null");
        }
        this.updatedAt = updatedAt;
    }

    /**
     * toString method for the lending
     * 
     * @return string representation of the lending
     */
    @Override
    public String toString() {
        return "Lending{" + "id=" + id + ", memberId=" + memberId + ", bookId=" + bookId + ", lendingDate="
                + lendingDate + ", dueDate=" + dueDate + ", returned=" + returned + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + '}';
    }

}

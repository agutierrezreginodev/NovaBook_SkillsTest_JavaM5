/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.repository;

import com.codeup.novabook.domain.Lending;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Lending entity operations.
 * Defines the contract for Lending data access operations.
 * 
 * @author Adrián Gutiérrez
 */
public interface LendingRepository {
    
    /**
     * Saves a lending to the database.
     * 
     * @param lending the lending to save
     * @return the saved lending with generated ID
     */
    Lending save(Lending lending);
    
    /**
     * Finds a lending by ID.
     * 
     * @param id the lending ID
     * @return Optional containing the lending if found, empty otherwise
     */
    Optional<Lending> findById(int id);
    
    /**
     * Finds all lendings.
     * 
     * @return list of all lendings
     */
    List<Lending> findAll();
    
    /**
     * Finds lendings by member ID.
     * 
     * @param memberId the member ID
     * @return list of lendings for the specified member
     */
    List<Lending> findByMemberId(int memberId);
    
    /**
     * Finds lendings by book ID.
     * 
     * @param bookId the book ID
     * @return list of lendings for the specified book
     */
    List<Lending> findByBookId(int bookId);
    
    /**
     * Finds lendings by return status.
     * 
     * @param returned the return status
     * @return list of lendings with the specified return status
     */
    List<Lending> findByReturned(boolean returned);
    
    /**
     * Finds lendings that are overdue.
     * 
     * @param currentDate the current date for comparison
     * @return list of overdue lendings
     */
    List<Lending> findOverdueLendings(Instant currentDate);
    
    /**
     * Finds lendings by due date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of lendings within the date range
     */
    List<Lending> findByDueDateBetween(Instant startDate, Instant endDate);
    
    /**
     * Updates a lending.
     * 
     * @param lending the lending to update
     * @return the updated lending
     */
    Lending update(Lending lending);
    
    /**
     * Marks a lending as returned.
     * 
     * @param lendingId the lending ID to mark as returned
     * @return true if update was successful
     */
    boolean markAsReturned(int lendingId);
    
    /**
     * Deletes a lending by ID.
     * 
     * @param id the lending ID to delete
     * @return true if deletion was successful
     */
    boolean deleteById(int id);
    
    /**
     * Counts the total number of lendings.
     * 
     * @return the total count of lendings
     */
    long count();
    
    /**
     * Counts active lendings (not returned).
     * 
     * @return the count of active lendings
     */
    long countActiveLendings();
    
    /**
     * Counts lendings by member.
     * 
     * @param memberId the member ID
     * @return the count of lendings for the specified member
     */
    long countByMemberId(int memberId);
}

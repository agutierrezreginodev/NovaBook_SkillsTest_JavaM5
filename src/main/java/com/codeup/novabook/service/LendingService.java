/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.service;

import com.codeup.novabook.domain.Lending;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Lending business logic operations.
 * Defines the contract for Lending service operations.
 * 
 * @author Adrián Gutiérrez
 */
public interface LendingService {
    
    /**
     * Creates a new lending transaction.
     * 
     * @param memberId the member ID
     * @param bookId the book ID
     * @param lendingDays the number of days for lending (default: 14)
     * @return the created lending
     * @throws IllegalArgumentException if validation fails
     */
    Lending lendBook(int memberId, int bookId, int lendingDays);
    
    /**
     * Returns a book.
     * 
     * @param lendingId the lending ID
     * @return true if return was successful
     */
    boolean returnBook(int lendingId);
    
    /**
     * Finds a lending by ID.
     * 
     * @param id the lending ID
     * @return Optional containing the lending if found
     */
    Optional<Lending> findLendingById(int id);
    
    /**
     * Gets all lendings.
     * 
     * @return list of all lendings
     */
    List<Lending> getAllLendings();
    
    /**
     * Gets lendings for a specific member.
     * 
     * @param memberId the member ID
     * @return list of lendings for the member
     */
    List<Lending> getLendingsByMember(int memberId);
    
    /**
     * Gets lendings for a specific book.
     * 
     * @param bookId the book ID
     * @return list of lendings for the book
     */
    List<Lending> getLendingsByBook(int bookId);
    
    /**
     * Gets active lendings (not returned).
     * 
     * @return list of active lendings
     */
    List<Lending> getActiveLendings();
    
    /**
     * Gets overdue lendings.
     * 
     * @return list of overdue lendings
     */
    List<Lending> getOverdueLendings();
    
    /**
     * Gets lendings due between specific dates.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of lendings due in the date range
     */
    List<Lending> getLendingsDueBetween(Instant startDate, Instant endDate);
    
    /**
     * Extends a lending period.
     * 
     * @param lendingId the lending ID
     * @param additionalDays additional days to extend
     * @return true if extension was successful
     */
    boolean extendLending(int lendingId, int additionalDays);
    
    /**
     * Checks if a member can borrow more books.
     * 
     * @param memberId the member ID
     * @param maxBooks the maximum number of books allowed (default: 3)
     * @return true if member can borrow more books
     */
    boolean canMemberBorrowMoreBooks(int memberId, int maxBooks);
    
    /**
     * Checks if a book is currently lent out.
     * 
     * @param bookId the book ID
     * @return true if book is currently lent out
     */
    boolean isBookCurrentlyLent(int bookId);
    
    /**
     * Calculates fine for overdue lending.
     * 
     * @param lendingId the lending ID
     * @param dailyFineRate the daily fine rate (default: 1.0)
     * @return the calculated fine amount
     */
    double calculateFine(int lendingId, double dailyFineRate);
    
    /**
     * Gets lending count.
     * 
     * @return the total number of lendings
     */
    long getLendingCount();
    
    /**
     * Gets active lending count.
     * 
     * @return the number of active lendings
     */
    long getActiveLendingCount();
    
    /**
     * Gets lending count for a member.
     * 
     * @param memberId the member ID
     * @return the number of lendings for the member
     */
    long getLendingCountByMember(int memberId);
}

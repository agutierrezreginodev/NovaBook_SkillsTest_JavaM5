/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.repository;

import com.codeup.novabook.domain.Member;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Member entity operations.
 * Defines the contract for Member data access operations.
 * 
 * @author Adrián Gutiérrez
 */
public interface MemberRepository {
    
    /**
     * Saves a member to the database.
     * 
     * @param member the member to save
     * @return the saved member with generated ID
     */
    Member save(Member member);
    
    /**
     * Finds a member by ID.
     * 
     * @param id the member ID
     * @return Optional containing the member if found, empty otherwise
     */
    Optional<Member> findById(int id);
    
    /**
     * Finds all members.
     * 
     * @return list of all members
     */
    List<Member> findAll();
    
    /**
     * Finds members by name containing the given text.
     * 
     * @param name the name text to search for
     * @return list of members matching the name
     */
    List<Member> findByNameContaining(String name);
    
    /**
     * Finds members by role.
     * 
     * @param role the role to search for
     * @return list of members with the specified role
     */
    List<Member> findByRole(String role);
    
    /**
     * Finds all active members.
     * 
     * @param active the active status to search for
     * @return list of members with the specified active status
     */
    List<Member> findByActive(boolean active);
    
    /**
     * Finds all non-deleted members.
     * 
     * @param deleted the deleted status to search for
     * @return list of members with the specified deleted status
     */
    List<Member> findByDeleted(boolean deleted);
    
    /**
     * Updates a member.
     * 
     * @param member the member to update
     * @return the updated member
     */
    Member update(Member member);
    
    /**
     * Deletes a member by ID (soft delete).
     * 
     * @param id the member ID to delete
     * @return true if deletion was successful
     */
    boolean deleteById(int id);
    
    /**
     * Counts the total number of members.
     * 
     * @return the total count of members
     */
    long count();
    
    /**
     * Counts members by role.
     * 
     * @param role the role to count
     * @return the count of members with the specified role
     */
    long countByRole(String role);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.service;

import com.codeup.novabook.domain.Member;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Member business logic operations.
 * Defines the contract for Member service operations.
 * 
 * @author Adrián Gutiérrez
 */
public interface MemberService {
    
    /**
     * Registers a new member.
     * 
     * @param member the member to register
     * @return the registered member
     * @throws IllegalArgumentException if validation fails
     */
    Member registerMember(Member member);
    
    /**
     * Finds a member by ID.
     * 
     * @param id the member ID
     * @return Optional containing the member if found
     */
    Optional<Member> findMemberById(int id);
    
    /**
     * Gets all members.
     * 
     * @return list of all members
     */
    List<Member> getAllMembers();
    
    /**
     * Gets all active members.
     * 
     * @return list of active members
     */
    List<Member> getActiveMembers();
    
    /**
     * Searches members by name.
     * 
     * @param name the name to search for
     * @return list of members matching the name
     */
    List<Member> searchMembersByName(String name);
    
    /**
     * Gets members by role.
     * 
     * @param role the role to filter by
     * @return list of members with the specified role
     */
    List<Member> getMembersByRole(String role);
    
    /**
     * Updates member information.
     * 
     * @param member the member to update
     * @return the updated member
     * @throws IllegalArgumentException if member is invalid
     */
    Member updateMember(Member member);
    
    /**
     * Deactivates a member (soft delete).
     * 
     * @param id the member ID to deactivate
     * @return true if deactivation was successful
     */
    boolean deactivateMember(int id);
    
    /**
     * Activates a member.
     * 
     * @param id the member ID to activate
     * @return true if activation was successful
     */
    boolean activateMember(int id);
    
    /**
     * Upgrades a member to premium.
     * 
     * @param id the member ID
     * @return true if upgrade was successful
     */
    boolean upgradeToPremium(int id);
    
    /**
     * Downgrades a member to regular.
     * 
     * @param id the member ID
     * @return true if downgrade was successful
     */
    boolean downgradeToRegular(int id);
    
    /**
     * Checks if a member is active.
     * 
     * @param id the member ID
     * @return true if member is active
     */
    boolean isMemberActive(int id);
    
    /**
     * Validates member name.
     * 
     * @param name the name to validate
     * @return true if name is valid
     */
    boolean isValidMemberName(String name);
    
    /**
     * Gets member count.
     * 
     * @return the total number of members
     */
    long getMemberCount();
    
    /**
     * Gets member count by role.
     * 
     * @param role the role to count
     * @return the number of members with the specified role
     */
    long getMemberCountByRole(String role);
}

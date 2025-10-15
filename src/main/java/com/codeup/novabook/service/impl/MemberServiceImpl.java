/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.Member;
import com.codeup.novabook.repository.MemberRepository;
import com.codeup.novabook.repository.jdbc.MemberRepositoryJDBC;
import com.codeup.novabook.service.MemberService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Member business logic operations.
 * Handles member registration, management, and role operations.
 * 
 * @author Adrián Gutiérrez
 */
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;
    
    /**
     * Constructor that initializes the member repository.
     */
    public MemberServiceImpl() {
        this.memberRepository = new MemberRepositoryJDBC();
    }
    
    /**
     * Validates a member's data before saving or updating
     * @param member The member to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be null or empty");
        }
        if (member.getRole() == null || member.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Member role cannot be null or empty");
        }
        if (!member.getRole().equals("REGULAR") && !member.getRole().equals("PREMIUM")) {
            throw new IllegalArgumentException("Member role must be either 'REGULAR' or 'PREMIUM'");
        }
        if (member.getAccessLevel() == null || member.getAccessLevel().trim().isEmpty()) {
            throw new IllegalArgumentException("Member access level cannot be null or empty");
        }
        if (!member.getAccessLevel().equals("READ_WRITE")) {
            throw new IllegalArgumentException("Member access level must be 'READ_WRITE'");
        }
    }

    @Override
    public Member registerMember(Member member) {
        validateMember(member);
        
        // Set timestamps
        Instant now = Instant.now();
        member.setCreatedAt(now);
        member.setUpdatedAt(now);
        
        return memberRepository.save(member);
    }
    
    @Override
    public Optional<Member> findMemberById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Member ID must be positive");
        }
        return memberRepository.findById(id);
    }
    
    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
    
    @Override
    public List<Member> getActiveMembers() {
        return memberRepository.findByActive(true);
    }
    
    @Override
    public List<Member> searchMembersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return memberRepository.findByNameContaining(name.trim());
    }
    
    @Override
    public List<Member> getMembersByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        
        if (!role.equals("REGULAR") && !role.equals("PREMIUM")) {
            throw new IllegalArgumentException("Role must be either 'REGULAR' or 'PREMIUM'");
        }
        
        return memberRepository.findByRole(role.trim());
    }
    
    @Override
    public Member updateMember(Member member) {
        validateMember(member);
        
        if (member.getId() <= 0) {
            throw new IllegalArgumentException("Member ID must be positive");
        }
        
        // Check if member exists
        if (!memberRepository.findById(member.getId()).isPresent()) {
            throw new IllegalArgumentException("Member with ID " + member.getId() + " does not exist");
        }
        
        member.setUpdatedAt(Instant.now());
        return memberRepository.update(member);
    }
    
    @Override
    public boolean deactivateMember(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Member ID must be positive");
        }
        
        Optional<Member> memberOpt = memberRepository.findById(id);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.setActive(false);
            member.setUpdatedAt(Instant.now());
            memberRepository.update(member);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean activateMember(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Member ID must be positive");
        }
        
        Optional<Member> memberOpt = memberRepository.findById(id);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.setActive(true);
            member.setUpdatedAt(Instant.now());
            memberRepository.update(member);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean upgradeToPremium(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Member ID must be positive");
        }
        
        Optional<Member> memberOpt = memberRepository.findById(id);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            if (member.getRole().equals("REGULAR")) {
                member.setRole("PREMIUM");
                member.setUpdatedAt(Instant.now());
                memberRepository.update(member);
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean downgradeToRegular(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Member ID must be positive");
        }
        
        Optional<Member> memberOpt = memberRepository.findById(id);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            if (member.getRole().equals("PREMIUM")) {
                member.setRole("REGULAR");
                member.setUpdatedAt(Instant.now());
                memberRepository.update(member);
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean isMemberActive(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Member ID must be positive");
        }
        
        Optional<Member> memberOpt = memberRepository.findById(id);
        return memberOpt.isPresent() && memberOpt.get().isActive() && !memberOpt.get().isDeleted();
    }
    
    @Override
    public boolean isValidMemberName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        String trimmedName = name.trim();
        return trimmedName.length() >= 2 && trimmedName.length() <= 100;
    }
    
    @Override
    public long getMemberCount() {
        return memberRepository.count();
    }
    
    @Override
    public long getMemberCountByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        
        if (!role.equals("REGULAR") && !role.equals("PREMIUM")) {
            throw new IllegalArgumentException("Role must be either 'REGULAR' or 'PREMIUM'");
        }
        
        return memberRepository.countByRole(role.trim());
    }
    
    // Removed duplicate validateMember method to resolve the issue.
}

package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.Member;
import com.codeup.novabook.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        Instant now = Instant.now();
        // use accessLevel READ_WRITE to satisfy MemberServiceImpl.validateMember
        testMember = new Member(1, "Test Member", true, false, "REGULAR", "READ_WRITE", now, now);
    }

    @Test
    void registerMember_WithValidMember_ShouldSaveAndReturnMember() {
        // Arrange
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // Act
        Member result = memberService.registerMember(testMember);

        // Assert
        assertNotNull(result);
        assertEquals(testMember.getName(), result.getName());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void registerMember_WithNullName_ShouldThrowIllegalArgumentException() {
        // Arrange
        // The Member constructor prevents creating a member with null name, so
        // create a valid instance then set the name to null to simulate invalid input
        testMember = new Member(1, "Valid Name", true, false, "REGULAR", "READ_WRITE", Instant.now(), Instant.now());
        testMember.setName(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> memberService.registerMember(testMember));
        verify(memberRepository, never()).save(any());
    }

    @Test
    void registerMember_WithInvalidRole_ShouldThrowIllegalArgumentException() {
        // Arrange
        // The constructor enforces a valid role, so create valid then set invalid
        testMember = new Member(1, "Test Member", true, false, "REGULAR", "READ_WRITE", Instant.now(), Instant.now());
        testMember.setRole("INVALID_ROLE");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> memberService.registerMember(testMember));
        verify(memberRepository, never()).save(any());
    }

    @Test
    void findMemberById_WithValidId_ShouldReturnMember() {
        // Arrange
        when(memberRepository.findById(1)).thenReturn(Optional.of(testMember));

        // Act
        Optional<Member> result = memberService.findMemberById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testMember.getId(), result.get().getId());
    }

    @Test
    void findMemberById_WithInvalidId_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> memberService.findMemberById(0));
        verify(memberRepository, never()).findById(anyInt());
    }

    @Test
    void updateMember_WithValidMember_ShouldUpdateAndReturnMember() {
        // Arrange
        when(memberRepository.findById(1)).thenReturn(Optional.of(testMember));
        when(memberRepository.update(any(Member.class))).thenReturn(testMember);

        testMember.setName("Updated Name");

        // Act
        Member result = memberService.updateMember(testMember);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(memberRepository).update(any(Member.class));
    }

    @Test
    void updateMember_WithNonExistentMember_ShouldReturnNull() {
        // Arrange
        when(memberRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        // Act & Assert: service throws when member does not exist
        assertThrows(IllegalArgumentException.class, () -> memberService.updateMember(testMember));
        verify(memberRepository, never()).update(any());
    }

    @Test
    void getAllMembers_ShouldReturnListOfMembers() {
        // Arrange
        List<Member> expectedMembers = Arrays.asList(testMember);
        when(memberRepository.findAll()).thenReturn(expectedMembers);

        // Act
        List<Member> result = memberService.getAllMembers();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(expectedMembers.size(), result.size());
        assertEquals(expectedMembers.get(0).getName(), result.get(0).getName());
    }

    @Test
    void searchMembersByName_WithValidName_ShouldReturnMatchingMembers() {
        // Arrange
        List<Member> expectedMembers = Arrays.asList(testMember);
        when(memberRepository.findByNameContaining("Test")).thenReturn(expectedMembers);

        // Act
        List<Member> result = memberService.searchMembersByName("Test");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(expectedMembers.size(), result.size());
        assertTrue(result.get(0).getName().contains("Test"));
    }

    @Test
    void searchMembersByName_WithEmptyName_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> memberService.searchMembersByName(""));
        verify(memberRepository, never()).findByNameContaining(any());
    }
}
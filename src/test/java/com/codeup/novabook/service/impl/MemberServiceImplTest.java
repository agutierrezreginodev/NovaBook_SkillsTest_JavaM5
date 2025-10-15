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
        testMember = new Member(1, "Test Member", true, false, "REGULAR", "USER", now, now);
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
        testMember = new Member(1, null, true, false, "REGULAR", "USER", Instant.now(), Instant.now());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> memberService.registerMember(testMember));
        verify(memberRepository, never()).save(any());
    }

    @Test
    void registerMember_WithInvalidRole_ShouldThrowIllegalArgumentException() {
        // Arrange
        testMember = new Member(1, "Test Member", true, false, "INVALID_ROLE", "USER", Instant.now(), Instant.now());

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
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        testMember.setName("Updated Name");

        // Act
        Member result = memberService.updateMember(testMember);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void updateMember_WithNonExistentMember_ShouldReturnNull() {
        // Arrange
        when(memberRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        Member result = memberService.updateMember(testMember);

        // Assert
        assertNull(result);
        verify(memberRepository, never()).save(any());
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
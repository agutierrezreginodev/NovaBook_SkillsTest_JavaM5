package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.Lending;
import com.codeup.novabook.repository.LendingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LendingServiceImplTest {

    @Mock
    private LendingRepository lendingRepository;

    @InjectMocks
    private LendingServiceImpl lendingService;

    private Lending testLending;

    @BeforeEach
    void setUp() {
        Instant now = Instant.now();
        Instant returnDate = now.plus(14, ChronoUnit.DAYS);
        testLending = new Lending(1, 1, 1, now, returnDate, false, now, now);
    }

    @Test
    void lendBook_WithValidParameters_ShouldCreateLending() {
        // Arrange
        int memberId = 1;
        int bookId = 1;
        int lendingDays = 14;
        when(lendingRepository.findByMemberId(memberId)).thenReturn(Arrays.asList());
        when(lendingRepository.save(any(Lending.class))).thenReturn(testLending);

        // Act
        Lending result = lendingService.lendBook(memberId, bookId, lendingDays);

        // Assert
        assertNotNull(result);
        assertEquals(memberId, result.getMemberId());
        assertEquals(bookId, result.getBookId());
        verify(lendingRepository).save(any(Lending.class));
    }

    @Test
    void lendBook_WithInvalidMemberId_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> lendingService.lendBook(0, 1, 14));
        verify(lendingRepository, never()).save(any());
    }

    @Test
    void lendBook_WithInvalidBookId_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> lendingService.lendBook(1, 0, 14));
        verify(lendingRepository, never()).save(any());
    }

    @Test
    void lendBook_WhenMemberReachedMaxBooks_ShouldThrowIllegalArgumentException() {
        // Arrange
        List<Lending> activeLoans = Arrays.asList(
            testLending,
            new Lending(2, 1, 2, Instant.now(), Instant.now().plus(14, ChronoUnit.DAYS), false, Instant.now(), Instant.now()),
            new Lending(3, 1, 3, Instant.now(), Instant.now().plus(14, ChronoUnit.DAYS), false, Instant.now(), Instant.now())
        );
        when(lendingRepository.findByMemberId(1)).thenReturn(activeLoans);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> lendingService.lendBook(1, 1, 14));
        verify(lendingRepository, never()).save(any());
    }

    @Test
    void findLendingById_WithValidId_ShouldReturnLending() {
        // Arrange
        when(lendingRepository.findById(1)).thenReturn(Optional.of(testLending));

        // Act
        Optional<Lending> result = lendingService.findLendingById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testLending.getId(), result.get().getId());
    }

    @Test
    void returnBook_WithValidLendingId_ShouldReturnTrue() {
        // Arrange
        int lendingId = 1;
        when(lendingRepository.findById(lendingId)).thenReturn(Optional.of(testLending));
        when(lendingRepository.save(any(Lending.class))).thenReturn(testLending);

        // Act
        boolean result = lendingService.returnBook(lendingId);

        // Assert
        assertTrue(result);
        verify(lendingRepository).save(any(Lending.class));
    }

    @Test
    void returnBook_WithInvalidLendingId_ShouldReturnFalse() {
        // Arrange
        when(lendingRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        boolean result = lendingService.returnBook(1);

        // Assert
        assertFalse(result);
        verify(lendingRepository, never()).save(any());
    }
}
package com.codeup.novabook.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceIsbnTest {

    private final BookServiceImpl service = new BookServiceImpl();

    @Test
    void validIsbn13WithoutHyphens() {
        assertTrue(service.isValidIsbn("9780134685991"));
    }

    @Test
    void validIsbn13WithHyphens() {
        assertTrue(service.isValidIsbn("978-0-13-468599-1"));
    }

    @Test
    void validIsbn10WithX() {
        assertTrue(service.isValidIsbn("0306406152"));
    }

    @Test
    void invalidIsbn() {
        assertFalse(service.isValidIsbn("123-ABC-456"));
    }
}

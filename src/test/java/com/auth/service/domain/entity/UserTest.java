package com.auth.service.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .login("johndoe")
                .password("secret123")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+123456789")
                .createdAt(now)
                .build();

        assertEquals("johndoe", user.getLogin());
        assertEquals("secret123", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("+123456789", user.getPhone());
        assertEquals(now, user.getCreatedAt());
    }
}

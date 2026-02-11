package techthree.binitright.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BinItRightUserTest {
    @Test
    void shouldSetAndGetParentFields_viaChild() {
        Admin admin = new Admin();

        admin.setId(10L);
        admin.setName("Test Name");
        admin.setUsername("testuser");
        admin.setPassword_hash("hash");
        admin.setLocale("en_SG");
        admin.setEmailAddress("test@example.com");
        admin.setRole("ADMIN");

        LocalDateTime now = LocalDateTime.of(2026, 2, 11, 12, 0);
        admin.setCreated_at(now);

        assertEquals(10L, admin.getId());
        assertEquals("Test Name", admin.getName());
        assertEquals("testuser", admin.getUsername());
        assertEquals("hash", admin.getPassword_hash());
        assertEquals("en_SG", admin.getLocale());
        assertEquals("test@example.com", admin.getEmailAddress());
        assertEquals("ADMIN", admin.getRole());
        assertEquals(now, admin.getCreated_at());
    }
}

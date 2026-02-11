package tech3.binitright.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class UserTest {
    @Test
    void defaultConstructor_shouldSetPointBalanceToZero() {
        User user = new User();
        assertEquals(0, user.getPointBalance());
    }

    @Test
    void settersAndGetters_shouldWork_forChildAndParentFields() {
        User user = new User();


        user.setId(1L);
        user.setName("Sujitha");
        user.setUsername("sujitha01");
        user.setPassword_hash("hash");
        user.setLocale("en_SG");
        user.setEmailAddress("sujitha@example.com");
        user.setRole("USER");

        LocalDateTime created = LocalDateTime.of(2026, 2, 11, 10, 0);
        user.setCreated_at(created);


        user.setUserAddress("Singapore");
        user.setCurrentRank(3);
        user.setCarbonEmissionSaved(12.5f);

        LocalDateTime updated = LocalDateTime.of(2026, 2, 11, 11, 0);
        user.setUpdatedat(updated);

        user.setPointBalance(250);

        user.setIssues(new ArrayList<>());
        user.setCheckin(new ArrayList<>());
        user.setRewardredemption(new ArrayList<>());
        user.setFeedbacks(new ArrayList<>());

        assertEquals(1L, user.getId());
        assertEquals("Sujitha", user.getName());
        assertEquals("sujitha01", user.getUsername());
        assertEquals("hash", user.getPassword_hash());
        assertEquals("en_SG", user.getLocale());
        assertEquals("sujitha@example.com", user.getEmailAddress());
        assertEquals("USER", user.getRole());
        assertEquals(created, user.getCreated_at());

        assertEquals("Singapore", user.getUserAddress());
        assertEquals(3, user.getCurrentRank());
        assertEquals(12.5f, user.getCarbonEmissionSaved(), 0.0001f);
        assertEquals(updated, user.getUpdatedat());
        assertEquals(250, user.getPointBalance());

        assertNotNull(user.getIssues());
        assertNotNull(user.getCheckin());
        assertNotNull(user.getRewardredemption());
        assertNotNull(user.getFeedbacks());
    }

    @Test
    void constructor_shouldSetSelectedFields() {
        User user = new User("Address", 2, 5.0f);

        assertEquals("Address", user.getUserAddress());
        assertEquals(2, user.getCurrentRank());
        assertEquals(5.0f, user.getCarbonEmissionSaved(), 0.0001f);
        assertEquals(0, user.getPointBalance());
    }
}


package tech3.binitright.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class UserAccessoriesTest {
    @Test
    void settersAndGetters_shouldWork() {
        UserAccessories ua = new UserAccessories();

        User user = new User();
        Accessories acc = new Accessories();

        ua.setUserAccessoriesId(1L);
        ua.setEquipped(true);
        ua.setUser(user);
        ua.setAccessories(acc);

        assertEquals(1L, ua.getUserAccessoriesId());
        assertTrue(ua.isEquipped());
        assertSame(user, ua.getUser());
        assertSame(acc, ua.getAccessories());
    }

    @Test
    void constructor_shouldSetFields() {
        User user = new User();
        Accessories acc = new Accessories();

        UserAccessories ua = new UserAccessories(false, user, acc);

        assertFalse(ua.isEquipped());
        assertSame(user, ua.getUser());
        assertSame(acc, ua.getAccessories());
        assertNull(ua.getUserAccessoriesId()); // not generated until persisted
    }
}

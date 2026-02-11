package tech3.binitright.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.interfacemethods.UserAccessoriesInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.UserAccessories;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserAccessoriesRestControllerTest {

    private UserAccessoriesRestController controller;
    private UserAccessoriesInterface userAccessoriesService;
    private UserInterface userService;

    @BeforeEach
    void setUp() {
        controller = new UserAccessoriesRestController();
        userAccessoriesService = Mockito.mock(UserAccessoriesInterface.class);
        userService = Mockito.mock(UserInterface.class);
        ReflectionTestUtils.setField(controller, "userAccessoriesService", userAccessoriesService);
        ReflectionTestUtils.setField(controller, "userService", userService);
    }

    @Test
    void getMyAccessories_ReturnsList() {
        // Arrange
        Long userId = 101L;
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(userId.toString());

        UserAccessories item = new UserAccessories();
        List<UserAccessories> mockItems = List.of(item);

        when(userAccessoriesService.findAllByUser_Id(userId)).thenReturn(mockItems);

        // Act
        ResponseEntity<List<UserAccessories>> response = controller.getMyAccessories(auth);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userAccessoriesService).findAllByUser_Id(userId);
    }

    @Test
    void equipAccessory_Success_ReturnsOk() {
        // Arrange
        Long userId = 101L;
        Long accessoryId = 5L;
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(userId.toString());

        // Act
        ResponseEntity<String> response = controller.equipAccessory(accessoryId, auth);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item equipped successfully.", response.getBody());
        verify(userAccessoriesService).equipItem(userId, accessoryId);
    }

    @Test
    void unequipAccessory_Success_ReturnsOk() {
        // Arrange
        Long userId = 101L;
        Long accessoryId = 5L;
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(userId.toString());

        // Act
        ResponseEntity<String> response = controller.unequipAccessory(accessoryId, auth);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item unequipped.", response.getBody());
        verify(userAccessoriesService).unequipItem(userId, accessoryId);
    }
}

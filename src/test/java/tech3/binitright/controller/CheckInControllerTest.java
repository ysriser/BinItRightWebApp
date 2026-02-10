package tech3.binitright.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tech3.binitright.JwtAuthFilter;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.User;
import tech3.binitright.request.CheckInDataReq;
import tech3.binitright.service.CheckInImplementation;
import tech3.binitright.service.UserImplementation;

@WebMvcTest(
        controllers = CheckInController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
class CheckInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // To convert Java objects to JSON

    @MockitoBean private CheckInImplementation checkInService;
    @MockitoBean private UserImplementation userService;
    @Autowired
    public void setUserService(UserImplementation userService) {
        this.userService = userService;
    }
    @Autowired
    public void setcheckInService(CheckInImplementation checkInService) {
        this.checkInService = checkInService;
    }

    @Test
    @WithMockUser(username = "123") // Simulates authentication.getName() returning "123"
    void submitCheckIn_WithUserId_ReturnsSuccess() throws Exception {
        // Arrange
        CheckInDataReq request = new CheckInDataReq();
        request.setQuantity(5);

        CheckIn mockSaved = new CheckIn();
        mockSaved.setCheckInId(1L);

        when(checkInService.processCheckIn(any(CheckInDataReq.class), eq(123L)))
                .thenReturn(mockSaved);

        // Act & Assert
        mockMvc.perform(post("/api/checkin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("SUCCESS"))
                .andExpect(jsonPath("$.responseDesc").value("Check-in submitted successfully"))
                .andExpect(jsonPath("$.checkInId").value(1));
    }

    @Test
    @WithMockUser(username = "johndoe") // Simulates fallback logic (username instead of ID)
    void submitCheckIn_WithUsername_TriggersFallback() throws Exception {
        // Arrange
        CheckInDataReq request = new CheckInDataReq();
        request.setQuantity(15); // > 10 to test the message change

        User mockUser = new User();
        mockUser.setId(99L);
        mockUser.setUsername("johndoe");

        CheckIn mockSaved = new CheckIn();
        mockSaved.setCheckInId(2L);

        when(userService.findByUsername("johndoe")).thenReturn(List.of(mockUser));
        when(checkInService.processCheckIn(any(CheckInDataReq.class), eq(99L)))
                .thenReturn(mockSaved);

        // Act & Assert
        mockMvc.perform(post("/api/checkin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseDesc").value("Check-in submitted successfully and pending validation"));
    }
}

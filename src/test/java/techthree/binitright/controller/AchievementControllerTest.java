package techthree.binitright.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import techthree.binitright.JwtAuthFilter;
import techthree.binitright.request.AchievementDTO;
import techthree.binitright.service.AchievementImplementation;
import java.util.List;

import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AchievementController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)

class AchievementControllerTest {

    @MockitoBean
    private techthree.binitright.util.JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AchievementImplementation achievementImplementation;

    @Test
    @WithMockUser(username = "testUser")
    void getUserAchievements_ShouldReturnList() throws Exception {
        // Arrange
        Long userId = 1L;

        // Corrected Constructor: Providing all 6 required arguments
        // Argument 1 must be a Long (no quotes)
        AchievementDTO ach1 = new AchievementDTO(
                1L,
                "Master Recycler",
                "Recycle 100 items",
                "Recycle > 100",
                "http://icon.url/1",
                true
        );

        AchievementDTO ach2 = new AchievementDTO(
                2L,
                "Bin Expert",
                "Correctly sort 50 items",
                "Sort > 50",
                "http://icon.url/2",
                false
        );

        when(achievementImplementation.getAchievementsForUser(userId))
                .thenReturn(List.of(ach1, ach2));

        // Act & Assert
        mockMvc.perform(get("/api/achievements/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Master Recycler"))
                .andExpect(jsonPath("$[1].isUnlocked").value(false));
    }
}
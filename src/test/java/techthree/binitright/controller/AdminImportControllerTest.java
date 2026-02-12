package techthree.binitright.controller;

import org.junit.jupiter.api.Test;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import techthree.binitright.JwtAuthFilter;
import techthree.binitright.service.BinDataImporter;
import techthree.binitright.util.JwtUtil;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AdminImportController.class,
        // Exclude the filter to prevent UnsatisfiedDependencyException on JwtUtil
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
class AdminImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BinDataImporter binDataImporter;

    @MockitoBean
    private JwtUtil jwtUtil; // Mocked to satisfy any remaining security context needs

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void importBins_ShouldReturnSuccessMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/admin/import/bins")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));

        // Verify the service method was actually called
        verify(binDataImporter, times(1)).importData();
    }
}
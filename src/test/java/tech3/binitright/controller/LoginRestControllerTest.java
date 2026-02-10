package tech3.binitright.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.model.Admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminInterface adminService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void createAdminReturnsConflictWhenUsernameExists() throws Exception {
        final Admin existing = new Admin();
        existing.setUsername("admin1");
        when(adminService.findAdminByUsername("admin1")).thenReturn(List.of(existing));

        final Admin request = new Admin();
        request.setUsername("admin1");
        request.setPassword_hash("plain");

        mockMvc.perform(post("/api/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Username already exists"));

        verify(passwordEncoder, never()).encode(any());
        verify(adminService, never()).saveAdmin(any());
    }

    @Test
    void createAdminEncodesPasswordAndSavesWhenUsernameIsNew() throws Exception {
        when(adminService.findAdminByUsername("newadmin")).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode("plain")).thenReturn("encoded-value");

        final Admin request = new Admin();
        request.setUsername("newadmin");
        request.setPassword_hash("plain");

        mockMvc.perform(post("/api/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Admin Account created successfully"));

        verify(passwordEncoder).encode("plain");
        verify(adminService).saveAdmin(any(Admin.class));
    }
}

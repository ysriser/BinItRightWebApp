package techthree.binitright.controller;

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
import techthree.binitright.JwtAuthFilter;
import techthree.binitright.request.ChatRequest;
import techthree.binitright.service.ChatImplementation;
import techthree.binitright.util.JwtUtil;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ChatController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChatImplementation chatService;

    @MockitoBean
    private JwtUtil jwtUtil; // Prevents UnsatisfiedDependencyException

    @Test
    @WithMockUser
    void chat_ValidMessage_ReturnsReply() throws Exception {

        ChatRequest request = new ChatRequest();
        request.setMessage("How do I recycle plastic?");

        when(chatService.askRecyclingAssistant(anyString()))
                .thenReturn("Plastic should be rinsed before recycling.");


        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value("Plastic should be rinsed before recycling."));
    }

    @Test
    @WithMockUser
    void chat_EmptyMessage_ReturnsBadRequest() throws Exception {

        ChatRequest request = new ChatRequest();
        request.setMessage("");


        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reply").value("Message is empty."));
    }
}
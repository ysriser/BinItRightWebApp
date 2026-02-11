package techthree.binitright.service;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import techthree.binitright.interfacemethods.LlmClient;


import static org.junit.jupiter.api.Assertions.*;

class ChatImplementationTest {

    static class FakeLlmClient implements LlmClient {
        String response;
        RuntimeException toThrow;

        String lastModel;
        String lastSystem;
        String lastUser;

        @Override
        public String chat(String model, String systemPrompt, String userMessage) {
            if (toThrow != null) throw toThrow;
            this.lastModel = model;
            this.lastSystem = systemPrompt;
            this.lastUser = userMessage;
            return response;
        }
    }

    @Test
    void askRecyclingAssistant_success_returnsResponse() {
        FakeLlmClient fake = new FakeLlmClient();
        fake.response = "Use blue bin if clean and dry.";

        ChatImplementation service = new ChatImplementation(fake);
        ReflectionTestUtils.setField(service, "model", "gpt-test-model");

        String result = service.askRecyclingAssistant("Plastic bottle");

        assertEquals("Use blue bin if clean and dry.", result);
        assertEquals("gpt-test-model", fake.lastModel);
        assertTrue(fake.lastSystem.contains("AI recycling assistant for Singapore"));
        assertEquals("Plastic bottle", fake.lastUser);
    }

    @Test
    void askRecyclingAssistant_whenThrows_returnsFallback() {
        FakeLlmClient fake = new FakeLlmClient();
        fake.toThrow = new RuntimeException("down");

        ChatImplementation service = new ChatImplementation(fake);
        ReflectionTestUtils.setField(service, "model", "gpt-test-model");

        String result = service.askRecyclingAssistant("Battery");

        assertEquals("Recycling assistant is temporarily unavailable. Please try again later.", result);
    }

    @Test
    void generateProgressSummary_success_formatsPrompt_andReturnsResponse() {
        FakeLlmClient fake = new FakeLlmClient();
        fake.response = "Nice work—keep recycling!";

        ChatImplementation service = new ChatImplementation(fake);
        ReflectionTestUtils.setField(service, "model", "gpt-test-model");

        String result = service.generateProgressSummary(1200, 2.54, 3, 18);

        assertEquals("Nice work—keep recycling!", result);

        assertTrue(fake.lastSystem.contains("max 35 words"));
        assertTrue(fake.lastUser.contains("Point balance: 1200"));
        assertTrue(fake.lastUser.contains("Carbon emission saved: 2.5 kg"));
        assertTrue(fake.lastUser.contains("Current rank: 3"));
        assertTrue(fake.lastUser.contains("Total recycled items: 18"));
    }

    @Test
    void generateProgressSummary_whenThrows_returnsFallback() {
        FakeLlmClient fake = new FakeLlmClient();
        fake.toThrow = new RuntimeException("timeout");

        ChatImplementation service = new ChatImplementation(fake);
        ReflectionTestUtils.setField(service, "model", "gpt-test-model");

        String result = service.generateProgressSummary(100, 1.2, 1, 5);

        assertEquals("You're making a real environmental impact 🌱 Keep recycling to climb higher and save more CO₂!", result);
    }
}




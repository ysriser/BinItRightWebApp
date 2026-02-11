package tech3.binitright.interfacemethods;

public interface LlmClient {
    String chat(String model, String systemPrompt, String userMessage);
}

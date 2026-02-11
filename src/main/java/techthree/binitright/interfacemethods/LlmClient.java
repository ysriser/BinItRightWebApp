package techthree.binitright.interfacemethods;

public interface LlmClient {
    String chat(String model, String systemPrompt, String userMessage);
}

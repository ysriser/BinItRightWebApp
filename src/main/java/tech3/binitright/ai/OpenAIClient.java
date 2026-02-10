//package tech3.binitright.ai;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class OpenAIClient {
//
//    private final WebClient webClient;
//
//    public OpenAIClient(
//            @Value("${openai.base-url}") String baseUrl,
//            @Value("${openai.api.key}") String apiKey
//    ) {
//        this.webClient = WebClient.builder()
//                .baseUrl(baseUrl)
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }
//
//    @SuppressWarnings("unchecked")
//    public String chat(String model, String systemPrompt, String userMessage) {
//
//        Map<String, Object> body = Map.of(
//                "model", model,
//                "messages", List.of(
//                        Map.of("role", "system", "content", systemPrompt),
//                        Map.of("role", "user", "content", userMessage)
//                )
//        );
//
//        // Response shape: { choices: [ { message: { content: "..." } } ] }
//        Map<String, Object> resp = webClient.post()
//                .uri("/chat/completions")
//                .bodyValue(body)
//                .retrieve()
//                .bodyToMono(Map.class)
//                .block();
//
//        if (resp == null) return "No response from OpenAI.";
//
//        List<Object> choices = (List<Object>) resp.get("choices");
//        if (choices == null || choices.isEmpty()) return "No choices returned.";
//
//        Map<String, Object> choice0 = (Map<String, Object>) choices.get(0);
//        Map<String, Object> message = (Map<String, Object>) choice0.get("message");
//
//        Object content = (message != null) ? message.get("content") : null;
//        return content != null ? content.toString() : "Empty reply.";
//    }
//
//
//}
//
package tech3.binitright.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import tech3.binitright.interfacemethods.LlmClient;

import java.util.List;
import java.util.Map;

@Component
public class OpenAIClient implements LlmClient {

    private final WebClient webClient;

    public OpenAIClient(
            @Value("${openai.base-url}") String baseUrl,
            @Value("${openai.api.key}") String apiKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @SuppressWarnings("unchecked")
    public String chat(String model, String systemPrompt, String userMessage) {

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                )
        );

        try {
            Map<String, Object> resp = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (resp == null) return fallback();

            List<Object> choices = (List<Object>) resp.get("choices");
            if (choices == null || choices.isEmpty()) return fallback();

            Map<String, Object> choice0 = (Map<String, Object>) choices.get(0);
            Map<String, Object> message = (Map<String, Object>) choice0.get("message");

            Object content = (message != null) ? message.get("content") : null;
            return content != null ? content.toString() : fallback();

        } catch (Exception e) {
            // ⭐ graceful fallback for demo / rate-limit / network failure
            return fallback();
        }
    }

    private String fallback() {
        return "⚠️ AI service temporarily unavailable. " +
                "Please try again later.";
    }
}

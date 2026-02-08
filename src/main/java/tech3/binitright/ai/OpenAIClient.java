package tech3.binitright.ai;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public final class OpenAIClient {

    private final WebClient webClient;

    public OpenAIClient(
            @Value("${openai.base-url}") final String baseUrl,
            @Value("${openai.api.key}") final String apiKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @SuppressWarnings("unchecked")
    public String chat(final String model, final String systemPrompt, final String userMessage) {

        final Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                )
        );

        try {
            final Map<String, Object> resp = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (resp == null) {
                return fallback();
            }

            final List<Object> choices = (List<Object>) resp.get("choices");
            if (choices == null || choices.isEmpty()) {
                return fallback();
            }

            final Map<String, Object> choice0 = (Map<String, Object>) choices.get(0);
            final Map<String, Object> message = (Map<String, Object>) choice0.get("message");

            final Object content = (message != null) ? message.get("content") : null;
            return content != null ? content.toString() : fallback();

        } catch (final Exception e) {
            // ⭐ graceful fallback for demo / rate-limit / network failure
            return fallback();
        }
    }

    private String fallback() {
        return "⚠️ AI service temporarily unavailable. "
                + "Please try again later.";
    }
}
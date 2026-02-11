package tech3.binitright.ai;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import static org.junit.jupiter.api.Assertions.*;


public class OpenAIClientTest {
    @Test
    void chat_whenValidResponse_returnsMessageContent() {
        String json =
                """
                {
                  "choices": [
                    { "message": { "content": "Hello from AI" } }
                  ]
                }
                """;

        DisposableServer server = HttpServer.create()
                .port(0)
                .route(routes -> routes.post("/chat/completions", (req, res) ->
                        res.status(200)
                                .header("Content-Type", "application/json")
                                .sendString(Mono.just(json))
                ))
                .bindNow();

        try {
            String baseUrl = "http://localhost:" + server.port();
            OpenAIClient client = new OpenAIClient(baseUrl, "fake-key");

            String result = client.chat("gpt-4", "system", "user");

            assertEquals("Hello from AI", result);
        } finally {
            server.disposeNow();
        }
    }

    @Test
    void chat_whenChoicesMissing_returnsFallback() {
        String json = "{}";

        DisposableServer server = HttpServer.create()
                .port(0)
                .route(routes -> routes.post("/chat/completions", (req, res) ->
                        res.status(200)
                                .header("Content-Type", "application/json")
                                .sendString(Mono.just(json))
                ))
                .bindNow();

        try {
            String baseUrl = "http://localhost:" + server.port();
            OpenAIClient client = new OpenAIClient(baseUrl, "fake-key");

            String result = client.chat("gpt-4", "system", "user");

            assertEquals("AI service temporarily unavailable. Please try again later.", result);
        } finally {
            server.disposeNow();
        }
    }

    @Test
    void chat_whenServerReturns500_returnsFallback() {
        DisposableServer server = HttpServer.create()
                .port(0)
                .route(routes -> routes.post("/chat/completions", (req, res) ->
                        res.status(500).send()
                ))
                .bindNow();

        try {
            String baseUrl = "http://localhost:" + server.port();
            OpenAIClient client = new OpenAIClient(baseUrl, "fake-key");

            String result = client.chat("gpt-4", "system", "user");

            assertEquals("AI service temporarily unavailable. Please try again later.", result);
        } finally {
            server.disposeNow();
        }
    }
}
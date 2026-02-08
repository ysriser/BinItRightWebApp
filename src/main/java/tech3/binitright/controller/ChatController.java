package tech3.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.request.ChatRequest;
import tech3.binitright.response.ChatResponse;
import tech3.binitright.service.ChatImplementation;

@RestController
@RequestMapping("/api/chat")
public final class ChatController {

    private final ChatImplementation chatService;

    public ChatController(final ChatImplementation chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody final ChatRequest req) {
        final String msg = (req.getMessage() == null) ? "" : req.getMessage().trim();
        if (msg.isEmpty()) {
            return ResponseEntity.badRequest().body(new ChatResponse("Message is empty."));
        }

        final String reply = chatService.askRecyclingAssistant(msg);
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}
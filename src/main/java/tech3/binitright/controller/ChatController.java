package tech3.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.request.ChatRequest;
import tech3.binitright.response.ChatResponse;
import tech3.binitright.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest req) {
        String msg = (req.getMessage() == null) ? "" : req.getMessage().trim();
        if (msg.isEmpty()) return ResponseEntity.badRequest().body(new ChatResponse("Message is empty."));

        String reply = chatService.askRecyclingAssistant(msg);
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}
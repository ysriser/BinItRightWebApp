package techthree.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techthree.binitright.request.ChatRequest;
import techthree.binitright.response.ChatResponse;
import techthree.binitright.service.ChatImplementation;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatImplementation chatService;

    public ChatController(ChatImplementation chatService) {
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
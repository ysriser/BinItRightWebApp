package techthree.binitright.request;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private Long userId;
}
package tech3.binitright.request;

import lombok.Data;

@Data
public final class ChatRequest { // 修复：声明类为 final
    private String message;
    private Long userId;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
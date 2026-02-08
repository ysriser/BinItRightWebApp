package tech3.binitright.response;

import lombok.Data;

@Data
public final class ChatResponse {
    private String reply;

    public ChatResponse(final String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(final String reply) {
        this.reply = reply;
    }
}
package tech3.binitright.response;

public final class RegisterResponse {
    private boolean success;
    private String message;

    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}


package tech3.binitright.response;

public final class RegisterResponse {
    private final boolean success;
    private final String message;

    public RegisterResponse(final boolean success, final String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
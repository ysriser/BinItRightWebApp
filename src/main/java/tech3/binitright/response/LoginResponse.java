package tech3.binitright.response;

public final class LoginResponse {
    private boolean success;
    private String message;
    private String token; // simple token for mobile

    public LoginResponse() {}

    public LoginResponse(final boolean success, final String message, final String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    // Getters and setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}



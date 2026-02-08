package tech3.binitright.request;

public final class PresignedUploadRequest {
    private Long userId;

    public PresignedUploadRequest() {
    }

    public PresignedUploadRequest(final Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
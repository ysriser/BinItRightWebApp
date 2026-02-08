package tech3.binitright.request;

public class PresignedUploadRequest {
    private Long userId;

    public PresignedUploadRequest() {}

    public PresignedUploadRequest(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

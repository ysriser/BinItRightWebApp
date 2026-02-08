package tech3.binitright.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class PresignedUploadResponse {

    @JsonProperty("uploadUrl")
    private String uploadUrl;

    @JsonProperty("objectKey")
    private String objectKey;

    public PresignedUploadResponse() {
    }

    public PresignedUploadResponse(final String uploadUrl, final String objectKey) {
        this.uploadUrl = uploadUrl;
        this.objectKey = objectKey;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(final String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(final String objectKey) {
        this.objectKey = objectKey;
    }
}
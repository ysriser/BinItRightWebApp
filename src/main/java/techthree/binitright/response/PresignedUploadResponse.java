package techthree.binitright.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PresignedUploadResponse {

    @JsonProperty("uploadUrl")
    private String uploadUrl;

    @JsonProperty("objectKey")
    private String objectKey;

    public PresignedUploadResponse() {}

    public PresignedUploadResponse(String uploadUrl, String objectKey) {
        this.uploadUrl = uploadUrl;
        this.objectKey = objectKey;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }
}
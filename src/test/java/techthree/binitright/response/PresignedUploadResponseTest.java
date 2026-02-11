package techthree.binitright.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class PresignedUploadResponseTest {
    @Test
    void defaultConstructor_andSetters_shouldWork() {
        PresignedUploadResponse response = new PresignedUploadResponse();

        response.setUploadUrl("https://s3-upload-url");
        response.setObjectKey("video123.mp4");

        assertEquals("https://s3-upload-url", response.getUploadUrl());
        assertEquals("video123.mp4", response.getObjectKey());
    }

    @Test
    void parameterizedConstructor_shouldSetFieldsCorrectly() {
        PresignedUploadResponse response =
                new PresignedUploadResponse("https://upload", "key123");

        assertEquals("https://upload", response.getUploadUrl());
        assertEquals("key123", response.getObjectKey());
    }
}

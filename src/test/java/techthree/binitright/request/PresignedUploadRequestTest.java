package techthree.binitright.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class PresignedUploadRequestTest {
    @Test
    void defaultConstructor_andSetter_shouldWork() {
        PresignedUploadRequest request = new PresignedUploadRequest();

        request.setUserId(100L);

        assertEquals(100L, request.getUserId());
    }

    @Test
    void parameterizedConstructor_shouldSetUserId() {
        PresignedUploadRequest request = new PresignedUploadRequest(200L);

        assertEquals(200L, request.getUserId());
    }
}

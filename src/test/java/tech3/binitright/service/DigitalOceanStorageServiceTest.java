package tech3.binitright.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URL;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DigitalOceanStorageServiceTest {
    @Mock
    private AmazonS3 s3;

    private DigitalOceanStorageService service;

    @BeforeEach
    void setUp() {
        service = new DigitalOceanStorageService(s3);
        // set @Value field
        ReflectionTestUtils.setField(service, "bucket", "my-test-bucket");
    }

    @Test
    void generateSignedVideoUrl_buildsPresignedRequest_andReturnsUrl() throws Exception {
        // given
        String objectKey = "videos/demo.mp4";
        URL fakeUrl = new URL("https://example.com/presigned");

        when(s3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenReturn(fakeUrl);

        // when
        String result = service.generateSignedVideoUrl(objectKey);

        // then
        assertEquals("https://example.com/presigned", result);

        ArgumentCaptor<GeneratePresignedUrlRequest> captor =
                ArgumentCaptor.forClass(GeneratePresignedUrlRequest.class);

        verify(s3).generatePresignedUrl(captor.capture());

        GeneratePresignedUrlRequest req = captor.getValue();
        assertNotNull(req);

        // bucket + key
        assertEquals("my-test-bucket", req.getBucketName());
        assertEquals(objectKey, req.getKey());

        // method
        assertEquals(HttpMethod.GET, req.getMethod());

        // expiry should be in the future (rough check)
        Date expiration = req.getExpiration();
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()), "Expiration should be in the future");

        // response params
        assertEquals("video/mp4", req.getRequestParameters().get("response-content-type"));
        assertEquals("inline", req.getRequestParameters().get("response-content-disposition"));
    }
}


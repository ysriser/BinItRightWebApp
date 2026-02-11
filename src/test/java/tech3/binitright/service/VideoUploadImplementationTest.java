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

import tech3.binitright.response.PresignedUploadResponse;

import java.net.URL;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoUploadImplementationTest {
    @Mock
    private AmazonS3 s3;

    private VideoUploadImplementation service;

    @BeforeEach
    void setUp() {
        service = new VideoUploadImplementation(s3);

        // inject @Value bucket manually
        try {
            var f = VideoUploadImplementation.class.getDeclaredField("bucket");
            f.setAccessible(true);
            f.set(service, "test-bucket");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createPresignedUpload_returnsUrlAndObjectKey() throws Exception {
        Long userId = 42L;

        URL fakeUrl = new URL("https://signed.example.com/upload");
        when(s3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenReturn(fakeUrl);

        PresignedUploadResponse response =
                service.createPresignedUpload(userId);

        assertNotNull(response);
        assertEquals(fakeUrl.toString(), response.getUploadUrl());
        assertNotNull(response.getObjectKey());

        // object key format check
        assertTrue(response.getObjectKey().startsWith("videos/42/"));
        assertTrue(response.getObjectKey().endsWith(".mp4"));

        // capture request for validation
        ArgumentCaptor<GeneratePresignedUrlRequest> captor =
                ArgumentCaptor.forClass(GeneratePresignedUrlRequest.class);

        verify(s3).generatePresignedUrl(captor.capture());

        GeneratePresignedUrlRequest req = captor.getValue();

        assertEquals("test-bucket", req.getBucketName());
        assertEquals(HttpMethod.PUT, req.getMethod());
        assertEquals("video/mp4", req.getContentType());

        // expiry sanity check (≈10 min)
        Date expiry = req.getExpiration();
        assertTrue(expiry.after(Date.from(Instant.now())));
    }
}

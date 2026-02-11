package tech3.binitright.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.response.PresignedUploadResponse;
import tech3.binitright.service.VideoUploadImplementation;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VideoUploadControllerTest {

    private VideoUploadImplementation service;
    private AmazonS3 s3Client;
    private final String BUCKET_NAME = "test-bucket";

    @BeforeEach
    void setUp() {
        s3Client = Mockito.mock(AmazonS3.class);
        service = new VideoUploadImplementation(s3Client);

        ReflectionTestUtils.setField(service, "bucket", BUCKET_NAME);
    }

    @Test
    void createPresignedUpload_ReturnsValidResponse() throws MalformedURLException {
        // Arrange
        Long userId = 123L;
        String mockUrlString = "https://s3.test.com/upload-here";
        URL mockUrl = new URL(mockUrlString);

        when(s3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenReturn(mockUrl);

        // Act
        PresignedUploadResponse response = service.createPresignedUpload(userId);

        // Assert
        assertNotNull(response);
        assertEquals(mockUrlString, response.getUploadUrl());


        assertNotNull(response.getObjectKey());
        assertTrue(response.getObjectKey().startsWith("videos/" + userId + "/"));
        assertTrue(response.getObjectKey().endsWith(".mp4"));


        ArgumentCaptor<GeneratePresignedUrlRequest> captor =
                ArgumentCaptor.forClass(GeneratePresignedUrlRequest.class);
        verify(s3Client).generatePresignedUrl(captor.capture());

        GeneratePresignedUrlRequest capturedRequest = captor.getValue();
        assertEquals(BUCKET_NAME, capturedRequest.getBucketName());
        assertEquals("video/mp4", capturedRequest.getContentType());
        assertEquals(com.amazonaws.HttpMethod.PUT, capturedRequest.getMethod());
    }
}
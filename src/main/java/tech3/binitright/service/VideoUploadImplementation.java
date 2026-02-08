package tech3.binitright.service;

import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import tech3.binitright.interfacemethods.VideoUploadInterface;
import tech3.binitright.response.PresignedUploadResponse;

@Service
public final class VideoUploadImplementation implements VideoUploadInterface {
    private final AmazonS3 s3;

    @Value("${storage.spaces.bucket}")
    private String bucket;

    public VideoUploadImplementation(final AmazonS3 s3) {
        this.s3 = s3;
    }
    @Override
    public PresignedUploadResponse createPresignedUpload(final Long userId) {

        // Backend-controlled object key
        final String objectKey = "videos/" + userId + "/" + UUID.randomUUID() + ".mp4";

        // Expiry (10 minutes)
        final Date expiry = Date.from(Instant.now().plus(10, ChronoUnit.MINUTES));

        // Pre-signed PUT request WITH Content-Type
        final GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiry)
                        .withContentType("video/mp4");

        final URL uploadUrl = s3.generatePresignedUrl(request);

        // Return opaque response
        return new PresignedUploadResponse(
                uploadUrl.toString(),
                objectKey
        );
    }
}

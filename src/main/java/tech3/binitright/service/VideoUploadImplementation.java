package tech3.binitright.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech3.binitright.interfacemethods.VideoUploadInterface;
import tech3.binitright.response.PresignedUploadResponse;

import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class VideoUploadImplementation implements VideoUploadInterface {
    private final AmazonS3 s3;

    @Value("${storage.spaces.bucket}")
    private String bucket;

    public VideoUploadImplementation(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public PresignedUploadResponse createPresignedUpload(Long userId) {

        // Backend-controlled object key
        String objectKey =
                "videos/" + userId + "/" + UUID.randomUUID() + ".mp4";

        // Expiry (10 minutes)
        Date expiry =
                Date.from(Instant.now().plus(10, ChronoUnit.MINUTES));

        // Pre-signed PUT request
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiry);

        URL uploadUrl = s3.generatePresignedUrl(request);

        // Return opaque response
        return new PresignedUploadResponse(
                uploadUrl.toString(),
                objectKey
        );
    }
}

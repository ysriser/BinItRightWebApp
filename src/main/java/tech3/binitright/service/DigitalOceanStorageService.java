package tech3.binitright.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

@Service
public final class DigitalOceanStorageService {

    private final AmazonS3 s3;

    @Value("${storage.spaces.bucket}")
    private String bucket;

    public DigitalOceanStorageService(final AmazonS3 s3) {
        this.s3 = s3;
    }
    public String generateSignedVideoUrl(final String objectKey) {

        final Date expiry =
                Date.from(Instant.now().plus(10, ChronoUnit.MINUTES));

        final GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiry);

        request.addRequestParameter("response-content-type", "video/mp4");
        request.addRequestParameter("response-content-disposition", "inline");

        return s3.generatePresignedUrl(request).toString();
    }

}

package techthree.binitright.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class DigitalOceanStorageService {

    private final AmazonS3 s3;

    @Value("${storage.spaces.bucket}")
    private String bucket;

    public DigitalOceanStorageService(AmazonS3 s3) {
        this.s3 = s3;
    }
    public String generateSignedVideoUrl(String objectKey) {

        Date expiry =
                Date.from(Instant.now().plus(10, ChronoUnit.MINUTES));

        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiry);

        request.addRequestParameter("response-content-type", "video/mp4");
        request.addRequestParameter("response-content-disposition", "inline");

        return s3.generatePresignedUrl(request).toString();
    }

}

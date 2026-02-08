package tech3.binitright.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import jakarta.annotation.PostConstruct;

@Configuration
@ConditionalOnProperty(
        name = "storage.spaces.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public final class SpacesConfig {

    private static final Logger log = LoggerFactory.getLogger(SpacesConfig.class);

    @Value("${storage.spaces.endpoint}")
    private String endpoint;

    @Value("${storage.spaces.region}")
    private String region;

    @Value("${storage.spaces.access-key}")
    private String accessKey;

    @Value("${storage.spaces.secret-key}")
    private String secretKey;

    @Value("${storage.spaces.bucket}")
    private String bucket;

    @PostConstruct
    void validate() {
        if (accessKey == null || accessKey.isBlank()
                || secretKey == null || secretKey.isBlank()) {
            // 修复了超长字符串行
            throw new IllegalStateException("DigitalOcean Spaces credentials are missing. " 
                    + "Set SPACESUACCESSUKEY and SPACESUSECRETUKEY.");
        }
        log.info("DigitalOcean Spaces configuration loaded");
    }

    @Bean
    public AmazonS3 amazonS3() {
        final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpoint, region)
                )
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
package tech3.binitright;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import tech3.binitright.service.DigitalOceanStorageService;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "openai.api.key=test-key"
        }
)
@ActiveProfiles("test")
class BinItRightApplicationTests {

    @MockBean
    private AmazonS3 amazonS3;

    @MockBean
    private DigitalOceanStorageService digitalOceanStorageService;

    @Test
    void contextLoads() {
        // Basic smoke test to ensure Spring context starts.
    }
}

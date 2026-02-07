package tech3.binitright;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import tech3.binitright.service.DigitalOceanStorageService;

@SpringBootTest(
        properties = {
                "openai.api.key=test-key"
        }
)
@ActiveProfiles("test")
class BinItRightApplicationTests {

    @MockBean
    private DigitalOceanStorageService digitalOceanStorageService;

    @Test
    void contextLoads() {
        // Basic smoke test to ensure Spring context starts.
    }
}

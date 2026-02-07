package tech3.binitright;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        properties = {
                "openai.api.key=test-key"
        }
)
@ActiveProfiles("test")
class BinItRightApplicationTests {

    @Test
    void contextLoads() {
        // Basic smoke test to ensure Spring context starts.
    }
}

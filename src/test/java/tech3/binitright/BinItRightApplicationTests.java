package tech3.binitright;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
                "openai.api.key=test-key"
        }
)
class BinItRightApplicationTests {

    @Test
    void contextLoads() {
        // Basic smoke test to ensure Spring context starts.
    }
}

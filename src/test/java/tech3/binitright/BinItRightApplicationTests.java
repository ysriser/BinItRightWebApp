package tech3.binitright;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
        }
)
class BinItRightApplicationTests {

    @Test
    void contextLoads() {
        // Basic smoke test to ensure Spring context starts.
    }
}

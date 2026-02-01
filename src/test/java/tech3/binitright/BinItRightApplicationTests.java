package tech3.binitright;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // This tells Spring to use application-test.properties
class BinItRightApplicationTests {
    @Test
    void contextLoads() {
        // This ensures your JPA entities and Repositories are mapped correctly
    }
}
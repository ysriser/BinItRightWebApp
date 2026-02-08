package tech3.binitright;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class BinItRightApplicationTests {

    @Test
    void applicationClassConstructs() {
        final BinItRightApplication app = new BinItRightApplication();
        assertNotNull(app);
    }

    @Test
    void mainMethodExists() throws NoSuchMethodException {
        assertNotNull(BinItRightApplication.class.getMethod("main", String[].class));
    }
}

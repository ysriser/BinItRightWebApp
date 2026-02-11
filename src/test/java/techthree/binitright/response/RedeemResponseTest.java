package techthree.binitright.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RedeemResponseTest {
    @Test
    void parameterizedConstructor_shouldSetFieldsCorrectly() {
        RedeemResponse response = new RedeemResponse(150, "Redeemed successfully");

        assertEquals(150, response.getNewTotalPoints());
        assertEquals("Redeemed successfully", response.getMessage());
    }

    @Test
    void defaultConstructor_shouldCreateObject() {
        RedeemResponse response = new RedeemResponse();

        assertNotNull(response);
    }
}

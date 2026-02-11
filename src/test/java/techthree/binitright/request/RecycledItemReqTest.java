package techthree.binitright.request;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecycledItemReqTest {
    @Test
    void gettersAndSetters_shouldWorkCorrectly() {
        RecycledItemReq req = new RecycledItemReq();

        req.setItemType("Plastic");
        req.setQuantity(5);

        assertEquals("Plastic", req.getItemType());
        assertEquals(5, req.getQuantity());
    }

    @Test
    void toString_shouldContainFieldValues() {
        RecycledItemReq req = new RecycledItemReq();
        req.setItemType("Glass");
        req.setQuantity(10);

        String result = req.toString();

        assertNotNull(result);
        assertTrue(result.contains("Glass"));
        assertTrue(result.contains("10"));
    }
}

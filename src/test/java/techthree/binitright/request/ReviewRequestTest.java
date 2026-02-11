package techthree.binitright.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ReviewRequestTest {
    @Test
    void gettersAndSetters_shouldWorkCorrectly() {
        ReviewRequest request = new ReviewRequest();

        request.setStatus("APPROVED");
        request.setRemarks("Looks good");

        assertEquals("APPROVED", request.getStatus());
        assertEquals("Looks good", request.getRemarks());
    }

    @Test
    void toString_shouldContainFieldValues() {
        ReviewRequest request = new ReviewRequest();
        request.setStatus("REJECTED");
        request.setRemarks("Invalid data");

        String result = request.toString();

        assertNotNull(result);
        assertTrue(result.contains("REJECTED"));
        assertTrue(result.contains("Invalid data"));
    }

}

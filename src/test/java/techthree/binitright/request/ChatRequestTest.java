package techthree.binitright.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ChatRequestTest {
    @Test
    void gettersAndSetters_shouldWorkCorrectly() {
        ChatRequest request = new ChatRequest();

        request.setMessage("Hello AI");
        request.setUserId(10L);

        assertEquals("Hello AI", request.getMessage());
        assertEquals(10L, request.getUserId());
    }

    @Test
    void equalsAndHashCode_shouldWorkCorrectly() {
        ChatRequest r1 = new ChatRequest();
        r1.setMessage("Hi");
        r1.setUserId(1L);

        ChatRequest r2 = new ChatRequest();
        r2.setMessage("Hi");
        r2.setUserId(1L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void toString_shouldNotBeNull() {
        ChatRequest request = new ChatRequest();
        request.setMessage("Test");
        request.setUserId(5L);

        assertNotNull(request.toString());
    }
}

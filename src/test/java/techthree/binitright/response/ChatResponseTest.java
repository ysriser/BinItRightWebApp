package techthree.binitright.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChatResponseTest {
    @Test
    void allArgsConstructor_shouldSetReply() {
        ChatResponse response = new ChatResponse("Hello there");

        assertEquals("Hello there", response.getReply());
    }

    @Test
    void setterAndGetter_shouldWork() {
        ChatResponse response = new ChatResponse("Initial");
        response.setReply("Updated");

        assertEquals("Updated", response.getReply());
    }

    @Test
    void equalsAndHashCode_shouldMatchForSameValues() {
        ChatResponse r1 = new ChatResponse("Hi");
        ChatResponse r2 = new ChatResponse("Hi");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void toString_shouldContainReply() {
        ChatResponse response = new ChatResponse("Test");

        String result = response.toString();

        assertNotNull(result);
        assertTrue(result.contains("Test"));
    }
}

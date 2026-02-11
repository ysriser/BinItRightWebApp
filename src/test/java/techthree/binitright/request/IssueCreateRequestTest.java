package techthree.binitright.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IssueCreateRequestTest {
    @Test
    void shouldCreateRecord_andReturnValues() {
        IssueCreateRequest req = new IssueCreateRequest(
                "BIN_PROBLEM",
                "Bin is full",
                10L
        );

        assertEquals("BIN_PROBLEM", req.issueCategory());
        assertEquals("Bin is full", req.description());
        assertEquals(10L, req.raisedByUserId());
    }

    @Test
    void equalsAndHashCode_shouldMatchForSameValues() {
        IssueCreateRequest r1 = new IssueCreateRequest("A", "B", 1L);
        IssueCreateRequest r2 = new IssueCreateRequest("A", "B", 1L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void toString_shouldContainValues() {
        IssueCreateRequest req = new IssueCreateRequest("CAT", "DESC", 99L);

        String s = req.toString();

        assertNotNull(s);
        assertTrue(s.contains("CAT"));
        assertTrue(s.contains("DESC"));
        assertTrue(s.contains("99"));
    }
}

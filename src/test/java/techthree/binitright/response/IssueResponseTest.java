package techthree.binitright.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class IssueResponseTest {
    @Test
    void defaultConstructor_andSetter_shouldWork() {
        IssueResponse response = new IssueResponse();

        response.setIssueId(10L);

        assertEquals(10L, response.getIssueId());
    }

    @Test
    void parameterizedConstructor_shouldSetIssueId() {
        IssueResponse response = new IssueResponse(20L);

        assertEquals(20L, response.getIssueId());
    }

}

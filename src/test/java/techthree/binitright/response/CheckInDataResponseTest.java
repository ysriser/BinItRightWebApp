package techthree.binitright.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class CheckInDataResponseTest {
    @Test
    void defaultConstructor_andSetters_shouldWork() {
        CheckInDataResponse response = new CheckInDataResponse();

        response.setCheckInId(100L);
        response.setResponseCode("200");
        response.setResponseDesc("Success");

        assertEquals(100L, response.getCheckInId());
        assertEquals("200", response.getResponseCode());
        assertEquals("Success", response.getResponseDesc());
    }

    @Test
    void parameterizedConstructor_shouldSetFieldsCorrectly() {
        CheckInDataResponse response =
                new CheckInDataResponse(200L, "400", "Failed");

        assertEquals(200L, response.getCheckInId());
        assertEquals("400", response.getResponseCode());
        assertEquals("Failed", response.getResponseDesc());
    }

    @Test
    void toString_shouldContainFieldValues() {
        CheckInDataResponse response =
                new CheckInDataResponse(1L, "201", "Created");

        String result = response.toString();

        assertNotNull(result);
        assertTrue(result.contains("201"));
        assertTrue(result.contains("Created"));
    }
}

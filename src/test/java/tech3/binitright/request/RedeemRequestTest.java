package tech3.binitright.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class RedeemRequestTest {
    @Test
    void defaultConstructor_andSetters_shouldWork() {
        RedeemRequest request = new RedeemRequest();

        request.setUserId(1L);
        request.setAccessoriesId(10L);

        assertEquals(1L, request.getUserId());
        assertEquals(10L, request.getAccessoriesId());
    }

    @Test
    void parameterizedConstructor_shouldSetFieldsCorrectly() {
        RedeemRequest request = new RedeemRequest(2L, 20L);

        assertEquals(2L, request.getUserId());
        assertEquals(20L, request.getAccessoriesId());
    }
}

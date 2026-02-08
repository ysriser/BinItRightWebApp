package tech3.binitright.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DistanceCalculatorTest {

    @Test
    void samePointDistanceIsZero() {
        final double d = DistanceCalculator.calculateDistance(1.3000, 103.8000, 1.3000, 103.8000);
        assertEquals(0.0, d, 0.0001);
    }

    @Test
    void distanceIsSymmetric() {
        final double d1 = DistanceCalculator.calculateDistance(1.3000, 103.8000, 1.3050, 103.8050);
        final double d2 = DistanceCalculator.calculateDistance(1.3050, 103.8050, 1.3000, 103.8000);
        assertEquals(d1, d2, 0.0001);
    }

    @Test
    void nearbyPointsReturnReasonableMeters() {
        final double d = DistanceCalculator.calculateDistance(1.3000, 103.8000, 1.3005, 103.8005);
        assertTrue(d > 50.0);
        assertTrue(d < 100.0);
    }
}
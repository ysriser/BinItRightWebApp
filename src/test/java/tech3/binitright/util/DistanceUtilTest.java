package tech3.binitright.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistanceUtilTest {

    @Test
    void samePointDistanceIsZero() {
        final double distance = DistanceUtil.distanceInMeters(1.3000, 103.8000, 1.3000, 103.8000);
        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    void distanceIsSymmetric() {
        final double d1 = DistanceUtil.distanceInMeters(1.3000, 103.8000, 1.3050, 103.8050);
        final double d2 = DistanceUtil.distanceInMeters(1.3050, 103.8050, 1.3000, 103.8000);
        assertEquals(d1, d2, 0.0001);
    }

    @Test
    void fartherTargetHasLargerDistance() {
        final double nearDistance = DistanceUtil.distanceInMeters(1.3000, 103.8000, 1.3002, 103.8002);
        final double farDistance = DistanceUtil.distanceInMeters(1.3000, 103.8000, 1.3200, 103.8200);

        assertTrue(farDistance > nearDistance);
        assertTrue(nearDistance > 0.0);
    }
}

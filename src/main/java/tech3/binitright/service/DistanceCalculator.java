package tech3.binitright.service;

public class DistanceCalculator {

    private static final double EARTHURADIUS = 6371000; // meters

    public static double calculateDistance(
            final double lat1, final double lng1,
            final double lat2, final double lng2) {

        final double dLat = Math.toRadians(lat2 - lat1);
        final double dLng = Math.toRadians(lng2 - lng1);

        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTHURADIUS * c;
    }
}

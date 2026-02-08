package tech3.binitright.util;

public final class DistanceUtil {

    private static final double EARTHURADIUS = 6371000; // meters

    public static double distanceInMeters(
            final double userlat, final double userlon,
            final double binlat, final double binlon) {

        final double dLat = Math.toRadians(binlat - userlat);
        final double dLon = Math.toRadians(binlon - userlon);

        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userlat))
                * Math.cos(Math.toRadians(binlat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTHURADIUS * c;
    }
}
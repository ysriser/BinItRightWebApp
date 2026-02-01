package tech3.binitright.util;

public class DistanceUtil {

    private static final double EARTH_RADIUS = 6371000; // meters

    public static double distanceInMeters(
            double userlat, double userlon,
            double binlat, double binlon) {

        double dLat = Math.toRadians(binlat - userlat);
        double dLon = Math.toRadians(binlon - userlon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userlat))
                * Math.cos(Math.toRadians(binlat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
package com.firebase.geofire.util;

public class GeoUtils {

    public static double distance(double lat1, double long1, double lat2, double long2) {
        // Earth's mean radius in meters
        final double radius = (Constants.EARTH_EQ_RADIUS + Constants.EARTH_POLAR_RADIUS)/2;
        double latDelta = Math.toRadians(lat1 - lat2);
        double lonDelta = Math.toRadians(long1 - long2);

        double a = (Math.sin(latDelta/2)*Math.sin(latDelta/2)) +
                   (Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2)) *
                           Math.sin(lonDelta/2) * Math.sin(lonDelta/2));
        return radius * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public static double distanceToLatitudeDegrees(double distance) {
        return distance/Constants.METERS_PER_DEGREE_LATITUDE;
    }

    public static double distanceToLongitudeDegrees(double distance, double latitude) {
        double radians = Math.toRadians(latitude);
        double numerator = Math.cos(radians) * Constants.EARTH_EQ_RADIUS * Math.PI / 180;
        double denominator = 1/Math.sqrt(1 - Constants.EARTH_E2*Math.sin(radians)*Math.sin(radians));
        double deltaDegrees = numerator*denominator;
        if (deltaDegrees < Constants.EPSILON) {
            return distance > 0 ? 360 : distance;
        } else {
            return Math.min(360, distance/deltaDegrees);
        }
    }

    public static double wrapLongitude(double longitude) {
        if (longitude >= -180 && longitude <= 180) {
            return longitude;
        }
        double adjusted = longitude + 180;
        if (adjusted > 0) {
            return (adjusted % 360.0) - 180;
        } else {
            return 180 - (-adjusted % 360);
        }
    }

    public static boolean coordinatesValid(double latitude, double longitude) {
        return (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180);
    }

    public static void checkCoordinates(double latitude, double longitude) {
        if (!GeoUtils.coordinatesValid(latitude, longitude)) {
            throw new IllegalArgumentException("Not a valid geo location: " + latitude + ", " + longitude);
        }
    }
}
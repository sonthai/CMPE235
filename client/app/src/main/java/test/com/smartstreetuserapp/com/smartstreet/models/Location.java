package test.com.smartstreetuserapp.com.smartstreet.models;

/**
 * Created by sonthai on 2/28/16.
 */
public class Location {
    private double lat;
    private double lon;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
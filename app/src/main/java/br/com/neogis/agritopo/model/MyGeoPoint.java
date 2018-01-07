package br.com.neogis.agritopo.model;

import android.location.Location;

import org.osmdroid.util.GeoPoint;

import flexjson.JSONSerializer;

/**
 * Created by carlo on 22/10/2017.
 */

public class MyGeoPoint extends GeoPoint {
    public MyGeoPoint() {
        super(0.0, 0.0, 0.0);
    }

    public MyGeoPoint(int aLatitudeE6, int aLongitudeE6) {
        super(aLatitudeE6, aLongitudeE6);
    }

    public MyGeoPoint(int aLatitudeE6, int aLongitudeE6, int aAltitude) {
        super(aLatitudeE6, aLongitudeE6, aAltitude);
    }

    public MyGeoPoint(double aLatitude, double aLongitude) {
        super(aLatitude, aLongitude);
    }

    public MyGeoPoint(double aLatitude, double aLongitude, double aAltitude) {
        super(aLatitude, aLongitude, aAltitude);
    }

    public MyGeoPoint(Location aLocation) {
        super(aLocation);
    }

    public MyGeoPoint(GeoPoint aGeopoint) {
        super(aGeopoint);
    }

    public String toString() {
        JSONSerializer serializer = new JSONSerializer();

        serializer.prettyPrint(true);
        return serializer.serialize(this);
    }
}

package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Deren Lei on 5/23/2017.
 */

public class LocationModel {
    private String locationName;
    private String locationId;
    private LatLng locationCoordinate;
    private Bitmap locationImage;

    public LocationModel(String locationName, String locationId, LatLng locationCoordinate) {
        this.locationName = locationName;
        this.locationId = locationId;
        this.locationCoordinate = locationCoordinate;
        this.locationImage = null; // no image
    }

    // Constructor used for if the location has an image the user has taken.
    public LocationModel(String locationName, String locationId, LatLng locationCoordinate, Bitmap locationImage) {
        this.locationName = locationName;
        this.locationId = locationId;
        this.locationCoordinate = locationCoordinate;
        this.locationImage = locationImage;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Bitmap getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(Bitmap locationImage) {
        this.locationImage = locationImage;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public LatLng getLocationCoordinate() {
        return locationCoordinate;
    }

    public void setLocationCoordinate(LatLng locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }
}

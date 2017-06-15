package edu.ucsb.cs.cs190i.derenlei.GauchoMap.models;

/**
 * Created by Zichen Sun on 9/6/2017.
 */


public class GauchoMap {

    public String Name;
    public String Uri;
    public String Event;
    public Double Longitude;
    public Double Latitude;
    public String Time;


    public GauchoMap() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public GauchoMap(String Uri, String Event,Double Latitude,Double Longitude, String Time, String Name) {
        this.Uri = Uri;
        this.Event = Event;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Time = Time;
        this.Name = Name;
    }

}
// [END blog_user_class]

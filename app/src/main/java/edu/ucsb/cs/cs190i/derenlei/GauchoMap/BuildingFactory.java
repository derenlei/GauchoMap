package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Deren Lei on 5/23/2017.
 */

public class BuildingFactory {
    public static ArrayList<LocationModel> buildings;
    public static ArrayList<LocationModel> createLocationsFromJSON(String response) {
        buildings = new ArrayList<>();
        try {
            JSONObject jsonobject = new JSONObject(response);
            JSONArray jsonArray = jsonobject.getJSONArray("results");
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                String name = jsonObject.getString("name");
                JSONObject geomJSONObject = jsonObject.getJSONObject("geometry");
                JSONObject locationJSONObject = geomJSONObject.getJSONObject("location");
                double latitude = locationJSONObject.getDouble("lat");
                double longitude = locationJSONObject.getDouble("lng");
                LatLng coordinates = new LatLng(latitude, longitude);
                String id = jsonObject.getString("place_id");
                LocationModel building = new LocationModel(name, id, coordinates);
                buildings.add(building);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return buildings;
    }

    public static LocationModel createLocationFromJSON(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String name = jsonObject.getString("name");
        JSONObject locationJSONObject = jsonObject.getJSONObject("location");
        double latitude = locationJSONObject.getDouble("latitude");
        double longitude = locationJSONObject.getDouble("longitude");
        LatLng coordinates = new LatLng(latitude, longitude);
        String description = jsonObject.getString("description");
        LocationModel location = new LocationModel(name, description, coordinates);
        return location;
    }
}


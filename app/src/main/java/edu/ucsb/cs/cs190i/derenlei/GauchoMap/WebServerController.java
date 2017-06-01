package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Created by Deren Lei on 5/23/2017.
 */

public class WebServerController {
    private static RequestQueue queue = null;
    private static final String NAME = "name";
    private static final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=34.4140,-119.8489&radius=500&key=AIzaSyChQJFJOlllWIGylBRzDAbSdOnsP_8Dno0";
    public static ArrayList<LocationModel> locations;
    public static BuildingFactory buildingFactory = new BuildingFactory();

    public static void retrieveLocations(Context context) {
        if (queue == null) queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        buildingFactory.createLocationsFromJSON(response);
                        locations = buildingFactory.buildings;
                        MapsActivity.locations = locations;
                        //for (LocationModel location : locations) {
                         //   Log.d("Locations",Integer.toString(locations.size()));
                        //}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        });
        queue.add(stringRequest);
        queue.start();
    }

}

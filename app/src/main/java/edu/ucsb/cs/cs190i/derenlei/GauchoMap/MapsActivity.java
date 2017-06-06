package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.string.yes;
import static edu.ucsb.cs.cs190i.derenlei.GauchoMap.R.id.map;
import static edu.ucsb.cs.cs190i.derenlei.GauchoMap.R.id.title;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,GoogleMap.OnMarkerClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnInfoWindowClickListener, ResultCallback<Status> {

    public static boolean loggedIn = false;
    private GoogleMap mMap = null;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private Marker placesMarker;
    private LocationRequest mLocationRequest;
    public static ArrayList<LocationModel> locations;
    private WebServerController webServerController;
    private List<Geofence> mGeofenceList = new ArrayList<>();
    private PendingIntent mGeofencePendingIntent;
    private Circle geoFenceLimits;
    private CameraPosition cameraPosition;
    private String markerId;
    private OkHttpClient client = new OkHttpClient();

    private static final String DETAIL_END = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final String PLACES_KEY = "AIzaSyChQJFJOlllWIGylBRzDAbSdOnsP_8Dno0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if(!loggedIn) {
//            loggedIn = true;
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setContentView(R.layout.activity_maps);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        locations = new ArrayList<>();
        //webServerController = new WebServerController();
        //WebServerController.retrieveLocations(this);
//        SupportMapFragment mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
//        mf.getMapAsync(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (!inListView && id == R.id.list_view) {
//            inListView = true;
//
//            if(listFrag==null) {
//                listFrag = new ListFragment();
//            }
//            getFragmentManager().beginTransaction().replace(R.id.fragment_container, listFrag).commit();
//            //Log.d("LIST","going to list view");
//        }
//        else if (inListView && id == R.id.grid_view) {
//            inListView = false;
//
//            if(cFragment==null){
//                cFragment = new CalendarFragment();
//            }
//            getFragmentManager().beginTransaction().replace(R.id.fragment_container, cFragment).commit();
//            TextView upload = (TextView)findViewById(R.id.upload);
//            upload.setVisibility(View.INVISIBLE);
//            //Log.d("GRID","going to grid view");
//        }
//        else if (id==R.id.logout){
//            Intent intent = new Intent(this, LoginActivity.class);
//            this.startActivity(intent);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng UCSB = new LatLng(34.4140, -119.8489);
        if (cameraPosition != null) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));;
        }
        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCSB, 15));
        }
        mMap.setOnMapLoadedCallback(this);
        //locations = WebServerController.locations;
    }


    @Override
    public void onMapLoaded() {
        /*
        for (LocationModel i : locations) {
            LatLng latLng = i.getLocationCoordinate();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            String name = i.getLocationName();
            markerOptions.title(name);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            placesMarker = mMap.addMarker(markerOptions);
            placesMarker.setTag(i.getLocationId());
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(name)
                    .setCircularRegion(
                            i.getLocationCoordinate().latitude,
                            i.getLocationCoordinate().longitude,
                            25
                    )
                    .setExpirationDuration(60 * 60 * 1000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
        */


        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng position) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MapsActivity.this);
                }
                builder.setTitle("Add Event")
                        .setMessage("Add event to the GauchoMap")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(position)
                                        .title("Edit this")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                                        .setDraggable(true);
                                LocationModel location = new LocationModel("Edit name", "Edit id", position);
                                locations.add(location);
                                mGeofenceList.add(new Geofence.Builder()
                                        .setRequestId("Edit this")
                                        .setCircularRegion(
                                                position.latitude,
                                                position.longitude,
                                                25
                                        )
                                        .setExpirationDuration(60 * 60 * 1000)
                                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                                Geofence.GEOFENCE_TRANSITION_EXIT)
                                        .build());
                                GeofencingRequest geofencingRequest = getGeofencingRequest();
                                addGeofence(geofencingRequest);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, mMap.getCameraPosition().zoom));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        //GeofencingRequest geofencingRequest = getGeofencingRequest();
        //addGeofence(geofencingRequest);
        //cameraPosition = mMap.getCameraPosition();
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(final Marker marker) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MapsActivity.this);
                }

                builder.setTitle("Delete event")
                        .setMessage("Delete event from GauchoMap")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                marker.remove();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub

            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        /*
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }
        */
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //move map camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,mMap.getCameraPosition().zoom));

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new
                GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        //	Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionService.class);
        //	We use FLAG_UPDATE_CURRENT so that we get the same
        //	pending	intent back	when calling addGeofences()	and
        //	removeGeofences().
        return PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addGeofence(GeofencingRequest request) {
        //if (checkPermission())
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()){
            //drawGeofence();
        }
    }

    private void drawGeofence(){
        if ( geoFenceLimits != null)
            geoFenceLimits.remove();
        for (int i = 0; i < locations.size(); i++) {
            CircleOptions circleOptions = new CircleOptions()
                    .center(locations.get(i).getLocationCoordinate())
                    .strokeColor(Color.argb(100, 150, 150, 150))
                    .radius(25);
            geoFenceLimits = mMap.addCircle(circleOptions);
        }
    }

    public static Intent makeNotificationIntent(Context geoFenceService, String msg) {
        return new Intent(geoFenceService, MapsActivity.class);
    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
        getAndGotoUrl(markerId);
    }

    protected void getAndGotoUrl(String markerPlaceId) {
        String URL = DETAIL_END + "?place_id=" + markerPlaceId + "&key=" + PLACES_KEY;
        String result = null;
        Request request = new Request.Builder()
                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, IOException e) {

                e.printStackTrace();

            }
            @Override public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String place = response.body().string();
                JSONObject jsonobject = null;
                JSONObject jsonA = null;
                String url = null;
                try {
                    jsonobject = new JSONObject(place);
                    jsonA = jsonobject.getJSONObject("result");
                    url = jsonA.getString("url");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                final String finalUrl = url;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent browserIntent = new Intent(MapsActivity.this, DetailActivity.class);
                        browserIntent.putExtra("URL", finalUrl);
                        startActivity(browserIntent);
                    }
                });
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, (String) marker.getTag(),
                Toast.LENGTH_SHORT).show();
        markerId = (String) marker.getTag();
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        if (mMap != null) {
            CameraPosition position = mMap.getCameraPosition();
            bundle.putParcelable("cameraPosition", position);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        if (bundle != null)
            cameraPosition = bundle.getParcelable("cameraPosition");


    }

}

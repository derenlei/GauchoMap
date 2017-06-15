package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static edu.ucsb.cs.cs190i.derenlei.GauchoMap.EventFragment.newInstance;
import static edu.ucsb.cs.cs190i.derenlei.GauchoMap.R.id.map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, ResultCallback<Status> {

    private static GoogleMap mMap = null;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private Marker placesMarker;
    private LocationRequest mLocationRequest;
    public static ArrayList<LocationModel> locations;
    private WebServerController webServerController;
    private static List<Geofence> mGeofenceList = new ArrayList<>();
    private PendingIntent mGeofencePendingIntent;
    private Circle geoFenceLimits;
    private CameraPosition cameraPosition;
    private String markerId;
    private LatLng targetMarkerPosition;
    private OkHttpClient client = new OkHttpClient();
    private Marker CurrentPositionMarker;
    private static final String DETAIL_END = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final String PLACES_KEY = "AIzaSyChQJFJOlllWIGylBRzDAbSdOnsP_8Dno0";
    //String used for photo
    private static final int PICK_IMAGE_REQUEST = 9876;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName;
    public final String APP_TAG = "MyCustomApp";
    //String used for database
    static public GauchomapDatabaseHelper helper = MainActivity.helper;
    static public SQLiteDatabase DB = MainActivity.DB;
    public static Boolean setMarker;
    public static String username = LoginActivity.username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMarker = false;
        setContentView(R.layout.activity_maps);
        locations = new ArrayList<>();
        //webServerController = new WebServerController();
        //WebServerController.retrieveLocations(this);
        SupportMapFragment mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mf.getMapAsync(this);
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
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCSB, 15));
        }

        if(!DatabaseFactory.is_empty()) {
            ArrayList<String> namelist = DatabaseFactory.getNamelist();
            ArrayList<LatLng> locationlist = new ArrayList<>();
            for (int i = 0; i < namelist.size(); i++) {
                String name = namelist.get(i);
                String uri = DatabaseFactory.getUri(name);
                LatLng position = new LatLng(DatabaseFactory.getLatitude(uri), DatabaseFactory.getLongitude(uri));
                locationlist.add(position);
            }

            for (int i = 0; i < namelist.size(); i++) {
                mMap.addMarker(new MarkerOptions()
                        .position(locationlist.get(i))
                        .title(namelist.get(i))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                        .setDraggable(true);
                LocationModel location = new LocationModel(namelist.get(i), DatabaseFactory.getUri(namelist.get(i)), locationlist.get(i));
                locations.add(location);
            /*
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId("name")
                    .setCircularRegion(
                            locationlist.get(i).latitude,
                            locationlist.get(i).longitude,
                            25
                    )
                    .setExpirationDuration(60 * 60 * 1000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
            GeofencingRequest geofencingRequest = getGeofencingRequest();
            addGeofence(geofencingRequest);
            */

            }
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
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng position) {
                targetMarkerPosition = position;
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MapsActivity.this);
                }
                builder.setTitle("Add Event")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Add an event to the GauchoMap!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Get event photo from Camera or gallery.
                                AlertDialog.Builder Fragmentbuilder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Fragmentbuilder = new AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    Fragmentbuilder = new AlertDialog.Builder(MapsActivity.this);
                                }

                                Fragmentbuilder.setTitle("Event Photo")
                                        //.setMessage("Choose a photo for the event")
                                        .setItems(new CharSequence[]
                                                        {"Choose From Camera", "Choose From Gallery", "Cancel"},
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // The 'which' argument contains the index position
                                                        // of the selected item
                                                        switch (which) {
                                                            case 0:
                                                                //camera
                                                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                                photoFileName = "image" + System.currentTimeMillis() + ".jpg";
                                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name
                                                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                                                                }
                                                                break;
                                                            case 1:
                                                                //Gallery
                                                                Intent galleryIntent = new Intent();
                                                                galleryIntent.setType("image/*");
                                                                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                                                photoFileName = "image" + System.currentTimeMillis() + ".jpg";
                                                                galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
                                                                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                                                                break;
                                                            case 2:
                                                                //return
                                                                break;
                                                        }
                                                    }
                                                })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();




                                //ToDo add Event Edit Fragment
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
                if (marker == mCurrLocationMarker) {return;}
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
                                DatabaseFactory.deletebyname(marker.getTitle());


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
        GauchomapDatabaseHelper.GetInstance().close();
    }

    @Override
    public void onLocationChanged(Location location) {

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
        GauchomapDatabaseHelper.GetInstance().close();
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

    private static GeofencingRequest getGeofencingRequest() {
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

        String url = DatabaseFactory.getUri(marker.getTitle());
        LatLng position = marker.getPosition();
        Double longitude = position.longitude;
        Double latitude = position.latitude;
        marker.remove();

        //this line need to be changed
        String User = username;

        EventFragment frag = newInstance(Uri.parse(url),User,longitude,latitude);
        frag.show(getSupportFragmentManager().beginTransaction(), "tag");
        //getAndGotoUrl(markerId);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        Toast.makeText(this, "Picture was taken!", Toast.LENGTH_SHORT).show();
                        Uri takenPhoto = getPhotoFileUri(photoFileName);
                        Double longitude = targetMarkerPosition.longitude;
                        Double latitude = targetMarkerPosition.latitude;

                        //this line need to be changed
                        String User = username;

                        EventFragment frag = newInstance(takenPhoto,User,longitude,latitude);
                        frag.show(getSupportFragmentManager().beginTransaction(), "tag");
                        //storephoto(takenPhoto.toString());
                    } else { // Result was a failure
                        Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    // if we are here, everything processed okay

                    if (requestCode == PICK_IMAGE_REQUEST) {
                        // if we are here, we are hearing back from image gallery
                        Uri takenPhoto = data.getData();
                        Double longitude = targetMarkerPosition.longitude;
                        Double latitude = targetMarkerPosition.latitude;

                        //this line need to be changed
                        String User = username;

                        EventFragment frag = newInstance(takenPhoto,User,longitude,latitude);
                        frag.show(getSupportFragmentManager().beginTransaction(), "tag");
                        String uri = data.getData().toString();
                        //storephoto(uri);
                    }
                }
                break;

        }
    }

    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new
                    File( getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }
            // Return the file target for the photo based on filename
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            // wrap File object into a content provider, required for API >= 24
            return FileProvider.getUriForFile(this, "com.codepath.fileprovider", file);
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GauchomapDatabaseHelper.GetInstance().close();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // Show your dialog here (this is called right after onActivityResult)
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

    public static void addMarker(LatLng position){
        String name = DatabaseFactory.getNamebyloc(position.longitude,position.latitude);
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                .setDraggable(true);
        LocationModel location = new LocationModel(name, DatabaseFactory.getUri(name), position);
        locations.add(location);
        mGeofenceList.add(new Geofence.Builder()
                .setRequestId("name")
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
        //addGeofence(geofencingRequest);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, mMap.getCameraPosition().zoom));
    }

}

package com.example.fa_olaoluwa_lawal_c0857663_android;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.fa_olaoluwa_lawal_c0857663_android.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Geocoder geocoder;
    List<Address> addresses;
    private static final int REQUEST_CODE = 1;
    private String newText = "";
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 3000;
    private static int count;
    private static final int POLYGON_SIDES = 4;
    private FusedLocationProviderClient mClient;
    private static boolean cameraSet = false;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Polyline line;
    private Bitmap newBit;
    private List<LatLng> points,polygonPoints = new ArrayList<>();
    private Marker userMarker, favMarker,lastMarker;
    private List<Marker> markerList = new ArrayList<>();
    private Polygon shape;

    String dateCreated;
    LatLng userloc;
    LatLng favloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bundle = getIntent().getExtras();
        geocoder = new Geocoder(this, Locale.getDefault());
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (!isGrantedLocationPermission()){
            requestLocationPermission();
        }
        else{
            startUpdatingLocation();


        }

//Extract the data…
        Double latitude = bundle.getDouble("latitude");
        Double longitude = bundle.getDouble("longitude");
        favloc = new LatLng(latitude,longitude);
        dateCreated = bundle.getString("date");
        Toast.makeText(MapsActivity.this,"this is longitude: "+longitude,Toast.LENGTH_LONG).show();

        // Add a marker in Sydney and move the camera
        LatLng place = new LatLng(latitude, longitude);
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address;
        if(addresses.size()==0){
            address = dateCreated;
        }
        else {
        String addressa = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        address = "" + addressa + "\n" + state + "\n" + country + "\n" + postalCode + "\n" + knownName;
        }
        favMarker = mMap.addMarker(new MarkerOptions().position(place).title(address));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,16.0f));

    }
    @SuppressLint("MissingPermission")
    private void startUpdatingLocation(){
        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult){
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                LatLng userlocation = new LatLng(location.getLatitude()
                        ,location.getLongitude());
                float[] results = new float[1];
                userloc = userlocation;
                Location.distanceBetween(userlocation.latitude, userlocation.longitude,favloc.latitude,
                        favloc.longitude,results);
                favMarker.setSnippet("distance to location is "+results[0]/1000+"Km");



            userMarker = mMap.addMarker(new MarkerOptions().position(userlocation).title("Your Current Location").snippet("I am here"));
                if(!cameraSet){
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation,16.0f));
                    cameraSet = true;
                }
                locationRequest.setSmallestDisplacement(1);
            }
        };
        mClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null
        );
    }
    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE);
    }
    private boolean isGrantedLocationPermission(){
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this).setMessage("Accessing the location is Mandatory")
                        .setPositiveButton("OK", (dialogInterface, i) -> requestPermissions(
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE
                        )).setNegativeButton("Cancel",null)
                        .create().show();
            }
            else{
                startUpdatingLocation();
            }
        }
    }
}
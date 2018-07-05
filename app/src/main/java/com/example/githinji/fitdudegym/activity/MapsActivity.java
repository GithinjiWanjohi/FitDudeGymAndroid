package com.example.githinji.fitdudegym.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.githinji.fitdudegym.R;
import com.example.githinji.fitdudegym.model.Gym;
import com.example.githinji.fitdudegym.model.GymMaps;
import com.example.githinji.fitdudegym.utils.Tools;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private static final String TAG = MapsActivity.class.getSimpleName();

    //the URL having the json data
    private static final String JSON_URL = "http://fitdude.herokuapp.com/gym";

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private GoogleMap mMap;

    SupportMapFragment mapFragment;

    // Declare a variable for the cluster manager.
    private ClusterManager<GymMaps> mClusterManager;


    private BottomNavigationView navigation;

    private ActionBar actionBar;
    private CardView cardView;

    List<Gym> gymList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_map);

        initComponent();

        initMapFragment();
        loadGymList();
    }


//    private void initComponent(String gymName, String openingTime) {
//        // get the bottom sheet view
//        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
//
//        TextView txtGymName = findViewById(R.id.gym_name);
//        TextView txtWorkHours = findViewById(R.id.work_hours);
//
//        // init the bottom sheet behavior
//        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
//
//        // change the state of the bottom sheet
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//
//        txtGymName.setText(gymName);
//        txtWorkHours.setText(openingTime);
//
//        // set callback for changes
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
//
//        ((FloatingActionButton) findViewById(R.id.fab_directions)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                try {
////                    mMap.animateCamera(zoomingLocation());
//                } catch (Exception e) {
//                }
//            }
//        });
//    }

    private void initComponent() {

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_map:
                        Intent mapsIntent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(mapsIntent);
                        navigation.setBackgroundColor(getResources().getColor(R.color.blue_grey_700));
                        return true;
                    case R.id.navigation_workout:
                        Intent workoutIntent = new Intent(getApplicationContext(), WorkoutActivity.class);
                        startActivity(workoutIntent);
                        navigation.setBackgroundColor(getResources().getColor(R.color.pink_800));
                        return true;
                    case R.id.navigation_profile:
                        Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                        startActivity(profileIntent);
                        navigation.setBackgroundColor(getResources().getColor(R.color.grey_700));
                        return true;
                }
                return false;
            }
        });
    }

    private void initMapFragment() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = Tools.configActivityMaps(googleMap);
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(-1.292066, 36.821946));
                setUpClusterer();
            }

        });
    }

    private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-1.292066, 36.821946), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<GymMaps>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
    }

    private CameraUpdate zoomingLocation(Double latitude, Double longitude) {
        return CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
    }

    private void loadGymList() {
        //getting the progressbar
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, obj.toString());

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray heroArray = obj.getJSONArray("data");
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = heroArray.getJSONObject(i);
                                    String gymName = jsonObject.getString("gym_name");
                                    final Double latitude = jsonObject.getDouble("latitude");
                                    final Double longitude = jsonObject.getDouble("longitude");
                                    String openingTime = jsonObject.getString("opening_time");
                                    String closingTime = jsonObject.getString("closing_time");
                                    String time = openingTime + " - " + closingTime;

//                                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latitude, longitude));
//                                    mMap.addMarker(markerOptions);
////                                    mMap.moveCamera(zoomingLocation(latitude, longitude));
//                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                                        @Override
//                                        public boolean onMarkerClick(Marker marker) {
//                                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                                            try {
//                                                mMap.animateCamera(zoomingLocation(latitude, longitude));
//                                            } catch (Exception e) {
//                                                Log.getStackTraceString(e);
//                                            }
//                                            return true;
//                                        }
//                                    });
////                                    initComponent(gymName, openingTime);

                                    GymMaps gymItem = new GymMaps(latitude, longitude);
                                    mClusterManager.addItem(gymItem);

                                    // Create a cluster item for the marker and set the title and snippet using the constructor.
                                    GymMaps infoWindowItem = new GymMaps(latitude, longitude, gymName, time);

                                    // Add the cluster item (marker) to the cluster manager.
                                    mClusterManager.addItem(infoWindowItem);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

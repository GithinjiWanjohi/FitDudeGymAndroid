package com.example.githinji.fitdudegym.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.githinji.fitdudegym.R;
import com.example.githinji.fitdudegym.adapter.GymAdapter;
import com.example.githinji.fitdudegym.model.Gym;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GymListActivity extends AppCompatActivity {

    private static final String TAG = GymListActivity.class.getSimpleName();

    //the URL having the json data
    private static final String JSON_URL = "http://fitdude.herokuapp.com/gym";

    //the gym list where we will store all the hero objects after parsing json
    List<Gym> gymList;

    // RecyclerView object
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_list);

        recyclerView = findViewById(R.id.gym_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing gym list
        gymList = new ArrayList<>();

        //this method will fetch and parse the data
        loadGymList();
    }

    private void loadGymList() {
        //getting the progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
                                    String latitude = jsonObject.getString("latitude");
                                    String longitude = jsonObject.getString("longitude");
                                    String openingTime = jsonObject.getString("opening_time");
                                    String closingTime = jsonObject.getString("closing_time");
                                    String rating = jsonObject.getString("rating");

                                    Gym gym = new Gym(gymName, latitude, longitude, openingTime, closingTime, rating);

                                    gymList.add(gym);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                //creating custom adapter object
                                recyclerView.setAdapter(new GymAdapter(gymList));

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

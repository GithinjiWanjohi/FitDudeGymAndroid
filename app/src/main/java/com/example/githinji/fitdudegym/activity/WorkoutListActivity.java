package com.example.githinji.fitdudegym.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.githinji.fitdudegym.R;
import com.example.githinji.fitdudegym.activity.InstructorListActivity;
import com.example.githinji.fitdudegym.adapter.InstructorAdapter;
import com.example.githinji.fitdudegym.adapter.WorkoutAdapter;
import com.example.githinji.fitdudegym.model.Instructor;
import com.example.githinji.fitdudegym.model.Workout;
import com.example.githinji.fitdudegym.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutListActivity extends AppCompatActivity {

    private static final String TAG = InstructorListActivity.class.getSimpleName();

    //the URL having the json data
    private static final String JSON_URL = "http://fitdude.herokuapp.com/user/workout/?user_id=";

    //the gym list where we will store all the hero objects after parsing json
    List<Workout> workouts;

    // RecyclerView object
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);
        recyclerView = findViewById(R.id.workout_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing gym list
        workouts = new ArrayList<>();

        initToolbar();

        //this method will fetch and parse the data
        loadWorkoutsList();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.workouts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadWorkoutsList() {
        //getting the progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        String userId = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserId().toString();

        String userURL = JSON_URL + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, userURL,
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
                                    String workoutType = jsonObject.getString("workout_type");
                                    String location = jsonObject.getString("location");
                                    String date = jsonObject.getString("date");
                                    String time = jsonObject.getString("time");
                                    String userID = jsonObject.getString("user_id");
                                    String reps = jsonObject.getString("reps");
                                    String sets = jsonObject.getString("sets");

                                    Workout workout = new Workout(workoutType, location, date, time, userID, reps, sets);

                                    workouts.add(workout);
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), R.string.failed_to_get_data, Toast.LENGTH_LONG).show();
                                    Log.d(TAG, e.getMessage());
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                //creating custom adapter object
                                recyclerView.setAdapter(new WorkoutAdapter(workouts));

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), R.string.failed_to_get_data, Toast.LENGTH_LONG).show();
                            Log.d(TAG, e.getMessage());
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        }){
//            @Override
////            public Map<String, String> getParams() {
////                HashMap<String, String> params = new HashMap<>();
////                String userId = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserId().toString();
////                params.put("user_id", userId);
////                return params;
////            }
////
////            @Override
////            public String getBodyContentType() {
////                return "application/x-www-form-urlencoded";
////            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

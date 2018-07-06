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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.githinji.fitdudegym.R;
import com.example.githinji.fitdudegym.adapter.GymAdapter;
import com.example.githinji.fitdudegym.adapter.InstructorAdapter;
import com.example.githinji.fitdudegym.model.Gym;
import com.example.githinji.fitdudegym.model.Instructor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InstructorListActivity extends AppCompatActivity {

    private static final String TAG = InstructorListActivity.class.getSimpleName();

    //the URL having the json data
    private static final String JSON_URL = "http://fitdude.herokuapp.com/instructor";

    //the gym list where we will store all the hero objects after parsing json
    List<Instructor> instructors;

    // RecyclerView object
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        recyclerView = findViewById(R.id.instructor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing gym list
        instructors = new ArrayList<>();

        initToolbar();

        //this method will fetch and parse the data
        loadInstructorList();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.registered_instructors);
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

    private void loadInstructorList() {
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
                                    String firstName = jsonObject.getString("first_name");
                                    String lastName = jsonObject.getString("last_name");
                                    String email = jsonObject.getString("email");
                                    String gymID = jsonObject.getString("gym_no");

                                    Instructor inst = new Instructor(gymID, firstName, lastName, email);

                                    instructors.add(inst);
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Failed to get data", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, e.getMessage());
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                //creating custom adapter object
                                recyclerView.setAdapter(new InstructorAdapter(instructors));

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Failed to get data", Toast.LENGTH_LONG).show();
                            Log.d(TAG, e.getMessage());
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Log.d(TAG, error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

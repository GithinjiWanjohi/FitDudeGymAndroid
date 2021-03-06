package com.example.githinji.fitdudegym.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.githinji.fitdudegym.R;
import com.example.githinji.fitdudegym.adapter.WorkoutAdapter;
import com.example.githinji.fitdudegym.model.User;
import com.example.githinji.fitdudegym.model.Workout;
import com.example.githinji.fitdudegym.utils.SharedPrefManager;
import com.example.githinji.fitdudegym.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    //the URL having the json data
    private static final String JSON_URL = "http://fitdude.herokuapp.com/user/";

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private ActionBar actionBar;
    private Toolbar toolbar;

    SharedPrefManager sharedPrefManager;

    TextView mTxtUserName, mTxtAge, mTxtGender, mTxtWeight, mTxtPrefGym;

    String fullName, firstName, lastName, age, gender, weight, prefGym;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_drawer_image);
        initToolbar();
        registerUser();
        initComponent();

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "TODO: Edit user dialog", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Tools.setSystemBarColor(this, R.color.orange_A700);
    }

    private void initComponent() {
        mTxtUserName = findViewById(R.id.txt_user_name);
        mTxtAge = findViewById(R.id.txt_age);
        mTxtWeight = findViewById(R.id.txt_weight);
        mTxtPrefGym = findViewById(R.id.txt_preferred_gym);
        mTxtGender = findViewById(R.id.txt_gender);

    }

    public void setTextViews(){

        mTxtUserName.setText(fullName);
        mTxtAge.setText(age);
        mTxtWeight.setText(weight);
        mTxtGender.setText(gender);
        mTxtPrefGym.setText(prefGym);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
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

    public void registerUser(){

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
                                    firstName = jsonObject.getString("first_name");
                                    lastName = jsonObject.getString("last_name");
//                                    email = jsonObject.getString("email");
                                    age = jsonObject.getString("age");
                                    weight = jsonObject.getString("weight");
                                    gender = jsonObject.getString("user_id");
                                    prefGym = jsonObject.getString("pref_gym");

                                    User user = new User(firstName, lastName, age, weight, gender, prefGym);

                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), R.string.failed_to_get_data, Toast.LENGTH_LONG).show();
                                    Log.d(TAG, e.getMessage());
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                setTextViews();

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

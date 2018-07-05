package com.example.githinji.fitdudegym.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.githinji.fitdudegym.R;
import com.example.githinji.fitdudegym.utils.SharedPrefManager;
import com.example.githinji.fitdudegym.utils.Tools;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class WorkoutActivity extends AppCompatActivity {

    //the URL to send json data
    private static final String JSON_URL = "http://fitdude.herokuapp.com/workout/save";

    private BottomNavigationView navigation;

    private Button spn_from_date, spn_from_time;

    String userId, workoutType, reps, sets, location, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        initComponent();
    }

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

    public void onNewWorkoutClicked(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_workout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        spn_from_date = (Button) dialog.findViewById(R.id.spn_from_date);
        spn_from_time = (Button) dialog.findViewById(R.id.spn_from_time);
        final AppCompatSpinner workoutType = dialog.findViewById(R.id.spn_workoutType);
        final AppCompatSpinner spnReps =  dialog.findViewById(R.id.spn_reps);
        final EditText location = (EditText) dialog.findViewById(R.id.et_location);
        final AppCompatSpinner spnSets = dialog.findViewById(R.id.spn_sets);


        String[] workouts = getResources().getStringArray(R.array.workout_types);
        ArrayAdapter<String> array = new ArrayAdapter<>(this, R.layout.simple_spinner_item, workouts);
        array.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        workoutType.setAdapter(array);
        workoutType.setSelection(0);

        String[] repItems = getResources().getStringArray(R.array.gym_reps);
        ArrayAdapter<String> arrayReps = new ArrayAdapter<>(this, R.layout.simple_spinner_item, repItems);
        arrayReps.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnReps.setAdapter(arrayReps);
        spnReps.setSelection(0);

        String[] setItems = getResources().getStringArray(R.array.gym_sets);
        ArrayAdapter<String> arraySets = new ArrayAdapter<>(this, R.layout.simple_spinner_item, setItems);
        arraySets.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnSets.setAdapter(arrayReps);
        spnSets.setSelection(0);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Event event = new Event();
//                event.email = tv_email.getText().toString();
//                event.name = et_name.getText().toString();
//                event.location = et_location.getText().toString();
//                event.from = spn_from_date.getText().toString() + " (" + spn_from_time.getText().toString() + ")";
//                event.to = spn_to_date.getText().toString() + " (" + spn_to_time.getText().toString() + ")";
//                event.is_allday = cb_allday.isChecked();
//                event.timezone = spn_timezone.getSelectedItem().toString();
//                displayDataResult(event);

                dialog.dismiss();
            }
        });

        spn_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerDark((Button) v);
            }
        });

        spn_from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTimePickerDark((Button) v);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void dialogDatePickerDark(final Button bt) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        spn_from_date.setText(Tools.getFormattedDateSimple(date_ship_millis));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark theme
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMaxDate(cur_calender);
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }



    private void dialogTimePickerDark(final Button bt) {
        Calendar cur_calender = Calendar.getInstance();
        TimePickerDialog datePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                spn_from_time.setText(hourOfDay + " : " + minute);
            }
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);
        //set dark light
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Timepickerdialog");
    }

//    public void registerUser(){
//        //getting the progressbar
//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//        SharedPrefManager.getInstance(getApplicationContext());
//        //making the progressbar visible
//        progressBar.setVisibility(View.VISIBLE);
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, JSON_URL,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("Response", response);
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("Error.Response", error.toString());
//                    }
//                }
//        ) {
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                params.put("user_id", firstName);
//                params.put("location", lastName);
//                params.put("workout_type", email);
//                params.put("reps", password);
//                params.put("sets", password);
//                params.put("date", password);
//                params.put("time", password);
//                return new JSONObject(params).toString().getBytes();
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(postRequest);
//    }
}

package com.example.githinji.fitdudegym.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class WorkoutActivity extends AppCompatActivity {

    private static final String TAG = WorkoutActivity.class.getSimpleName();

    //the URL to send json data
    private static final String JSON_URL = "http://fitdude.herokuapp.com/workout/save/";

    Dialog dialog;

    private BottomNavigationView navigation;

    private Button spn_from_date, spn_from_time;

    String userId, selectedWorkout, reps, sets, selectedLocation, selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        initToolbar();
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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Workouts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void onNewWorkoutClicked(View view) {
        dialog = new Dialog(this);
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

        selectedLocation = location.getText().toString();

        String[] workouts = getResources().getStringArray(R.array.workout_types);
        ArrayAdapter<String> array = new ArrayAdapter<>(this, R.layout.simple_spinner_item, workouts);
        array.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        workoutType.setAdapter(array);
        workoutType.setSelection(0);
        selectedWorkout = workoutType.getSelectedItem().toString();

        String[] repItems = getResources().getStringArray(R.array.gym_reps);
        ArrayAdapter<String> arrayReps = new ArrayAdapter<>(this, R.layout.simple_spinner_item, repItems);
        arrayReps.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnReps.setAdapter(arrayReps);
        spnReps.setSelection(0);
        reps = spnReps.getSelectedItem().toString();

        String[] setItems = getResources().getStringArray(R.array.gym_sets);
        ArrayAdapter<String> arraySets = new ArrayAdapter<>(this, R.layout.simple_spinner_item, setItems);
        arraySets.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnSets.setAdapter(arrayReps);
        spnSets.setSelection(0);
        sets = spnSets.getSelectedItem().toString();

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWorkout();
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
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
                        selectedDate = dateFormat.format(date_ship_millis);
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
                selectedTime = hourOfDay + ":" + minute;
                spn_from_time.setText(hourOfDay + " : " + minute);
            }
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);

        //set dark light
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Timepickerdialog");
    }

    public void registerWorkout(){
//        //getting the progressbar
//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
        userId = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserId().toString();
//        //making the progressbar visible
//        progressBar.setVisibility(View.VISIBLE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d(TAG, response);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.workout_saved, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, error.toString());
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.something_wrong_happened, Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", userId);
                params.put("workout_type", selectedWorkout);
                params.put("reps", reps);
                params.put("sets", sets);
                params.put("location", selectedLocation);
                params.put("date", selectedTime);
                params.put("time", selectedTime);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

    public void onViewGymsButtonClicked(View view) {
        Intent intent = new Intent(this, GymListActivity.class);
        startActivity(intent);
    }

    public void onGymInstructorsClicked(View view) {
        Intent intent = new Intent(this, InstructorListActivity.class);
        startActivity(intent);
    }

    public void onWorkoutSessionsClicked(View view) {
        Intent intent = new Intent(this, WorkoutListActivity.class);
        startActivity(intent);
    }
}

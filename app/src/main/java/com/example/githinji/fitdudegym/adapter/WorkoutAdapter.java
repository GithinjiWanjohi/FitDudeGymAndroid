package com.example.githinji.fitdudegym.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.githinji.fitdudegym.R;
import com.example.githinji.fitdudegym.model.Workout;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder>{
private List<Workout> workouts;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView mTextView;
    TextView workoutName;
    TextView location;
    TextView reps;
    TextView sets;
    TextView dateTime;
    public ViewHolder(View v) {
        super(v);

        workoutName = v.findViewById(R.id.workout_name);
        location = v.findViewById(R.id.location);
        reps = v.findViewById(R.id.reps);
        sets = v.findViewById(R.id.sets);
        dateTime = v.findViewById(R.id.date_time);
    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public WorkoutAdapter(List<Workout> workouts) {
        this.workouts = workouts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_workout, parent, false);

        WorkoutAdapter.ViewHolder vh = new WorkoutAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(WorkoutAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//            holder.mTextView.setText(gym.get(position).getGymName());
        holder.workoutName.setText(workouts.get(position).getWorkoutType());
        holder.location.setText(workouts.get(position).getLocation());
        holder.reps.setText(workouts.get(position).getReps());
        holder.sets.setText(workouts.get(position).getSets());
        String date = workouts.get(position).getDate();
        String time = workouts.get(position).getTime();
        holder.dateTime.setText(date + " " + time);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return workouts.size();
    }
}

package com.example.githinji.fitdudegym.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.githinji.fitdudegym.R;
import com.example.githinji.fitdudegym.model.Instructor;

import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder>{
private List<Instructor> instructors;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView mTextView;
    TextView firstName;
    TextView lastName;
    TextView email;
    TextView gymID;
    public ViewHolder(View v) {
        super(v);

        firstName = v.findViewById(R.id.firstName);
        lastName = v.findViewById(R.id.lastName);
        email = v.findViewById(R.id.email);
        gymID = v.findViewById(R.id.gym_id);
    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public InstructorAdapter(List<Instructor> instructors) {
        this.instructors = instructors;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InstructorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_instructor, parent, false);

        InstructorAdapter.ViewHolder vh = new InstructorAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(InstructorAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//            holder.mTextView.setText(gym.get(position).getGymName());
        holder.firstName.setText(instructors.get(position).getFirstName());
        holder.lastName.setText(instructors.get(position).getLastName());
        holder.email.setText(instructors.get(position).getEmail());
        holder.gymID.setText(instructors.get(position).getGymId());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return instructors.size();
    }
}
package com.example.githinji.fitdudegym.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.githinji.fitdudegym.R;
import com.example.githinji.fitdudegym.model.Gym;

import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.ViewHolder>{
        private List<Gym> gym;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            TextView gymTitle;
            TextView latitude;
            TextView longitude;
            TextView opening_time;
            TextView closing_time;
            TextView rating;
            public ViewHolder(View v) {
                super(v);

                gymTitle = v.findViewById(R.id.gym_name);
                latitude = v.findViewById(R.id.latitude);
                longitude = v.findViewById(R.id.longitude);
                opening_time = v.findViewById(R.id.opening_time);
                closing_time = v.findViewById(R.id.closing_time);
                rating = v.findViewById(R.id.rating);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public GymAdapter(List<Gym> gym) {
            this.gym = gym;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public GymAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_gym, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
//            holder.mTextView.setText(gym.get(position).getGymName());
            holder.gymTitle.setText(gym.get(position).getGymName());
            holder.latitude.setText(gym.get(position).getLatitude());
            holder.longitude.setText(gym.get(position).getLongitude());
            holder.opening_time.setText(gym.get(position).getOpeningTime());
            holder.closing_time.setText(gym.get(position).getClosingTime());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return gym.size();
        }
    }


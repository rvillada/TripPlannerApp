package com.codepath.tripplannerapp.fragments;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.tripplannerapp.MainActivity;
import com.codepath.tripplannerapp.NewTaskActivity;
import com.codepath.tripplannerapp.NewTripActivity;
import com.codepath.tripplannerapp.R;
import com.codepath.tripplannerapp.Task;
import com.codepath.tripplannerapp.Trip;
import com.codepath.tripplannerapp.TripAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.List;


public class ItineraryFragment extends Fragment {

    public static final String TAG = "ItineraryFragment";

    private TextView tvTripNameDetails;
    private TextView tvItinerary;


    //protected TaskAdapter adapter;
    protected List<Task> allTasks;


    public ItineraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itinerary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTripNameDetails = view.findViewById(R.id.tvTripNameDetails);
        tvItinerary = view.findViewById(R.id.tvItinerary);

        Bundle bundleItinerary = this.getArguments();
        if (bundleItinerary != null) {
            Trip trip = bundleItinerary.getParcelable("trip");
            Log.i(TAG, trip.getTripName());
            tvTripNameDetails.setText(trip.getTripName());

        }


    }

    private void queryTasks() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);

        query.include(Task.KEY_USER);

        query.setLimit(20);

        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting tasks", e);
                    return;
                }
                for (Task task : tasks) {
                    Log.i(TAG, "Task: " + task.getTaskName());
                }

                allTasks.clear();

                allTasks.addAll(tasks);
            }
        });
    }
}
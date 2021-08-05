package com.codepath.tripplannerapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private Button btnLogout;
    private Button btnNewTrip;
    private RecyclerView rvTrips;

    protected TripAdapter adapter;
    protected List<Trip> allTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        btnNewTrip = findViewById(R.id.btnNewTrip);
        rvTrips = findViewById(R.id.rvTrips);


        allTrips = new ArrayList<>();
        adapter = new TripAdapter(this, allTrips);

        rvTrips.setAdapter(adapter);
        rvTrips.setLayoutManager(new LinearLayoutManager(this));
        queryTrips();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewTripActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void queryTrips() {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);

        query.include(Trip.KEY_USER);

        query.setLimit(20);

        query.addDescendingOrder("createdAt");

        //getInBackground is used to retrieve a single item for the backend
        //find in background gets them all

        query.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> trips, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Trip trip : trips) {
                    Log.i(TAG, "Trip: " + trip.getTripName());
                }

                allTrips.clear();
                adapter.clear();

                allTrips.addAll(trips);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
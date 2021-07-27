package com.codepath.tripplannerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private Button btnLogout;
    private Button btnNewTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        btnNewTrip = findViewById(R.id.btnNewTrip);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null

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

        //queryTrips();
    }

    private void queryTrips() {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);

        //query.include(Trip.KEY_USER);

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
            }
        });
    }
}
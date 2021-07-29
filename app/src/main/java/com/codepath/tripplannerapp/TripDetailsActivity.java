package com.codepath.tripplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

public class TripDetailsActivity extends AppCompatActivity {

    Trip trip;

    // the view objects
    TextView tvTripNameDetails;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tvTripNameDetails = (TextView) findViewById(R.id.tvTripNameDetails);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        trip = (Trip) Parcels.unwrap(getIntent().getParcelableExtra("trip"));

        Log.i("TripDetailsActivity", Trip.class.getSimpleName());

        tvTripNameDetails.setText(trip.getTripName());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_itinerary:
                        // do something here
                        return true;
                    case R.id.action_map:
                        // do something here
                        return true;
                    default: return true;
                }
            }

        });

    }
}
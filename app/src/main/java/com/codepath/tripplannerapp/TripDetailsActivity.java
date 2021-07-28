package com.codepath.tripplannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.parceler.Parcels;

public class TripDetailsActivity extends AppCompatActivity {

    Trip trip;

    // the view objects
    TextView tvTripNameDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tvTripNameDetails = (TextView) findViewById(R.id.tvTripNameDetails);

        trip = (Trip) Parcels.unwrap(getIntent().getParcelableExtra(Trip.class.getSimpleName()));

        tvTripNameDetails.setText(trip.getTripName());

    }
}
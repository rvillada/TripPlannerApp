package com.codepath.tripplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

public class TripDetailsActivity extends AppCompatActivity {

    Trip trip;

    // the view objects
    private TextView tvTripNameDetails;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tvTripNameDetails = findViewById(R.id.tvTripNameDetails);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        trip = (Trip) Parcels.unwrap(getIntent().getParcelableExtra("trip"));

        Log.i("TripDetailsActivity", Trip.class.getSimpleName());

        tvTripNameDetails.setText(trip.getTripName());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_itinerary:
                        Toast.makeText(TripDetailsActivity.this, "Itinerary!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_map:
                        Toast.makeText(TripDetailsActivity.this, "Map!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }

        });

    }
}
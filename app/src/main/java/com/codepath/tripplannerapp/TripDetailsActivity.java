package com.codepath.tripplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.tripplannerapp.fragments.ItineraryFragment;
import com.codepath.tripplannerapp.fragments.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.Map;

public class TripDetailsActivity extends AppCompatActivity {

    Trip trip;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        trip = (Trip) Parcels.unwrap(getIntent().getParcelableExtra("trip"));


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_itinerary:
                        Toast.makeText(TripDetailsActivity.this, "Itinerary!", Toast.LENGTH_SHORT).show();

                        fragment = new ItineraryFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("trip", trip);
                        fragment.setArguments(bundle);
                        break;
                    case R.id.action_map:
                    default:
                        Toast.makeText(TripDetailsActivity.this, "Map!", Toast.LENGTH_SHORT).show();
                        fragment = new MapFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }

        });
        bottomNavigationView.setSelectedItemId(R.id.action_itinerary);

    }
}
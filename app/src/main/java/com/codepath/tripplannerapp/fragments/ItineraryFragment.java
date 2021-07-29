package com.codepath.tripplannerapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.tripplannerapp.R;
import com.codepath.tripplannerapp.Trip;

import org.parceler.Parcels;


public class ItineraryFragment extends Fragment {

    private TextView tvTripNameDetails;


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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Trip trip = bundle.getParcelable("trip");
            tvTripNameDetails.setText(trip.getTripName());
        }

        //trip = (Trip) Parcels.unwrap(.getParcelableExtra("trip"));

        //tvTripNameDetails.setText(trip.getTripName());
    }
}
package com.codepath.tripplannerapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.tripplannerapp.LoginActivity;
import com.codepath.tripplannerapp.MainActivity;
import com.codepath.tripplannerapp.R;
import com.codepath.tripplannerapp.Trip;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment {

    public static final String TAG = "MapFragment";

    //widgets
    private EditText etInputSearch;

    private GoogleMap myMap;

    private static final float DEFAULT_ZOOM = 15f;

    private Button btnAddItinerary;
    private Button btnMakeItinerary;

    private List<Address> searchedAddresses = new ArrayList<>();
    private List<Address> itineraryAddresses = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);




        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // i added this
                myMap = googleMap;
                // When map is loaded

                Bundle bundleMap = MapFragment.this.getArguments();
                if (bundleMap != null) {
                    Trip trip = bundleMap.getParcelable("trip");
                    Log.i(TAG, "Hey im here " + trip.getTripName());
                    geoLocate(trip.getTripName(), false );
                }

                etInputSearch = getActivity().findViewById(R.id.etInputSearch);
                init();

                btnAddItinerary = getActivity().findViewById(R.id.btnAddItinerary);

                btnAddItinerary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Address mostRecentAddress = searchedAddresses.get(searchedAddresses.size() - 1);
                        makeMarker(new LatLng(mostRecentAddress.getLatitude(), mostRecentAddress.getLongitude()), mostRecentAddress.getAddressLine(0), "blue");
                        itineraryAddresses.add(mostRecentAddress);
                    }
                });

                btnMakeItinerary = getActivity().findViewById(R.id.btnMakeItinerary);
                btnMakeItinerary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent i = new Intent(getActivity(), ItineraryFragment.);
//                        startActivity(i);
//                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.flContainer, new ItineraryFragment(), "NewFragment TAG");
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
                        //makeMatrix(itineraryAddresses);

                        Log.i(TAG, String.valueOf(itineraryAddresses));
                        makeMatrix(itineraryAddresses);


                    }
                });



//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(LatLng latLng) {
//                        // When clicked on map
//                        // Initialize marker options
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        // Set position of marker
//                        markerOptions.position(latLng);
//                        // Set title of marker
//                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
//                        //Remove all marker
//                        googleMap.clear();
//                        //Animating to zoom the marker
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                                latLng, 10
//                        ));
//                        // Add marker on map
//                        googleMap.addMarker(markerOptions);
//                    }
//                });
            }
        });

        //why cant init go here?
        return view;
    }

    private void init() {

        Log.d(TAG, "init: initializing " + etInputSearch);
        etInputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.getAction() == event.ACTION_DOWN
                || event.getAction() == event.KEYCODE_ENTER) {

                    String searchString = etInputSearch.getText().toString();
                    // execute our method for searching
                    geoLocate(searchString, true);
                }
                return false;
            }
        });

        hideKeyboard();
    }

    private void geoLocate(String searchString, Boolean pinBool) {
        // i should be passing in a context here... vid passed in MapActivity.this
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IO Exception: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            searchedAddresses.add(address);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0), pinBool);
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title, Boolean pinBool) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (pinBool) {
            makeMarker(latLng, title, "red");
        }
    }

    private void makeMarker(LatLng latLng, String title, String color) {
        MarkerOptions options = new MarkerOptions();
        if (color == "blue") {
            options.position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(title);
        } else {
            options.position(latLng)
                    .title(title);
        }
        myMap.addMarker(options);
        hideKeyboard();
    }

    private void makeMatrix(List<Address> itineraryAddresses) {
        // probably did not need to pass in itineraryAddresses as a parameter since I defined it globally
        int matrixDim = itineraryAddresses.size();
        Log.i(TAG, String.valueOf(itineraryAddresses.size()));

        double[][] distancesArray = new double[matrixDim][matrixDim];

        for (int i = 0; i < itineraryAddresses.size(); i++) {
            for (int j = 0; j < itineraryAddresses.size(); j++) {
                if (i == j) {
                    distancesArray[i][j] = 0;
                    Log.i(TAG, String.valueOf(distancesArray[i][j]));
                } else {
                    Address address1 = itineraryAddresses.get(i);
                    Address address2 = itineraryAddresses.get(j);

//                    Log.i(TAG, address1.getAddressLine(0));
//                    Log.i(TAG, address2.getAddressLine(0));

                    LatLng latLng1 = new LatLng(address1.getLatitude(), address1.getLongitude());
                    LatLng latLng2 = new LatLng(address2.getLatitude(), address2.getLongitude());

                    double distance = SphericalUtil.computeDistanceBetween(latLng1, latLng2);


                    distancesArray[i][j] = distance;
                    Log.i(TAG, String.valueOf(distancesArray[i][j]));


                }
            }
        }
        Log.i(TAG, String.valueOf(distancesArray));



    }

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}



// am i updating myMap correctly? outside of moving the camera , am i doing it right? why wasnt it returning california's coordinates?
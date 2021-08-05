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
import com.codepath.tripplannerapp.Node;
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
import java.util.Collections;
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
                myMap = googleMap;

                // When map is loaded

                // Unwrapping trip object passed over from the TripDetailsActivity
                Bundle bundleMap = MapFragment.this.getArguments();
                if (bundleMap != null) {
                    Trip trip = bundleMap.getParcelable("trip");
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

                        Log.i(TAG, String.valueOf(itineraryAddresses));
                        double[][] distancesArray = makeMatrix(itineraryAddresses);

                        List<List<Integer>> allLocationOrderings = new ArrayList<List<Integer>>();

                        List<Integer> list = new ArrayList<>();

                        for (int i = 0; i < itineraryAddresses.size(); i++) {
                            list.add(i);
                        }

                        permuteInts(list, 0, allLocationOrderings);

                        List<Integer> answer = smallestDistance(allLocationOrderings, distancesArray);
                        Log.i(TAG, "Shortest path list: " + String.valueOf(answer));

                    }
                });
            }
        });

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

                    etInputSearch.getText().clear();
                }
                return false;
            }
        });
        hideKeyboard();
    }

    private void geoLocate(String searchString, Boolean pinBool) {
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

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }




    // Each element in allLocationOrderings is a list that represents an ordering of locations. It returns the ordering of locations that requires the least amount of travel (if you travel from the location at the first index to the location at the second index, to the location at the third index, etc).
    // The distancesArray holds the distances between all locations in our itinerary (more explanation in makeMatrix function)
    private List<Integer> smallestDistance(List<List<Integer>> allLocationOrderings, double[][] distancesArray) {

        // This will hold the distances for all possible orderings of locations
        List<Double> distances = new ArrayList<>();

        // Go through each list in our bigList. Determine how long the path is given the ordering of locations specified by this list. Add the distance to our distances list.
        for (int i = 0; i < allLocationOrderings.size(); i++) {
            List<Integer> currentPermutation = allLocationOrderings.get(i);
            double distance = findPathDistance(currentPermutation, distancesArray);
            distances.add(distance);
        }

        // We would like the ordering of locations with the smallest total distance
        double min = Collections.min(distances);
        Log.i(TAG, "Path with minimum distance has length: " + String.valueOf(min));

        int index = distances.indexOf(min);
        return allLocationOrderings.get(index);
    }

    // Takes in an ordering of locations represented by a list of integers. References the distances array to determine how long the path between locations is given the ordering.
    private double findPathDistance(List<Integer> locationOrder, double[][] distancesArray) {
        double totalPathDistance = 0;
        int numLocations = locationOrder.size();
        for (int i = 1; i < numLocations; i++) {
            int row = locationOrder.get(i-1);
            int col = locationOrder.get(i);
            totalPathDistance = totalPathDistance + distancesArray[row][col];
        }
        return totalPathDistance;
    }

    private double[][] makeMatrix(List<Address> itineraryAddresses) {
        int matrixDim = itineraryAddresses.size();

        double[][] distancesArray = new double[matrixDim][matrixDim];

        for (int i = 0; i < itineraryAddresses.size(); i++) {
            for (int j = 0; j < itineraryAddresses.size(); j++) {
                if (i == j) {
                    distancesArray[i][j] = 0;
                } else {
                    Address address1 = itineraryAddresses.get(i);
                    Address address2 = itineraryAddresses.get(j);

                    LatLng latLng1 = new LatLng(address1.getLatitude(), address1.getLongitude());
                    LatLng latLng2 = new LatLng(address2.getLatitude(), address2.getLongitude());

                    double distance = SphericalUtil.computeDistanceBetween(latLng1, latLng2);


                    distancesArray[i][j] = distance;
                }
            }
        }
        return distancesArray;
    }


    private void permuteInts(List<Integer> nums, int index, List<List<Integer>> bigList) {
        if (index == nums.size()) {
            List l = new ArrayList(nums.size());
            for (int num : nums)
                l.add(num);
            bigList.add(l);
            return;
        }
        for (int i = index; i < nums.size(); i++) {
            swap(nums, i, index);
            permuteInts(nums, index + 1, bigList);
            swap(nums, i, index);
        }
    }

    private void swap(List<Integer> nums, int i, int index) {
        int temp = nums.get(i);
        nums.set(i, nums.get(index));
        nums.set(index, temp);

    }

}

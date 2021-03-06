package com.codepath.tripplannerapp;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Date;

@Parcel(analyze = Trip.class)
@ParseClassName("Trip")
public class Trip extends ParseObject {

    public static final String KEY_TRIP_NAME = "tripName";
    public static final String KEY_TRIP_IMAGE = "tripImage";
    public static final String KEY_USER = "user";
    public static final String KEY_TRIP_START = "tripStart";
    public static final String KEY_TRIP_END = "tripEnd";

    public Trip() {}

    public String getTripName() {
        return getString(KEY_TRIP_NAME);
    }

    public void setTripName(String tripName) {
        put(KEY_TRIP_NAME, tripName);
    }

    public ParseFile getTripImage() {
        return getParseFile(KEY_TRIP_IMAGE);
    }

    public void setTripImage(ParseFile parseFile) {
        put(KEY_TRIP_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setTripStart(Date tripStart) { put(KEY_TRIP_START, tripStart); }

    public Date getTripStart() {return getDate(KEY_TRIP_START);}

    public void setTripEnd(Date tripEnd) { put (KEY_TRIP_END, tripEnd); }

    public Date getTripEnd() { return getDate(KEY_TRIP_END); }
}

package com.codepath.tripplannerapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register our parse models
        ParseObject.registerSubclass(Trip.class);
        ParseObject.registerSubclass(Task.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("FsG3wUZsr0BwtIW8pSlrpLqcnIssCEf49yqfc8Ni")
                .clientKey("BVtcVZ2XelPYqQfjNu0kMjbzuWqpyXTvPzRGlldL")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

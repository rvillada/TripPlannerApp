package com.codepath.tripplannerapp;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("o5uxHoawTSTOGQWsUApBQUdv5WqQfSsLNAlWzdyR")
                .clientKey("Md5pIyUhiP1J4WPrnE3iKZXrQCUJJfHvAAh9j1Th")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

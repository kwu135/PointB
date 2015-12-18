package com.example.sophmore.pointb;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Sophmore on 12/13/2015.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}

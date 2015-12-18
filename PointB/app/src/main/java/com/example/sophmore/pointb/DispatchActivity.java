package com.example.sophmore.pointb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

/**
 * DispatchActivity
 *
 * First activity which sends the user to either the sign in/sign up activity or the main activity depending on
 * whether or not you are logged in
 *
 */
public class DispatchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, InitialActivity.class));
        }
        finish();
    }
}

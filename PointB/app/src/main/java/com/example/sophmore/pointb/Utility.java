package com.example.sophmore.pointb;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Utility
 *
 * Static class used to call a function to hide the keyboard
 */
public class Utility {
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}

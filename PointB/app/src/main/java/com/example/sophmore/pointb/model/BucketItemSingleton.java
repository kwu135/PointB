package com.example.sophmore.pointb.model;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sophmore.pointb.MainActivity;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * BucketItemSingleton
 *
 * Used to store the single instance of a user's bucketlist throughout the app
 *
 * @note Because this singleton has an instance of the mainactivity, I used it to call functions from the DataManager to the MainActivity
 *
 */
public class BucketItemSingleton {

    private ArrayList<BucketItem> mBucketList;
    private static BucketItemSingleton sBucketItemSingleton;
    private MainActivity activity = null;

    private BucketItemSingleton() {
        super();
        mBucketList = new ArrayList<BucketItem>();
    }

    private BucketItemSingleton(MainActivity activity) {
        mBucketList = new ArrayList<BucketItem>();
        this.activity = activity;
    }

    public static BucketItemSingleton getInstance() {
        if (sBucketItemSingleton == null) {
            sBucketItemSingleton = new BucketItemSingleton();
        }
        return sBucketItemSingleton;
    }

    public static BucketItemSingleton getInstance(MainActivity activity) {
        if (sBucketItemSingleton == null) {
            sBucketItemSingleton = new BucketItemSingleton(activity);
        }
        if (sBucketItemSingleton != null && sBucketItemSingleton.activity == null) {
            sBucketItemSingleton.activity = activity;
        }
        return sBucketItemSingleton;
    }


    //Does not belong here
    public void updateUserInfo() {
        if (activity != null) {
            activity.updateUserInfo();
        }
    }

    public void attachListViewToFAB(ListView lw) {
        if (activity != null) {
            activity.attachListViewToFAB(lw);
        }
    }

    public void updateSearchedUserInfo(ParseUser user) {
        if (activity != null) {
            activity.updateSearchedUserInfo(user);
        }
    }

    public void updateSearchedBucketList(ArrayList<BucketItem> bucketList) {
        if (activity != null) {
            activity.updateSearchedBucketList(bucketList);
        }
    }

    public void makeMainToast(String s) {
        if (activity != null) {
            Toast.makeText(activity.getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }
    public void clean() {
        sBucketItemSingleton = null;
    }

    public ArrayList<BucketItem> getmBucketList() {
        return mBucketList;
    }

    public void setmBucketList(ArrayList<BucketItem> mBucketList) {
        this.mBucketList = mBucketList;
        if (activity != null) {
            activity.updateUserListTab();
        }
    }
}

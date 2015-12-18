package com.example.sophmore.pointb.model;

import android.support.annotation.StringDef;
import android.util.Log;

import com.example.sophmore.pointb.MainActivity;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DataManager
 *
 * Static Class that calls Parse Cloud Functions and sends UI updates whenever the function is done
 */
public class DataManager {
    public static void getUserBucketList() {
        final ArrayList<BucketItem> bucketList = new ArrayList<BucketItem>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());
        ParseCloud.callFunctionInBackground("getUserItems", params, new FunctionCallback<ArrayList<ParseObject>>() {
            @Override
            public void done(ArrayList<ParseObject> object, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < object.size(); i++) {
                        bucketList.add(new BucketItem(object.get(i)));
                    }
                    BucketItemSingleton.getInstance().setmBucketList(bucketList);
                    fetchUser();
                } else {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }

    public static void createItem(String title) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("title", title);
        ParseCloud.callFunctionInBackground("createItem", params, new FunctionCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    HashMap<String, String> params2 = new HashMap<String, String>();
                    params2.put("itemId", object.getObjectId());
                    params2.put("userId", ParseUser.getCurrentUser().getObjectId());
                    ParseCloud.callFunctionInBackground("addItemToUser", params2, new FunctionCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser object, ParseException e) {
                            if (e == null) {
                                getUserBucketList();
                            } else {
                                Log.d("Error", e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }

    public static void fetchUser() {
        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    BucketItemSingleton.getInstance().updateUserInfo();
                }
            }
        });
    }

    public static void removeItemFromUser(String itemId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());
        params.put("itemId", itemId);
        ParseCloud.callFunctionInBackground("removeItemFromUser", params, new FunctionCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    getUserBucketList();
                } else {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }

    public static void userFinishedItem(String itemId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());
        params.put("itemId", itemId);
        ParseCloud.callFunctionInBackground("userFinishedItem", params, new FunctionCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    getUserBucketList();
                } else {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }

    public static void searchForUser(String username) {
        final ArrayList<BucketItem> bucketList = new ArrayList<BucketItem>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        ParseCloud.callFunctionInBackground("userByUsername", params, new FunctionCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    if (object != null) {
                        BucketItemSingleton.getInstance().updateSearchedUserInfo(object);
                        HashMap<String, String> params2 = new HashMap<String, String>();
                        params2.put("userId", object.getObjectId());
                        ParseCloud.callFunctionInBackground("getUserItems", params2, new FunctionCallback<ArrayList<ParseObject>>() {
                            @Override
                            public void done(ArrayList<ParseObject> object, ParseException e) {
                                if (e == null) {
                                    for (int i = 0; i < object.size(); i++) {
                                        bucketList.add(new BucketItem(object.get(i)));
                                    }
                                    BucketItemSingleton.getInstance().updateSearchedBucketList(bucketList);
                                } else {
                                    Log.d("Error", e.getMessage());
                                }
                            }
                        });
                    } else {
                        BucketItemSingleton.getInstance().makeMainToast("Could not find user!");
                    }
                } else {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }
}

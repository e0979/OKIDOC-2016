package com.colmenadeideas.okidoc.includes.config.libs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.colmenadeideas.okidoc.includes.config.CommonKeys;
import com.colmenadeideas.okidoc.includes.config.LoginActivity;

import java.util.HashMap;

public class User { //Session Manager

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "OkidocPreferences";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "loggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ROLE = "role";

    //Constructor
    public User(Context context) {
        this._context = context;
        pref =  _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void set (String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public void set (String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    //Get Stored Preferences
    public HashMap<String, String> get(String key, String value){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(key, pref.getString(key, null));
        return user;
    }

    public void checkSession(String currentActivity) {

        if(!this.activeSession()) {

            if (currentActivity != "LoginActivity") {
                //not logged in, redirect
                Intent i = new Intent(_context, LoginActivity.class);
                //Close All Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Add new flag
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //Start Login Activity
                _context.startActivity(i);
            }
            Log.d("Session", "not logged");
        } else {
            Log.d("Session", "is logged in");
        }

    }
    // Get Login State
    public boolean activeSession(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    public String getCurrentPractice(){
        return pref.getString(CommonKeys.KEY_PRACTICE_ID, null);
    }

    public String getEmail(){
        return pref.getString(CommonKeys.KEY_EMAIL, null);
    }
    //this is also username
    public String getTempKey() { return pref.getString(CommonKeys.KEY_TEMP_FORM_KEY, null); }

    public String getRole(){
        return pref.getString(KEY_ROLE, null);
    }

    public String getUid() { return pref.getString(CommonKeys.KEY_UID, null);}

    public String getName(){
        return pref.getString(KEY_NAME, null);
    }

    public void logoutUser(String currentActivity){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        if (currentActivity != "LoginActivity") {
            // After logout redirect user to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }

    }


}

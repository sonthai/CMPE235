package test.com.smartstreetuserapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.HashMap;
/**
 * Created by Ashutosh on 4/28/2016.
 * Maintaining user session
 *
 */
public class SessionHandler {

    SharedPreferences sharedPreferences;
    Context contxt;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "SmartStreet";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "isLoggedIn";

    // User name (
    public static final String KEY_USERNAME = "userName";

    // Email address
    public static final String KEY_EMAIL = "email";

    public SessionHandler(Context context) {
        this.contxt = context;
        sharedPreferences = contxt.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public SessionHandler() {

    }

    // create login session for user
    public void LoginManager(String userName) {
        editor.putBoolean(IS_LOGIN, true).apply();
        editor.putString(KEY_USERNAME, userName).apply();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }

    // check login status for user
    public void checkLoginStatus() {
        // Check login status
        if (!this.isLoggedIn()) {
            Intent i = new Intent(contxt, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            contxt.startActivity(i);

        }
        else
            return;


    }

    //get Store session value
    public HashMap<String, String> userDetails() {
        HashMap<String, String> hashmap = new HashMap<String, String>();
        hashmap.put(KEY_USERNAME,sharedPreferences.getString(KEY_USERNAME,null));

        return hashmap;
    }

    //logout
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(contxt, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        contxt.startActivity(i);
    }

}

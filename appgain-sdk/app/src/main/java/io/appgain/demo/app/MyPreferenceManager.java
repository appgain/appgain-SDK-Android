package io.appgain.demo.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.gson.Gson;


import io.appgain.demo.MainActivity;
import io.appgain.sdk.Model.User;


public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    public SharedPreferences pref;

    // Editor for Shared preferences
    public SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "sdk_demo_app_data";





    // All Shared Preferences Keys
    private static final String KEY_USER= "KEY_USER";
    private static final String KEY_CONF_KEYS= "KEY_CONF_KEYS";



    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public User getUser() {
        String user  = pref.getString(KEY_USER , null);
        if (user == null){
            return null;
        }else {
            return  new Gson().fromJson(user , User.class) ;
            return null;
        }
    }


    public void setUser( User user) {
        editor.putString(KEY_USER , new Gson().toJson(user)) ;
        editor.commit();
    }

    public Keys getKeys() {
        String user  = pref.getString(KEY_CONF_KEYS , null);
        if (user == null){
            return null;
        }else {
            return  new Gson().fromJson(user , Keys.class) ;
        }
        return null;
    }


    public void setKeys( Keys keys) {
        editor.putString(KEY_CONF_KEYS, new Gson().toJson(keys)) ;
        editor.commit();
    }

    public void clear(boolean restart) {
        editor.clear().commit() ;
       if (!restart)return;
        Intent intent = new Intent(_context, MainActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(cn);
        _context.startActivity(mainIntent);
    }


}

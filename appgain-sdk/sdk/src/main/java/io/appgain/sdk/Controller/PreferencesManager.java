package io.appgain.sdk.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Model.User;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 * class for saving necessary data internal in preferences mange
 */
   final class PreferencesManager {
   private String TAG = PreferencesManager.class.getSimpleName();

   // Shared Preferences
   SharedPreferences pref;

   // Editor for Shared preferences
   SharedPreferences.Editor editor;

   // Context
   Context _context;

   // Shared pref mode
   int PRIVATE_MODE = 0;

   // Shared pref file name
   private static final String PREF_NAME = "Appgain";


   // All Shared Preferences Keys
   private  final String CREDENTIALS_KEY = "SDKKeys" ;
   private  final String APP_ID = "APP_ID" ;
   private  final String API_KEY = "API_KEY" ;
   private  final String USER_KEY = "USER_KEY" ;
   private  final String FIRST_RUN = "FIRST_RUN" ;
   private  final String NOTIFICATION_STATUS = "NOTIFICATION_STATUS" ;


    public PreferencesManager(Context _context) {
        if (_context ==null){
            throw new RuntimeException(" please  user Appgain.setContext() or initialize your Appgain SDK first ") ;
        }
    this._context = _context;
    pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    editor = pref.edit();
   }


    public String getApiKey() {
        return  pref.getString(API_KEY,null) ;
    }

    public void saveApiKey(String apiKey) {
        editor.putString(API_KEY , apiKey) ;
        editor.commit();
    }


    public void saveAppId(String appId) {
       editor.putString(APP_ID , appId) ;
       editor.commit();
    }
    public String getAppId() {
        return  pref.getString(APP_ID,null) ;
    }





    public SDKKeys getAppGainCredentials() {
        String value = pref.getString(CREDENTIALS_KEY,null);
        SDKKeys appGainCredentials =null ;
        if (value!=null){
            appGainCredentials =  new Gson().fromJson(value , SDKKeys.class);
        }
        if (appGainCredentials!=null)
            Timber.tag("getAppGainCredentials").e(appGainCredentials.toString());
        return appGainCredentials;
   }

   public void saveAppGainCredentials(SDKKeys appGainCredentials) {
      String value  = new Gson().toJson(appGainCredentials);
      editor.putString(CREDENTIALS_KEY, value) ;
      editor.commit();
   }


    public User getUserProvidedData() {
        String value = pref.getString(USER_KEY,null);
        User  user =null ;
        if (value!=null){
            user =  new Gson().fromJson(value , User.class);
            if (user.getUsername()==null || user.getPassword()==null || user.getEmail()==null){
                return  null ;
            }else {
                return user ;
            }
        }
        return null;
    }

    public void saveUserProvidedData(User userProvidedData) {
        String value  = new Gson().toJson(userProvidedData);
        editor.putString(USER_KEY, value) ;
        editor.commit();
        Timber.e("saved user in pref " + userProvidedData.toString());
    }

    public void saveId(String id) {
        Timber.e("saveId " + id);
       User user = getUserProvidedData();
       if (user !=null){
           user.setId(id);
       }else {
           user = new User() ;
           user.setId(id);
       }
        saveUserProvidedData(user);
    }


    public String getUserId() {

        String value = pref.getString(USER_KEY,null);
        User  user =null ;
        if (value!=null) {
            user = new Gson().fromJson(value, User.class);
            if (user.getId() !=null){
                return user.getId() ;
            }else {
                return  null ;
            }
        }else {
            return  null ;
        }
    }



    public void saveFirstRun() {
       editor.putBoolean(FIRST_RUN,false) ;
       editor.commit();
    }

    public boolean isFirstRun() {
        return pref.getBoolean(FIRST_RUN, true) ;
    }

    public  void clear(){
        editor.clear().commit();
    }

    public void saveNotificationStatus(boolean apppush) {
        editor.putBoolean(NOTIFICATION_STATUS,apppush) ;
        editor.commit();
    }
    public boolean getNotificationStatus() {
        return pref.getBoolean(NOTIFICATION_STATUS, true) ;
    }


}

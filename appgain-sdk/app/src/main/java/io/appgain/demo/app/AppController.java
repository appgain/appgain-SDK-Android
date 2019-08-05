package io.appgain.demo.app;

import android.app.Application;

import io.appgain.sdk.Controller.Appgain;

//import com.google.firebase.FirebaseApp;
//import com.google.firebase.iid.FirebaseInstanceId;
//

import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.User;
import timber.log.Timber;


/**
 * Created by developers@appgain.io on 2/13/2018.
 */

public class AppController extends Application {

    public static final boolean DEBUG_MODE  =false;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this ;
        Appgain.enableLog();
    }



    MyPreferenceManager myPreferenceManager  ;
    public MyPreferenceManager getPrefManager() {
        if (myPreferenceManager == null) {
            myPreferenceManager = new MyPreferenceManager(this);
        }
        return myPreferenceManager;
    }
    public static MyPreferenceManager getMyPreferenceManager() {
        return getInstance().getPrefManager();
    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public static void saveUser(User user) {
        getMyPreferenceManager().setUser(user) ;
    }


    public static User getUser() {
        return getMyPreferenceManager().getUser() ;
    }


    public static void saveConfiguration(String api_key, String app_id, String ParseppID ,  String serverName ,  boolean io) {
        Keys keys = new Keys(app_id ,  api_key ,ParseppID , serverName , io) ;
        Config.io = io ;

        Config.API_URL =!Config.io ? "https://api.appgain.it/" : "https://api.appgain.io/"  ;
        Config.APPS_URL =Config.API_URL+"apps/" ;
        Config.APPGAIN_IO =!Config.io ? ".appgain.it/"  : ".appgain.io/";

        Timber.e(Config.io+" io ");
        Timber.e(Config.API_URL);
    }


    public static Keys getKeys() {
        return getMyPreferenceManager().getKeys();
    }

    public static void saveKeys(Keys keys) {
        getMyPreferenceManager().setKeys(keys);
    }
}

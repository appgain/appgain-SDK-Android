package io.appgain.demo.app;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import io.appgain.demo.DUMMY;
import io.appgain.demo.MainActivity;
import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.User;
import io.appgain.sdk.interfaces.AppgainSDKInitCallBack;


/**
 * Created by developers@appgain.io on 2/13/2018.
 */

public class AppController extends Application {

    public static final boolean DEBUG_MODE  =false;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this ;
        Appgain.enableLog();
        if (AppController.getKeys()!=null)
        userNameInit(AppController.getUser());
    }

    private void userNameInit(User user) {
        MainActivity.sdk_inti = false ;
        MainActivity.showLoading(true);
        Appgain.setContext(this);
        Appgain.clear();
        try {
            Appgain.initialize(
                    getApplicationContext(),
                    AppController.getKeys().getApp_id(),
                    AppController.getKeys().getApi_key(),
                    user,
                    new AppgainSDKInitCallBack() {
                        @Override
                        public void onSuccess() {
                            MainActivity.sdk_inti = true ;
                            MainActivity.showLoading(false);
                        }

                        @Override
                        public void onFail(BaseResponse failure) {
                            MainActivity.sdk_inti = false ;
                            MainActivity.showLoading(false);
                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
            MainActivity.sdk_inti = false ;
            MainActivity.showLoading(false);
        }
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


    public static void saveConfiguration(String api_key, String app_id, boolean io) {
        AppController.saveKeys(new Keys(api_key,app_id,io));

        DUMMY.APP_ID = app_id;
        DUMMY.APP_API_KEY = api_key;
        Config.io = io ;

        Config.API_URL =!Config.io ? "https://api.appgain.it/" : "https://api.appgain.io/"  ;
        Config.APPS_URL =Config.API_URL+"apps/" ;
        Config.APPGAIN_IO =!Config.io ? ".appgain.it/"  : ".appgain.io/";

    }


    public static Keys getKeys() {
        return getMyPreferenceManager().getKeys();
    }

    public static void saveKeys(Keys keys) {
        getMyPreferenceManager().setKeys(keys);
    }
}

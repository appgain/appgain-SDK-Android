package io.appgain.demo;

import android.app.Application;

import io.appgain.sdk.Controller.Appgain;


/**
 * Created by developers@appgain.io on 2/13/2018.
 */

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Appgain.enableLog();
    }

}

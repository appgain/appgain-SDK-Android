package com.appgain.sdk;

import android.app.Application;

import io.appgain.sdk.Controller.AppGain;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.User;
import io.appgain.sdk.interfaces.InitListener;

/**
 * Created by Sotra on 2/13/2018.
 */

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
            // anonymous inti
            AppGain.initialize(getApplicationContext(), DUMMY.APP_ID, DUMMY.APP_API_KEY, new InitListener() {
                @Override
                public void onSuccess() {
                    MainActivity.sdk_inti = true ;
                }

                @Override
                public void onFail(BaseResponse failure) {
                    MainActivity.sdk_inti = false ;

                }
            });
//            AppGain.initialize(getApplicationContext() ,DUMMY.APP_ID  , DUMMY.APP_API_KEY , new User("sotra" , "1234" , "sotra@gmail.com"));
    }

}

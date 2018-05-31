package io.appgain.PushNotfication;

import android.content.Intent;

/**
 * Created by developers@appgain.io on 3/22/2018.
 */


/**
 * AppGainPushConversion class made to access to recordPushStatus  API in CONVERSION case
 */


public class AppGainPushConversion {

    /**
     *
     * @param intent  > intent from push receive method
     *  recordConvertion() recordPushStatus  access recordPushStatus()  with CONVERSION case
     *
     */
    public  static  void recordConvertion(Intent intent , RecordPushStatusCallback recordPushStatusCallback){
        AppgainAppPushApi.recordPushStatus(AppGainPushReceiver.CONVERSION, intent , recordPushStatusCallback ) ;
    }
}

package io.appgain.sdk.PushNotfication;

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
    public  static  void recordConvertion(Intent intent , RecordPushStatusCallback recordPushStatusCallback) throws Exception {
        AppgainAppPushApi.recordPushStatus(AppGainPushReceiver.CONVERSION, intent , recordPushStatusCallback ) ;
    }

    public  static  void recordConvertion(Intent intent , String internalUserId,  RecordPushStatusCallback recordPushStatusCallback) throws Exception {
        AppgainAppPushApi.recordPushStatus(AppGainPushReceiver.CONVERSION ,  intent , internalUserId, recordPushStatusCallback ) ;
    }
}

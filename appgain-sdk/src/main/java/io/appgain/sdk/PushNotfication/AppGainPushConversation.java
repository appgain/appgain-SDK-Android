package io.appgain.sdk.PushNotfication;

import android.content.Intent;
import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse;
import timber.log.Timber;

/**
 * Created by Sotra on 3/22/2018.
 */

public class AppGainPushConversation {
    public  static  void recordConversation(Intent intent , RecordPushStatusListener recordPushStatusListener){
        PushApi.recordPushStatus(AppGainPushReceiver.CONVERSION, intent , recordPushStatusListener ) ;
    }
}

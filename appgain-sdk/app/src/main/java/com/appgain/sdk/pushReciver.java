package com.appgain.sdk;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import io.appgain.sdk.Controller.AppGain;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.PushNotfication.AppGainPushConversation;
import io.appgain.sdk.PushNotfication.AppGainPushReceiver;
import io.appgain.sdk.PushNotfication.ReceiveStatus;
import io.appgain.sdk.PushNotfication.RecordPushStatusListener;
import timber.log.Timber;

/**
 * Created by Sotra on 3/15/2018.
 */

public class pushReciver extends AppGainPushReceiver {

    @Override
    protected void onReceive(Context context, ReceiveStatus receiveStatus, Intent intent) {
        // testing conversiation
        if (intent !=null )
        AppGainPushConversation.recordConversation(intent, new RecordPushStatusListener() {
            @Override
            public void onSuccess(@Nullable BaseResponse response) {
                Timber.e("onSuccess " + response.toString());
            }

            @Override
            public void onFail(@Nullable BaseResponse failure) {
                Timber.e("onFail " + failure.toString());

            }
        });
        Timber.e(intent.toString());
    }
}

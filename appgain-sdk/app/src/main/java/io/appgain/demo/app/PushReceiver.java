package io.appgain.demo.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.PushNotfication.AppGainPushConversion;
import io.appgain.sdk.PushNotfication.AppGainPushReceiver;
import io.appgain.sdk.PushNotfication.ReceiveStatus;
import io.appgain.sdk.PushNotfication.RecordPushStatusCallback;

/**
 * Created by developers@appgain.io on 3/15/2018.
 */

public class PushReceiver extends AppGainPushReceiver {

    @Override
    protected void onReceive(Context context, ReceiveStatus receiveStatus, Intent intent) {
        // testing conversiation
        if (intent !=null ) {
            try {
                AppGainPushConversion.recordConvertion(intent, new RecordPushStatusCallback() {
                    @Override
                    public void onSuccess(@Nullable BaseResponse response) {
                        Log.e("AppGainPushReceiver","onSuccess " + response.toString());
                    }

                    @Override
                    public void onFail(@Nullable BaseResponse failure) {
                        Log.e("AppGainPushReceiver","onFail " + failure.toString());

                    }
                });
                Log.e("AppGainPushReceiver",intent.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

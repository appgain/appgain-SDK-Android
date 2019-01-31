package io.appgain.demo.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.PushNotfication.AppGainPushConversion;
import io.appgain.sdk.PushNotfication.AppGainPushReceiver;
import io.appgain.sdk.PushNotfication.ReceiveStatus;
import io.appgain.sdk.PushNotfication.RecordPushStatusCallback;
import timber.log.Timber;

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
                        Timber.e("onSuccess " + response.toString());
                    }

                    @Override
                    public void onFail(@Nullable BaseResponse failure) {
                        Timber.e("onFail " + failure.toString());

                    }
                });
                Timber.e(intent.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

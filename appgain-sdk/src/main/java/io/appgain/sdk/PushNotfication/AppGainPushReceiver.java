package io.appgain.sdk.PushNotfication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;

import io.appgain.sdk.Controller.AppGain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SdkKeys;
import io.appgain.sdk.interfaces.Auth2Listener;
import timber.log.Timber;

import static io.appgain.sdk.PushNotfication.ReceiveStatus.dismiss;
import static io.appgain.sdk.PushNotfication.ReceiveStatus.open;
import static io.appgain.sdk.PushNotfication.ReceiveStatus.receive;

/**
 * Created by Sotra on 2/17/2018.
 */

public abstract class AppGainPushReceiver extends ParsePushBroadcastReceiver {

    public static final String RECEIVED = "received";
    public static final String OPEN = "open";
    public static final String CONVERSION = "conversion";
    public static final String DISMISSED = "dismissed";

    protected abstract void onReceive(Context context, ReceiveStatus receiveStatus, Intent intent);

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Timber.e("onReceive" + intent.getExtras());
    }

    @Override
    protected void onPushReceive(Context context, final Intent intent) {
        super.onPushReceive(context, intent);
        onReceive(context , receive , intent);
        PushApi.recordPushStatus(RECEIVED, intent, new RecordPushStatusListener() {
            @Override
            public void onSuccess(@Nullable BaseResponse response) {
                Timber.e(response.toString());

            }

            @Override
            public void onFail(@Nullable BaseResponse failure) {
                Timber.e(failure.toString());

            }
        });
        Timber.e("onPushReceive" + intent.getExtras());
    }

    @Override
    protected void onPushDismiss(Context context, final Intent intent) {
        super.onPushDismiss(context, intent);
        onReceive(context , dismiss , intent);
        PushApi.recordPushStatus(DISMISSED, intent, new RecordPushStatusListener() {
            @Override
            public void onSuccess(@Nullable BaseResponse response) {
                Timber.e(response.toString());

            }

            @Override
            public void onFail(@Nullable BaseResponse failure) {
                Timber.e(failure.toString());

            }
        });
        Timber.e("onPushReceive" + intent.getExtras());        Timber.e("onPushDismiss" + intent.getExtras());
    }

    @Override
    protected void onPushOpen(Context context, final Intent intent) {
        super.onPushOpen(context, intent);
        onReceive(context , open , intent);
        PushApi.recordPushStatus(OPEN, intent, new RecordPushStatusListener() {
            @Override
            public void onSuccess(@Nullable BaseResponse response) {

            }

            @Override
            public void onFail(@Nullable BaseResponse failure) {

            }
        });
    }


}

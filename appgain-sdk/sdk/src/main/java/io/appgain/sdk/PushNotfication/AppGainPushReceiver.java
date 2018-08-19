package io.appgain.sdk.PushNotfication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.parse.ParsePushBroadcastReceiver;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Utils.Utils;
import timber.log.Timber;

import static io.appgain.sdk.PushNotfication.ReceiveStatus.dismiss;
import static io.appgain.sdk.PushNotfication.ReceiveStatus.open;
import static io.appgain.sdk.PushNotfication.ReceiveStatus.receive;
/**
 * Created by developers@appgain.io on 2/17/2018.
 */


/**
 * abstract  deliver  Parse push notification  ,  ReceiveStatus  and recordPushStatus on Appgain server
 *  should be extend by user in parse push  service receiver   class
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

    /**
     * called when push  notification received
     */
    @Override
    protected void onPushReceive(Context context, final Intent intent) {
        super.onPushReceive(context, intent);
        onReceive(context , receive , intent);
        try {
            AppgainAppPushApi.recordPushStatus(RECEIVED, intent, new RecordPushStatusCallback() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * called when push  notification dismiss
     */
    @Override
    protected void onPushDismiss(Context context, final Intent intent) {
        super.onPushDismiss(context, intent);
        onReceive(context , dismiss , intent);
        try {
            AppgainAppPushApi.recordPushStatus(DISMISSED, intent, new RecordPushStatusCallback() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * called when push notification open
     */
    @Override
    protected void onPushOpen(Context context, final Intent intent) {
        super.onPushOpen(context, intent);
        onReceive(context , open , intent);
        try {
            AppgainAppPushApi.recordPushStatus(OPEN, intent, new RecordPushStatusCallback() {
                @Override
                public void onSuccess(@Nullable BaseResponse response) {
                    Timber.e(response.toString());

                }

                @Override
                public void onFail(@Nullable BaseResponse failure) {
                    Timber.e(failure.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




 @Override
    protected Notification getNotification(Context context, Intent intent) {
        return Utils.getParseNotification(context, intent);
    }
}

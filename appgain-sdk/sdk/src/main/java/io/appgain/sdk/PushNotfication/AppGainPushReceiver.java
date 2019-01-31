package io.appgain.sdk.PushNotfication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.parse.ParsePushBroadcastReceiver;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.PushNotfication.OverKeyGuardActivities.GIFActivity;
import io.appgain.sdk.PushNotfication.OverKeyGuardActivities.WebViewActivity;
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
        if(isReleaseNotificationType(intent)){
            super.onPushReceive(context, intent);
            onReceive(context , receive , intent);
        }else {
            startPushActivity(context, intent);
        }
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

    private boolean isReleaseNotificationType(Intent intent) {
        PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(intent.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        if (pushDataReciveModel.getType()==null){
            return  true ;
        }
        return false;
    }

    private void startPushActivity(Context context, Intent intent) {
        PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(intent.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        switch (pushDataReciveModel.getType()){
            case PushDataReceiveModel.WEB_VIEW_TYPE :
                if (pushDataReciveModel.getUrl() != null)
                WebViewActivity.start(context,pushDataReciveModel.getUrl(),true ,  pushDataReciveModel.getOrientation());
                break;
            case PushDataReceiveModel.WEB_VIEW_HTML_TYPE :
                if (pushDataReciveModel.getHtml() != null)
                    WebViewActivity.start(context,pushDataReciveModel.getHtml(),false , pushDataReciveModel.getOrientation());
                break;
            case PushDataReceiveModel.GIF_TYPE :
                if (pushDataReciveModel.getUrl() != null)
                    GIFActivity.start(context,pushDataReciveModel.getUrl());
                break;
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





}

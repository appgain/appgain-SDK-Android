package io.appgain.sdk.Utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import android.support.v4.app.NotificationCompat;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.appgain.sdk.Model.BaseResponse;

import static android.content.ContentValues.TAG;
import static com.parse.ParsePushBroadcastReceiver.KEY_PUSH_DATA;
import static com.parse.ParsePushBroadcastReceiver.PROPERTY_PUSH_ICON;

/**
 * Created by developers@appgain.io on 2/13/2018.
 */

public class Utils {

    public static final String KEY_PUSH_DATA = "com.parse.Data";
    private static final int MALL_NOTIFICATION_MAX_CHARACTER_LIMIT = 38;

    /**
     * getAppGainFailure convert Retrofit not  successful  response to BaseResponse Model
     */
    public static BaseResponse getAppGainFailure(String error){
        return  new Gson().fromJson(error, BaseResponse.class)    ;
    }
    public  static Notification  getParseNotification( Context context , Intent intent){
        JSONObject pushData = getPushData(intent);
        if (pushData == null || (!pushData.has("alert") && !pushData.has("title"))) {
            return null;
        }

        String title = pushData.optString("title", getDisplayName(context));
        String alert = pushData.optString("alert", "Notification received.");
        String tickerText = String.format(Locale.getDefault(), "%s: %s", title, alert);

        Bundle extras = intent.getExtras();

        Random random = new Random();
        int contentIntentRequestCode = random.nextInt();
        int deleteIntentRequestCode = random.nextInt();

        // Security consideration: To protect the app from tampering, we require that intent filters
        // not be exported. To protect the app from information leaks, we restrict the packages which
        // may intercept the push intents.
        String packageName = context.getPackageName();

        Intent contentIntent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_OPEN);
        contentIntent.putExtras(extras);
        contentIntent.setPackage(packageName);

        Intent deleteIntent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_DELETE);
        deleteIntent.putExtras(extras);
        deleteIntent.setPackage(packageName);

        PendingIntent pContentIntent = PendingIntent.getBroadcast(context, contentIntentRequestCode,
                contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pDeleteIntent = PendingIntent.getBroadcast(context, deleteIntentRequestCode,
                deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // The purpose of setDefaults(Notification.DEFAULT_ALL) is to inherit notification properties
        // from system defaults
        NotificationCompat.Builder parseBuilder = new NotificationCompat.Builder(context,getDisplayName(context));
        parseBuilder.setContentTitle(title)
                .setContentText(alert)
                .setTicker(tickerText)
                .setSmallIcon(getSmallIconId(context, intent))
                .setLargeIcon(getLargeIcon(context, intent))
                .setContentIntent(pContentIntent)
                .setDeleteIntent(pDeleteIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);
        if (alert != null
                && alert.length() > MALL_NOTIFICATION_MAX_CHARACTER_LIMIT) {
            parseBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(alert));
        }
        return parseBuilder.build();
    }

    private static Bitmap getLargeIcon(Context context, Intent intent) {
        return null ;
    }

    private static int getSmallIconId(Context context, Intent intent) {
        Bundle metaData = getApplicationMetadata(context);
        int explicitId = 0;
        if (metaData != null) {
            explicitId = metaData.getInt(PROPERTY_PUSH_ICON);
        }
        return explicitId != 0 ? explicitId : getIconId(context);
    }

    public static Bundle getApplicationMetadata(Context context) {
        ApplicationInfo info = getApplicationInfo(context, PackageManager.GET_META_DATA);
        if (info != null) {
            return info.metaData;
        }
        return null;
    }


    private static ApplicationInfo getApplicationInfo(Context context, int flags) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), flags);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


    /**
     * Returns the default icon id used by this application, as specified by the android:icon
     * attribute in the <application> element of the manifest.
     */
    public static int getIconId(Context  context) {
        return  context.getApplicationInfo().icon;

    }


    private static JSONObject getPushData(Intent intent) {
        try {
            return new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
        } catch (JSONException e) {
            Log.e(TAG, "Unexpected JSONException when receiving push data: ", e);
            return null;
        }
    }


    /**
     * Returns the display name of the app used by the app launcher, as specified by the android:label
     * attribute in the <application> element of the manifest.
     */
    public static String getDisplayName(Context context) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        return  context.getPackageManager().getApplicationLabel(appInfo).toString();
    }

    private static ExecutorService executorService;
    public static ExecutorService getExecutorService(){
        if(executorService == null){
            int cpuNum = Runtime.getRuntime().availableProcessors();
            executorService = Executors.newFixedThreadPool(cpuNum);
        }
        return executorService;
    }
}

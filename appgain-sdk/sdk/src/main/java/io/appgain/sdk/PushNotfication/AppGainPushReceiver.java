package io.appgain.sdk.PushNotfication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.parse.ManifestInfo;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.PushNotfication.OverKeyGuardActivities.GIFActivity;
import io.appgain.sdk.PushNotfication.OverKeyGuardActivities.VideoActivity;
import io.appgain.sdk.PushNotfication.OverKeyGuardActivities.WebViewActivity;
import io.appgain.sdk.R;
import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
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
        if(!isAdvancePush(intent)){
            if (pushHaveText(intent))
            super.onPushReceive(context, intent);
            onReceive(context , receive , intent);
        }else if (isImageType(intent)){
            releaseNotficationImage(context , intent);
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

    protected  boolean pushHaveText(Intent intent){
        PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(intent.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        if (pushDataReciveModel ==null || TextUtils.isEmpty(pushDataReciveModel.getAlert())){
            return  false ;
        }
        return true;
    }


    public void releaseNotficationImage(final  Context context , final Intent intent){
        GetImageRequest.imageRequest(intent, new GetImageRequest.Callback() {
            @Override
            public void onSucess(Bitmap bitmap) {
                final NotificationCompat.Builder notificationBuilder = getImageNotification(context, intent , bitmap);

                Notification notification = null;
                if (notificationBuilder != null) {
                    notification = notificationBuilder.build();
                }

                if (notification != null) {
                    ParseNotificationManager.getInstance().showNotification(context, notification);
                }
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isImageType(Intent intent) {
        PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(intent.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        if (TextUtils.isEmpty(pushDataReciveModel.getImageUrl()) || !pushDataReciveModel.getType().equals(PushDataReceiveModel.IMG)){
            return  false ;
        }
        return true;
    }
    private boolean isUrlNotificationType(Intent intent) {
        PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(intent.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        if (TextUtils.isEmpty(pushDataReciveModel.getUrl())){
            return  false ;
        }
        return true;
    }
    private boolean isAdvancePush(Intent intent) {
        PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(intent.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        if (pushDataReciveModel.getType()==null){
            return  false ;
        }
        return true;
    }

    private void startPushActivity(Context context, Intent intent) {
        PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(intent.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        switch (pushDataReciveModel.getType()){
            case PushDataReceiveModel.WEB_VIEW_TYPE :
                if (pushDataReciveModel.getAttachment() != null)
                WebViewActivity.start(context,pushDataReciveModel.getAttachment(),true ,  pushDataReciveModel.getOrientation(),pushDataReciveModel.getCall2action());
                break;
            case PushDataReceiveModel.WEB_VIEW_HTML_TYPE :
                if (pushDataReciveModel.getAttachment() != null)
                    WebViewActivity.start(
                            context,
                            pushDataReciveModel.getAttachment(),
                            false ,
                            pushDataReciveModel.getOrientation(),
                            pushDataReciveModel.getCall2action()
                    );
                break;
            case PushDataReceiveModel.GIF_TYPE :
                if (pushDataReciveModel.getAttachment() != null)
                    GIFActivity.start(
                            context,
                            pushDataReciveModel.getAttachment(),
                            pushDataReciveModel.getCall2action()
                    );
                break;
            case PushDataReceiveModel.VIDEO_TYPE :
                if (pushDataReciveModel.getAttachment() != null)
                    VideoActivity.start(
                            context,
                            pushDataReciveModel.getAttachment(),
                            pushDataReciveModel.getOrientation(),
                            pushDataReciveModel.getCall2action()
                    );
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
        if (isUrlNotificationType(intent)){
            openNotification(intent , context);
        }else {
            super.onPushOpen(context, intent);
        }
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

    private  void openNotification(Intent data , Context context){
        PushDataReceiveModel notificationEntity = new Gson().fromJson(data.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        if (notificationEntity!=null && !TextUtils.isEmpty(notificationEntity.getUrl())){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(notificationEntity.getUrl().trim()));
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            if (canResolveIntent(i,context))
                context.startActivity(i);
        }
    }
    boolean canResolveIntent(Intent intent,Context context){
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            return true;
        }
        return  false;
    }


    /**
     * Creates a {@link Notification} with reasonable defaults. If "alert" and "title" are
     * both missing from data, then returns {@code null}. If the text in the notification is longer
     * than 38 characters long, the style of the notification will be set to
     * {@link Notification.BigTextStyle}.
     * <p/>
     * As a security precaution, developers overriding this method should be sure to set the package
     * on notification {@code Intent}s to avoid leaking information to other apps.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return The notification builder to be displayed.
     * @see ParsePushBroadcastReceiver#onPushReceive(Context, Intent)
     */
    @Nullable
    protected NotificationCompat.Builder getImageNotification(Context context, Intent intent , Bitmap image) {
        JSONObject pushData = getPushData(intent);
        if (pushData == null || (!pushData.has("alert") && !pushData.has("title"))) {
            return null;
        }

        String title = pushData.optString("title", ManifestInfo.getDisplayName(context));
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

        Intent contentIntent = getContentIntent(extras, packageName);

        Intent deleteIntent = getDeleteIntent(extras, packageName);

        PendingIntent pContentIntent = PendingIntent.getBroadcast(context, contentIntentRequestCode,
                contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pDeleteIntent = PendingIntent.getBroadcast(context, deleteIntentRequestCode,
                deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = getNotificationChannel(context, intent);
            createNotificationChannel(context, notificationChannel);
            channelId = notificationChannel.getId();
        }

        //we can ignore the fact that the channel Id might be null, it is fine for versions prior to O
        @SuppressWarnings("ConstantConditions")
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId);
        notificationBuilder.setContentTitle(title)
                .setContentText(alert)
                .setTicker(tickerText)
                .setSmallIcon(this.getSmallIconId(context, intent))
                .setLargeIcon(this.getLargeIcon(context, intent))
                .setContentIntent(pContentIntent)
                .setDeleteIntent(pDeleteIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                // The purpose of setDefaults(Notification.DEFAULT_ALL) is to inherit notification properties
                // from system defaults
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image)

                )/*Notification with Image*/
        ;

        if (alert != null
                && alert.length() > ParsePushBroadcastReceiver.SMALL_NOTIFICATION_MAX_CHARACTER_LIMIT) {
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(alert));
        }
        return notificationBuilder;
    }

    private Bitmap getDummyDrawable(Context context) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.thumb1);
        return largeIcon ;
    }

}

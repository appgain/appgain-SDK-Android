package io.appgain.sdk.Controller;

import android.text.TextUtils;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.interfaces.ParseAuthCallBack;
import io.appgain.sdk.interfaces.ParsePushSetupCallBack;
import io.appgain.sdk.interfaces.UpdateNotificationCallBack;
import io.appgain.sdk.interfaces.UpdateUserIdCallBack;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 5/29/2019.
 */
public  class NotificationChannelUtils {

    public  static void addEmailNotificationChannel(final String email , final NotificationChannelCallBack callBack){
        Appgain.AppgainParseAuth(new ParseAuthCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys, String parseUserId) {
                saveEmailNotificationChannel(parseUserId, email, new ParsePushSetupCallBack() {
                    @Override
                    public void onSuccess() {
                        callBack.onSucces();
                    }

                    @Override
                    public void onFailure(ParseException e) {
                        callBack.onFail(new BaseResponse(String.valueOf(e.getCode()), e.getMessage()));
                    }
                });
            }

            @Override
            public void onFailure(BaseResponse failure) {
                if (callBack !=null)
                    callBack.onFail(failure);
            }
        });
    }

    public static void addSmsNotificationChannel(final String mobileNumber , final NotificationChannelCallBack callBack){
        Appgain.AppgainParseAuth(new ParseAuthCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys, String parseUserId) {
                saveMessageNotificationChannel(parseUserId, mobileNumber, new ParsePushSetupCallBack() {
                    @Override
                    public void onSuccess() {
                        callBack.onSucces();
                    }

                    @Override
                    public void onFailure(ParseException e) {
                        callBack.onFail(new BaseResponse(String.valueOf(e.getCode()), e.getMessage()));
                    }
                });
            }

            @Override
            public void onFailure(BaseResponse failure) {
                if (callBack !=null)
                    callBack.onFail(failure);
            }
        });
    }

    public static void addWebPushNotificationChannel( final NotificationChannelCallBack callBack){
        Appgain.AppgainParseAuth(new ParseAuthCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys, String parseUserId) {
                saveWebPushNotificationChannel(parseUserId, new ParsePushSetupCallBack() {
                    @Override
                    public void onSuccess() {
                        callBack.onSucces();
                    }

                    @Override
                    public void onFailure(ParseException e) {
                        callBack.onFail(new BaseResponse(String.valueOf(e.getCode()), e.getMessage()));
                    }
                });
            }

            @Override
            public void onFailure(BaseResponse failure) {
                if (callBack !=null)
                    callBack.onFail(failure);
            }
        });
    }

    static void saveAppPushNotificationChannel(final String notficationChanelUserId , final ParsePushSetupCallBack callBack) {
        if (TextUtils.isEmpty(notficationChanelUserId)){
            Timber.tag("saveNotificationChannel").e("UserId =  null || empty  >> " + notficationChanelUserId);
            return;
        }

        ParseQuery.getQuery("NotificationChannels").whereEqualTo("UserId" , notficationChanelUserId).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e!=null){
                    if (callBack!=null)
                        callBack.onFailure(e);
                    Timber.e(e.toString());
                }else {
                    if (objects!=null && objects.size()!=0){
                        Timber.tag("saveNotificationChannel").e("already exist" );
                        callBack.onSuccess();
                    }else {
                        ParseObject notificationChannel = new ParseObject("NotificationChannels");
                        notificationChannel.put("type", "appPush");
                        notificationChannel.put("appPush", getNotificationStatus());
                        notificationChannel.put("UserId", notficationChanelUserId);
                        notificationChannel.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e!=null){
                                    Timber.tag("saveNotificationChannel").e(e.toString());
                                    callBack.onFailure(e);
                                }else {
                                    // ParseInstallation succeed
                                    Timber.tag("saveNotificationChannel").e("ParseInstallation succeed" );
                                    callBack.onSuccess();
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    static void saveMessageNotificationChannel(final String notficationChanelUserId, final String mobileNumber, final ParsePushSetupCallBack callBack) {
        if (TextUtils.isEmpty(notficationChanelUserId)){
            Timber.tag("saveNotificationChannel").e("UserId =  null || empty  >> " + notficationChanelUserId);
            return;
        }
        if (TextUtils.isEmpty(mobileNumber)){
            Timber.tag("saveNotificationChannel").e("mobileNumber =  null || empty  >> " + notficationChanelUserId);
            return;
        }

        ParseQuery.getQuery("NotificationChannels")
                .whereEqualTo("UserId" , notficationChanelUserId)
                .whereEqualTo("mobileNum" , mobileNumber)
                .whereEqualTo("type" , "SMS")
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e!=null){
                            if (callBack!=null)
                                callBack.onFailure(e);
                            Timber.e(e.toString());
                        }else {
                            if (objects!=null && objects.size()!=0){
                                Timber.tag("saveNotificationChannel").e("already exist" );
                                callBack.onFailure(new ParseException(new Exception("already exist")));
                            }else {
                                ParseObject notificationChannel = new ParseObject("NotificationChannels");
                                notificationChannel.put("type", "SMS");
                                notificationChannel.put("mobileNum", mobileNumber);
                                notificationChannel.put("appPush", getNotificationStatus());
                                notificationChannel.put("UserId", notficationChanelUserId);
                                notificationChannel.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e!=null){
                                            Timber.tag("saveNotificationChannel").e(e.toString());
                                            callBack.onFailure(e);
                                        }else {
                                            // ParseInstallation succeed
                                            Timber.tag("saveNotificationChannel").e("ParseInstallation succeed" );
                                            callBack.onSuccess();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

    }

    static void saveEmailNotificationChannel(final String notficationChanelUserId , final  String email , final ParsePushSetupCallBack callBack) {
        if (TextUtils.isEmpty(notficationChanelUserId)){
            Timber.tag("saveNotificationChannel").e("UserId =  null || empty  >> " + notficationChanelUserId);
            return;
        }
        if (TextUtils.isEmpty(email)){
            Timber.tag("saveNotificationChannel").e("mobileNumber =  null || empty  >> " + notficationChanelUserId);
            return;
        }

        ParseQuery.getQuery("NotificationChannels")
                .whereEqualTo("UserId" , notficationChanelUserId)
                .whereEqualTo("email" , email)
                .whereEqualTo("type" , "email")
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e!=null){
                            if (callBack!=null)
                                callBack.onFailure(e);
                            Timber.e(e.toString());
                        }else {
                            if (objects!=null && objects.size()!=0){
                                Timber.tag("saveNotificationChannel").e("already exist" );
                                callBack.onFailure(new ParseException(new Exception("already exist")));
                            }else {
                                ParseObject notificationChannel = new ParseObject("NotificationChannels");
                                notificationChannel.put("type", "email");
                                notificationChannel.put("email", email);
                                notificationChannel.put("appPush", getNotificationStatus());
                                notificationChannel.put("UserId", notficationChanelUserId);
                                notificationChannel.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e!=null){
                                            Timber.tag("saveNotificationChannel").e(e.toString());
                                            callBack.onFailure(e);
                                        }else {
                                            // ParseInstallation succeed
                                            Timber.tag("saveNotificationChannel").e("ParseInstallation succeed" );
                                            callBack.onSuccess();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

    }

    static void saveWebPushNotificationChannel(final String notficationChanelUserId  , final ParsePushSetupCallBack callBack) {
        if (TextUtils.isEmpty(notficationChanelUserId)){
            Timber.tag("saveNotificationChannel").e("UserId =  null || empty  >> " + notficationChanelUserId);
            return;
        }


        ParseQuery.getQuery("NotificationChannels")
                .whereEqualTo("UserId" , notficationChanelUserId)
                .whereEqualTo("type" , "webPush")
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e!=null){
                            if (callBack!=null)
                                callBack.onFailure(e);
                            Timber.e(e.toString());
                        }else {
                            if (objects!=null && objects.size()!=0){
                                Timber.tag("saveNotificationChannel").e("already exist" );
                                callBack.onFailure(new ParseException(new Exception("already exist")));
                            }else {
                                ParseObject notificationChannel = new ParseObject("NotificationChannels");
                                notificationChannel.put("type", "webPush");
                                notificationChannel.put("appPush", getNotificationStatus());
                                notificationChannel.put("UserId", notficationChanelUserId);
                                notificationChannel.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e!=null){
                                            Timber.tag("saveNotificationChannel").e(e.toString());
                                            callBack.onFailure(e);
                                        }else {
                                            // ParseInstallation succeed
                                            Timber.tag("saveNotificationChannel").e("ParseInstallation succeed" );
                                            callBack.onSuccess();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

    }

    static void updateNotificationsChannelUserID(final String userId, String old_userID, final UpdateUserIdCallBack callBack) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("NotificationChannels") ;
        parseQuery.whereEqualTo("UserId" , old_userID )
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null){
                            Timber.e("parse objects size" + objects.size() );
                            if (!objects.isEmpty())
                            {
                                for (ParseObject parseObject: objects) {
                                    parseObject.put("UserId" , userId);
                                    parseObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e!=null){
                                                Log.e("Appgain" , "NotificationChannels update " + e.toString());
                                                if (callBack !=null)
                                                    callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                                            }else {
                                                Appgain.getPreferencesManager().saveId(userId);
                                                if (callBack !=null)
                                                    callBack.onSuccess();

                                            }
                                        }
                                    });
                                }
                            }
                        }else {
                            Log.e("Appgain" , "NotificationChannels update " + e.toString());
                            if (callBack !=null)
                                callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                        }
                    }
                });

    }

    /**
     *  saveNotificationChannel()
     *  save Notification Channel object to parse
     */
     public static  boolean getNotificationStatus(){
        return Appgain.getPreferencesManager().getNotificationStatus();
    }



    public static void changeNotificationStatus(final String type , final boolean apppush , final UpdateNotificationCallBack callBack){
        ParseQuery.getQuery("NotificationChannels").whereEqualTo("UserId" , Appgain.getPreferencesManager().getUserId()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e!=null){
                    if (callBack!=null)
                        callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                    Timber.e(e.toString());
                }else {
                    for (ParseObject object : objects){
                        if (!object.getString("type").equals(type))
                            continue;
                        object.put("appPush" , apppush);
                        object.saveInBackground();
                    }
                    if (callBack!=null)
                        callBack.onSuccess();
                }
            }
        });
    }

    public static void changeAllNotificationStatus(final boolean apppush, final UpdateNotificationCallBack callBack){
        ArrayList<String> strings = new ArrayList<>() ;
        strings.add("SMS");
        strings.add("email");
        strings.add("appPush");
        strings.add("webPush");
         changeNotificationStatus(strings, apppush, new UpdateNotificationCallBack() {
             @Override
             public void onSuccess() {
                 Appgain.getPreferencesManager().saveNotificationStatus(apppush);
                 callBack.onSuccess();
             }

             @Override
             public void onFailure(BaseResponse failure) {
                 callBack.onFailure(failure);
             }
         });
    }

    private static void changeNotificationStatus(final ArrayList<String> types , final boolean apppush , final UpdateNotificationCallBack callBack){
        ParseQuery.getQuery("NotificationChannels").whereEqualTo("UserId" , Appgain.getPreferencesManager().getUserId()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e!=null){
                    if (callBack!=null)
                        callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                    Timber.e(e.toString());
                }else {
                    for (ParseObject object : objects){
                        if (!types.contains(object.getString("type")))
                            continue;
                        object.put("appPush" , apppush);
                        object.saveInBackground();
                    }
                    if (callBack!=null)
                        callBack.onSuccess();
                }
            }
        });
    }


}

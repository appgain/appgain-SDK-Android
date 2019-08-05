package io.appgain.sdk.Controller;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.fcm.ParseFCM;

import java.util.ArrayList;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.interfaces.ParseAuthCallBack;
import io.appgain.sdk.interfaces.ParseLoginCallBack;
import io.appgain.sdk.interfaces.ParsePushSetupCallBack;
import io.appgain.sdk.interfaces.SDKInitCallBack;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 7/2/2019.
 */
public class ParseUtil {

    /**
     *
     *
     *  parseSetup() excited  if user have backend support
     *  initialize parse
     *  if user id saved ? exit call callback onSuccess(user_id)
     *  if user provide user data call createParseUser()
     *   else login anonymous
     */
    private static  void parseSetup(String parseAppId, String server ,  final ParseLoginCallBack parseLoginCallBack){
        ParseSetupUtils.parseSetup(parseAppId , server , null , parseLoginCallBack);
    }

    /** get credentials() :
     * call setupServerKeys()
     * call parseSetup()
     */
    public static void setupParse(String parseAppId , String serverName , final ParseLoginCallBack authListener){
        setupParse(parseAppId , serverName  , null, authListener);
    }


    public static void setupParse(String parseAppId , String serverName  , String clientKey, final ParseLoginCallBack authListener){
        ParseSetupUtils.parseSetup(parseAppId , serverName , clientKey, new ParseLoginCallBack() {
            @Override
            public void onSuccess(final String userId) {
                parsePushSetup(userId , new ParsePushSetupCallBack(){
                    @Override
                    public void onSuccess() {
                        if (authListener!=null)
                            authListener.onSuccess(userId);
                    }

                    @Override
                    public void onFailure(ParseException e) {
                        authListener.onFail(e);
                    }
                });
            }

            @Override
            public void onFail(ParseException e){
                authListener.onFail(e);
            }
        });

    }


    public static void AppgainParseAuth(final ParseAuthCallBack authListener){
        AppgainSuitUtil.setupServerKeys(new SDKInitCallBack() {
            @Override
            public void onSuccess(final SDKKeys sdkKeys) {
                // parse setup
                parseSetup(sdkKeys.getParseAppId(), sdkKeys.getParseServerUrl(), new ParseLoginCallBack() {
                    @Override
                    public void onSuccess(final String userId) {
                        parsePushSetup(userId , new ParsePushSetupCallBack(){
                            @Override
                            public void onSuccess() {
                                if (authListener!=null)
                                    authListener.onSuccess(sdkKeys , userId);
                            }

                            @Override
                            public void onFailure(ParseException e) {
                                if (authListener!=null)
                                    authListener.onFailure(new BaseResponse(e.getCode()+"", "Parse setup " +e.getMessage()));
                                Timber.tag("AppGainParseSetup").e(  "getAppGainCredentials " + e.toString())  ;

                            }
                        });
                    }

                    @Override
                    public void onFail(ParseException e) {
                        //Parse setup applicationId or  server not found
                        if (TextUtils.equals(e.getMessage() , Config.NO_BACKEND)){
                            if (authListener!=null)
                                authListener.onSuccess(sdkKeys , null);
                        }
                        else{
                            if (authListener!=null)
                                authListener.onFailure(new BaseResponse(e.getCode()+"", "Parse setup " +e.getMessage()));
                            Timber.tag("AppGainParseSetup").e(  "getAppGainCredentials " + e.toString())  ;
                        }
                    }
                });
            }

            @Override
            public void onFailure(BaseResponse failure) {
                authListener.onFailure(failure);
            }
        });
    }

    /**
     *
     * create object in Parse installation entity  to be used with parse push notifications
     *
     */
    static void parsePushSetup(String userId, final ParsePushSetupCallBack callBack) {
        // install parse for push notification
        final ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        if (TextUtils.isEmpty(installation.getString(Config.USER_ID_KEY)) ||  !TextUtils.equals(installation.getString(Config.USER_ID_KEY) , Appgain.getPreferencesManager().getUserId()) ){
            installation.put("user",ParseUser.getCurrentUser());
            installation.put(Config.USER_ID_KEY ,userId);
            ArrayList arrayList = new ArrayList() ;
            installation.put("channels" , arrayList);
            installation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e!=null){
                        Log.e("Appgain" , "ParseInstallation : " +e.toString()) ;
                        if (callBack!=null)
                            callBack.onFailure(e);
                    }else {
                        // ParseInstallation succeed
                        Timber.d("ParseInstallation succeed" );
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                ParseFCM.register(Appgain.getContext());
                            }
                        });
                        insertUserIdInParseUserObject(installation.getString(Config.USER_ID_KEY) , callBack);
                    }
                }
            });
        }else {
            callBack.onSuccess();
        }
    }



    private static void insertUserIdInParseUserObject(final String userId, final ParsePushSetupCallBack callBack) {
        final ParseUser parseUser = ParseUser.getCurrentUser() ;
        parseUser.put(Config.USER_ID_KEY , userId);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("Appgain" , "insertUserIdInParseUserObject: " +e.toString()) ;
                    // remove sttatic user id
                    if (callBack !=null)
                        callBack.onFailure(e);
                }else {
                    // save notification chancels
                    NotificationChannelUtils.saveAppPushNotificationChannel(userId, callBack);
                }
            }
        });
    }

}

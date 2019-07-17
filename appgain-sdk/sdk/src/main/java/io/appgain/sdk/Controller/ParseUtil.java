package io.appgain.sdk.Controller;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.fcm.ParseFCM;

import java.util.ArrayList;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Model.User;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.interfaces.ParseAuthCallBack;
import io.appgain.sdk.interfaces.ParseLoginCallBack;
import io.appgain.sdk.interfaces.ParsePushSetupCallBack;
import io.appgain.sdk.interfaces.ParseSignUpCallBack;
import io.appgain.sdk.interfaces.SDKInitCallBack;
import io.appgain.sdk.interfaces.UpdateUserIdCallBack;
import timber.log.Timber;

import static com.parse.Parse.LOG_LEVEL_ERROR;

/**
 * Created by developers@appgain.io on 7/2/2019.
 */
public class ParseUtil {

    /**
     *
     * @param applicationId : parse app id
     * @param server  : parse server
     *
     *  parseSetup() excited  if user have backend support
     *  initialize parse
     *  if user id saved ? exit call callback onSuccess(user_id)
     *  if user provide user data call createParseUser()
     *   else login anonymous
     */
    private static  void parseSetup(String applicationId, String server ,  final ParseLoginCallBack parseLoginCallBack){
        parseSetup(applicationId , server , null , parseLoginCallBack);
    }
    private static  void parseSetup(String applicationId, String server,  String clientKey ,  final ParseLoginCallBack parseLoginCallBack){

        // have no backend then  exit
        if (applicationId ==null || server ==null ){
            parseLoginCallBack.onFail(new ParseException(404 , Config.NO_BACKEND));
            return;
        }

        if (!server.isEmpty() && !server.endsWith("/")){
            server = server+"/" ;
        }
        if (Appgain.getContext() == null){
            parseLoginCallBack.onFail(new ParseException(404 , "Application context cannot be null "));
            return;
        }

        //

        if (clientKey!=null){
            // inti parse
            Parse.initialize(new Parse.Configuration.Builder(Appgain.getContext())
                    .clientBuilder(Injector.provideOkHttpClientBuilder())
                    .applicationId(applicationId)
                    .server(server)
                    .clientKey(clientKey)
                    .build()
            );
        }else {
            Parse.initialize(new Parse.Configuration.Builder(Appgain.getContext())
                    .clientBuilder(Injector.provideOkHttpClientBuilder())
                    .applicationId(applicationId)
                    .server(server)
                    .build()
            );
        }

        Parse.setLogLevel(LOG_LEVEL_ERROR);
//
        // if user is logged before rerun > id
        String user_id = Appgain.getPreferencesManager().getUserId() ;
        Log.e("user_id" , user_id+"") ;
        if ( user_id != null)
        {
            ParseLogin.login(Appgain.getPreferencesManager().getUserProvidedData() , parseLoginCallBack);
            return;
        }


        // login user
        User userProvidedData = Appgain.getPreferencesManager().getUserProvidedData() ;
        if(userProvidedData != null){
            // login with provided data
            ParseLogin.createParseUser(userProvidedData, new ParseSignUpCallBack() {
                @Override
                public void onSuccess(User user) {
                    ParseLogin.login(user , parseLoginCallBack);
                }

                @Override
                public void onFail(ParseException e) {
                    Log.e("Appgain" , "ParseSignUp : " +e.getMessage()) ;
                    parseLoginCallBack.onFail(e);
                }
            });

        }else {
            // login anonymous
            ParseLogin.login(parseLoginCallBack);
        }
    }
    // update parse installation , users , notification channel
    static void updateParseUserId(final String userId, final UpdateUserIdCallBack callBack) {

        // update instalition
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        final String old_userID = (String) installation.get("UserId");
        Timber.e("updateParseUserId" +" old "+ old_userID + "  new  " + userId );
        // case Appgain sdk  initialized
        if (userId == null || (old_userID !=null&& old_userID.equals(userId)) ){
            return;
        }
        installation.put("UserId" , userId);
        ArrayList arrayList = new ArrayList() ;
        arrayList.add("AE");
        installation.put("channels" , arrayList);
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("Appgain" , "ParseInstallation update : " +e.toString()) ;
                    // remove sttatic user id
                    if (callBack !=null)
                        callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                }else {
                    // ParseInstallation succeed
                    Timber.e("ParseInstallation update succeed" );
                    // update preference manger usr id
                    Appgain.getPreferencesManager().saveId(userId);
                    // update UserId key on users object
                    updateUserIdInParseUserObject(userId , callBack);

                }
            }
        });
    }

    private static void updateUserIdInParseUserObject(final String userId, final UpdateUserIdCallBack callBack) {
        final ParseUser parseUser = ParseUser.getCurrentUser() ;
        parseUser.put("UserId" , userId);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("Appgain" , "updateUserIdInParseUserObject update : " +e.toString()) ;
                    // remove sttatic user id
                    if (callBack !=null)
                        callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                }else {
                    // update notification chancels
                    NotificationChannelUtils.updateNotificationsChannelUserID(userId,parseUser.getObjectId() , callBack);
                }
            }
        });
    }


    /** get credentials() :
     * call setupServerKeys()
     * call parseSetup()
     */
    public static void setupParse(String parseAppId , String serverName , final ParseLoginCallBack authListener){
        setupParse(parseAppId , serverName  , null, authListener);
    }


    public static void setupParse(String parseAppId , String serverName  , String clientKey, final ParseLoginCallBack authListener){
        parseSetup(parseAppId , serverName , clientKey, new ParseLoginCallBack() {
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
        if (TextUtils.isEmpty(installation.getString("UserId")) ||  !TextUtils.equals(installation.getString("UserId") , Appgain.getPreferencesManager().getUserId()) ){
            installation.put("user",ParseUser.getCurrentUser());
            installation.put("UserId" ,userId);
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
                        insertUserIdInParseUserObject(installation.getString("UserId") , callBack);
                    }
                }
            });
        }else {
            callBack.onSuccess();
        }
    }



    private static void insertUserIdInParseUserObject(final String userId, final ParsePushSetupCallBack callBack) {
        final ParseUser parseUser = ParseUser.getCurrentUser() ;
        parseUser.put("UserId" , userId);
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

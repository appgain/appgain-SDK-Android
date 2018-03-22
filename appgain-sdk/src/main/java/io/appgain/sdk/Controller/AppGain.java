package io.appgain.sdk.Controller;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.IOException;

import io.appgain.sdk.Model.SdkKeys;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.User;
import io.appgain.sdk.R;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.SmartLinkMatch.SmartLinkMatch;
import io.appgain.sdk.SmartLinkMatch.SmartLinkMatchListener;
import io.appgain.sdk.SmartLinkMatch.SmartLinkMatchResponse;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.interfaces.Auth2Listener;
import io.appgain.sdk.interfaces.AuthListener;
import io.appgain.sdk.interfaces.InitListener;
import io.appgain.sdk.interfaces.ParseLoginListener;
import io.appgain.sdk.interfaces.ParseSignUpListener;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Sotra on 2/12/2018.
 */

public class AppGain {

    private static  AppGain appGain ;
    private static Context context ;
    private static PreferencesManager preferencesManager ;

    public static AppGain getInstance() {
        if (appGain == null) {
            appGain = new AppGain();
        }
        return appGain;
    }


    public  static void initialize(final Context context , String appID , final String appApiKey){
        Timber.plant(new Timber.DebugTree());
        getInstance().setContext(context);
        getInstance().setAppID(appID);
        getInstance().setAppApiKey(appApiKey);

        getCredentials(new Auth2Listener() {
            @Override
            public void onSuccess(SdkKeys sdkKeys, String userId) {
                Log.e("AppGaInitialize" , "success") ;
                smartLinkMatch();
            }

            @Override
            public void onFailure(BaseResponse failure) {
                Log.e("AppGaInitialize" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
            }
        });
    }
    public  static void initialize(final Context context , String appID , final String appApiKey , final InitListener initListener){
        Timber.plant(new Timber.DebugTree());
        getInstance().setContext(context);
        getInstance().setAppID(appID);
        getInstance().setAppApiKey(appApiKey);

        getCredentials(new Auth2Listener() {
            @Override
            public void onSuccess(SdkKeys sdkKeys, String userId) {
                Log.e("AppGaInitialize" , "success") ;
                smartLinkMatch();
                initListener.onSuccess();
            }

            @Override
            public void onFailure(BaseResponse failure) {
                Log.e("AppGaInitialize" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                initListener.onFail(failure);
            }
        });
    }

    public  static void initialize(final Context context , String appID , String appApiKey , User user , final InitListener initListener){
        Timber.plant(new Timber.DebugTree());
        getInstance().setContext(context);
        getInstance().setAppID(appID);
        getInstance().setAppApiKey(appApiKey);
        getInstance().getPreferencesManager().saveUserProvidedData(user);

        getCredentials(new Auth2Listener() {
            @Override
            public void onSuccess(SdkKeys sdkKeys, String userId) {
                Log.e("AppGaInitialize" , "success") ;
                smartLinkMatch();
                initListener.onSuccess();
            }

            @Override
            public void onFailure(BaseResponse failure) {
                Log.e("AppGaInitialize" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                initListener.onFail(failure);
            }
        });


    }


    //-----------------------------------------------------------------------------------------------------------------------------------
    /**
     * settinig  values  start
     **/

    public static void setContext(Context context) {
        AppGain.context = context;
    }

    public static void setAppID(String appID) {
        AppGain.getPreferencesManager().saveAppId(appID); ;
    }

    public static void setAppApiKey(String apiKey) {
        AppGain.getPreferencesManager().saveApiKey(apiKey) ;
    }


    /**
     * settinig  values end
     **/

    /**
    *  getting values start
    **/

    private static   PreferencesManager getPreferencesManager(){
        if (preferencesManager == null) {
            preferencesManager = new PreferencesManager(context);
        }
        return preferencesManager;
    }

    public static String getAppID() {
        return getPreferencesManager().getAppId();
    }

    public static String getApiKey() {
        return getPreferencesManager().getApiKey();
    }

    public static Context getContext() {
        return context;
    }
    /**
    *  getting values end
    **/
    //-----------------------------------------------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------------------------------------------------------
    /**
     * class method  start
     **/

    private  static  void parseSetup(String applicationId , String server , final ParseLoginListener parseLoginListener){

        // have no backend then  exit
        if (applicationId ==null || server ==null ){
            parseLoginListener.onFail(new ParseException(404 , Config.NO_BACKEND));
            return;
        }

        // inti parse
        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId(applicationId)
                .server(server)
                .build()
        );


//
        // if user is logged before rerun > id
        String user_id = getPreferencesManager().getUserId() ;
        if ( user_id != null)
        {
            parseLoginListener.onSuccess(user_id);
            return;
        }


        // login user
        User userProvidedData = getPreferencesManager().getUserProvidedData() ;
        if(userProvidedData != null){
            // login with provided data
            createParseUser(userProvidedData, new ParseSignUpListener() {
                @Override
                public void onSuccess(User user) {
                        login(user , parseLoginListener);
                }

                @Override
                public void onFail(ParseException e) {
                    Log.e("AppGain" , "ParseSignUp : " +e.getMessage()) ;
                    parseLoginListener.onFail(e);
                }
            });

        }else {
            // login anonymous
            login(parseLoginListener);
        }
    }

    /**
     *
     * get sdk keys
     * 1- case localed saved exit with sucees
     * 2- case not exist local get it from server
     */
    public static void getSdkKeys(final AuthListener authListener){
        SdkKeys appGainCredentials = getPreferencesManager().getAppGainCredentials();
        if (appGainCredentials !=null){
            authListener.onSuccess(appGainCredentials);
        }

        else{
            Call<SdkKeys> call = Injector.Api().getCredentials(Config.CREDENTIALS_URL(AppGain.getAppID()));
            call.enqueue(new CallbackWithRetry<SdkKeys>(call , null) {
                @Override
                public void onResponse(Call<SdkKeys> call, final Response<SdkKeys> response) {
                        if (response.isSuccessful()&&response.body()!=null){
                            getPreferencesManager().saveAppGainCredentials(response.body());
                            if (authListener!=null)
                                authListener.onSuccess(response.body());
                        }else {
                            if (authListener!=null)
                                try {
                                    authListener.onFailure(Utils.getAppGainFailure(response.errorBody().string()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            Timber.tag("getSdkKeys").e(response.toString());
                        }
                }
            });
        }
    }

    /*
    ** get credentials
     * 1- get sdk keys from server and save it localy
      * 2- setup parse and login with user
    */
    public static void getCredentials(final Auth2Listener authListener){
        getSdkKeys(new AuthListener() {
            @Override
            public void onSuccess(final SdkKeys sdkKeys) {
                // parse setup
                parseSetup(sdkKeys.getParseAppId(), sdkKeys.getParseServerUrl(), new ParseLoginListener() {
                    @Override
                    public void onSuccess(String userId) {
                        authListener.onSuccess(sdkKeys , userId);
                    }

                    @Override
                    public void onFail(ParseException e) {
                        //Parse setup applicationId or  server not found
                        if (TextUtils.equals(e.getMessage() , Config.NO_BACKEND))
                            authListener.onSuccess(sdkKeys , null);
                        else
                        authListener.onFailure(new BaseResponse(e.getCode()+"", "Parse setup " +e.getMessage()));
                        Timber.tag("AppGainParseSetup").e(  "getAppGainCredentials" + e.toString())  ;
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
     * login as anonymous user in case of no username , password , email provided before
     */
    private static   void login(final ParseLoginListener parseLoginListener){
        Timber.e("login as anonymous entered");
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null) {
                    Timber.e( "Anonymous login failed. "+e.toString());
                    if (parseLoginListener!=null)
                        parseLoginListener.onFail(e);
                } else {
                    Timber.e( "Anonymous user logged in.");
                    parsePushSetup(parseUser);
                    getInstance().getPreferencesManager().saveId(parseUser.getObjectId());
                    if (parseLoginListener!=null)
                        parseLoginListener.onSuccess(parseUser.getObjectId());
                }
            }
        });
    }

    private static void parsePushSetup(ParseUser parseUser) {
        // install parse for push notification

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user",parseUser);
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("AppGain" , "ParseInstallation : " +e.toString()) ;
                }else {
                    // ParseInstallation succeed
                    Timber.e("ParseInstallation succeed" );
                }
            }
        });
    }

    /**
     * login with data
     */
    private static void login(final User user , final  ParseLoginListener parseLoginListener){
        Timber.e("login with data entered");
        ParseUser.logInInBackground(user.getUsername() , user.getPassword(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    parsePushSetup(parseUser);
                    Timber.e( "user logged in wit id : " + parseUser.getObjectId());
                    getInstance().getPreferencesManager().saveId(parseUser.getObjectId());
                    if (parseLoginListener!=null)
                        parseLoginListener.onSuccess(parseUser.getObjectId());
                } else {
                    Timber.e( "Anonymous login failed. "+e.toString());
                    if (parseLoginListener!=null)
                        parseLoginListener.onFail(e);
                }
            }
        });
    }
    /**
     * create parse  user
     */
    private static void createParseUser(final  User user, final ParseSignUpListener parseSignUpListener){
        Timber.e("create parse  user entered");
        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(user.getUsername());
        parseUser.setPassword(user.getPassword());
        parseUser.setEmail(user.getEmail());

        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Timber.e("createParseUser : "+"success");
                    if (parseSignUpListener!=null)
                    parseSignUpListener.onSuccess(user);
                } else {
                    if (e.getMessage().contains("already exists") || e.getCode()==202)
                    {
                        parseSignUpListener.onSuccess(user);
                        return;
                    }
                    Timber.e("createParseUser : " + e.getCode() +e.toString() );
                    if (parseSignUpListener!=null)
                   parseSignUpListener.onFail(e);
                }
            }
        });
    }



    private static void smartLinkMatch() {
        SmartLinkMatch.enqueue(new SmartLinkMatchListener() {
            @Override
            public void onSmartLinkCreated(@Nullable SmartLinkMatchResponse response) {
                getInstance().getPreferencesManager().saveFirstRun();
            }

            @Override
            public void onSmartLinkFail(@Nullable BaseResponse failure) {
                Timber.e("Inti_smart_link_match" , failure.getMessage()) ;
            }
        });
    }

    public static boolean isFirstRun() {
        return getInstance().getPreferencesManager().isFirstRun();
    }


    public  static  void clear (){
        getPreferencesManager().clear();
    }

    /**
     * class methods  end
     **/
    //------------------------------------------------------------------------------------------------------------------------------------

}

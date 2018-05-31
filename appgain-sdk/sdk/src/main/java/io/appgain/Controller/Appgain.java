package io.appgain.Controller;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import io.appgain.Model.SDKKeys;
import io.appgain.Model.BaseResponse;
import io.appgain.Model.User;
import io.appgain.Service.CallbackWithRetry;
import io.appgain.Service.Injector;
import io.appgain.SmartLinkMatch.SmartLinkMatch;
import io.appgain.SmartLinkMatch.SmartLinkMatchCallBack;
import io.appgain.SmartLinkMatch.ResponseModels.SmartLinkMatchResponse;
import io.appgain.Utils.Utils;
import io.appgain.interfaces.ParseInitCallBack;
import io.appgain.interfaces.SDKInitCallBack;
import io.appgain.interfaces.AppgainSDKInitCallBack;
import io.appgain.interfaces.ParseLoginCallBack;
import io.appgain.interfaces.ParseSignUpCallBack;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

import static com.parse.Parse.LOG_LEVEL_ERROR;

/**
 * Created by developers@appgain.io  on 2/12/2018.
 */

public class Appgain {

    private static Appgain appGain ;
    private static Context context ;
    private static PreferencesManager preferencesManager ;

    public static  Appgain getInstance() {
        if (appGain == null) {
            appGain = new Appgain();
        }
        return appGain;
    }


    /**
     *  initialize base case with appID  , appApiKey
     *  save appID , appApiKey
     *  init SDK keys
     *  save sdk keys
     *  call parse anonymous login if user have backend support
     *  save parse user id
     */
    public  static void initialize(final Context context , String appID , final String appApiKey){
        initialize(context , appID , appApiKey , (AppgainSDKInitCallBack) null);
    }


    /**
     *  initialize base case with callback
     */
    public  static void initialize(final Context context , String appID , final String appApiKey , final AppgainSDKInitCallBack appgainSDKInitCallBack){
        initialize(context , appID , appApiKey , (User) null , appgainSDKInitCallBack) ;
    }


    /**
     *  initialize  case with  given  user data
     *  save appID , appApiKey
     *  init SDK keys
     *  save sdk keys
     * call parse  login with username  , email and password  if user have backend support  and generate its id
     * *  save parse user  generated id
     */
    public  static void initialize(final Context context , String appID , String appApiKey , User user){
        initialize(context,appID,appApiKey,user,(AppgainSDKInitCallBack)null);
    }

    /**
     *  initialize  case with  given  user data with callback
     *
     */
    public  static void initialize(final Context context , String appID , String appApiKey , final User user , final AppgainSDKInitCallBack appgainSDKInitCallBack){
        getInstance().setContext(context);
        getInstance().setAppID(appID);
        getInstance().setAppApiKey(appApiKey);
        if (user !=null)
        getInstance().getPreferencesManager().saveUserProvidedData(user);

        // get cred
        getCredentials(new ParseInitCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys, String userId) {
                Log.e("AppGaInitialize" , "success") ;
                // smart link match  internal use case
                smartLinkMatch();
                // saving user NotificationChannels
                saveNotificationChannel(userId);
                if (appgainSDKInitCallBack !=null)
                appgainSDKInitCallBack.onSuccess();
            }

            @Override
            public void onFailure(BaseResponse failure) {
                Log.e("AppGaInitialize" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                if (appgainSDKInitCallBack !=null)
                    appgainSDKInitCallBack.onFail(failure);
            }
        });


    }


    //-----------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------
    /**
     * setting  values  start
     **/

    /**
     *
     * set Appgain instance context
     */

    public static void setContext(Context context) {
        Appgain.context = context;
    }

    /**
     *
     * save appID in preferences manger
     */

    public static void setAppID(String appID) {
        Appgain.getPreferencesManager().saveAppId(appID); ;
    }

    /**
     *
     * save apiKey in preferences manger
     */
    public static void setAppApiKey(String apiKey) {
        Appgain.getPreferencesManager().saveApiKey(apiKey) ;
    }


    /**
     * setting  values end
     */
    //-----------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------

    /**
    *  getting values start
    */

    /**
     *
     * init PreferencesManager model instance
     */
    private static   PreferencesManager getPreferencesManager(){
        if (preferencesManager == null) {
            preferencesManager = new PreferencesManager(context);
        }
        return preferencesManager;
    }

    /**
     *
     * @return Appgain saved app id
     */

    public static String getAppID() {
        return getPreferencesManager().getAppId();
    }


    /**
     *
     * @return Appgain saved API key
     */


    public static String getApiKey() {
        return getPreferencesManager().getApiKey();
    }


    /**
     *
     * @return Appgain instance context
     */

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
     */


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
    private  static  void parseSetup(String applicationId , String server , final ParseLoginCallBack parseLoginCallBack){

        // have no backend then  exit
        if (applicationId ==null || server ==null ){
            parseLoginCallBack.onFail(new ParseException(404 , Config.NO_BACKEND));
            return;
        }

        if (!server.isEmpty() && !server.endsWith("/")){
            server = server+"/" ;
        }
        // inti parse
        Parse.initialize(new Parse.Configuration.Builder(context)
                .clientBuilder(Injector.provideOkHttpClientBuilder())
                .applicationId(applicationId)
                .server(server)
                .build()
        );
        Parse.setLogLevel(LOG_LEVEL_ERROR);


//
        // if user is logged before rerun > id
        String user_id = getPreferencesManager().getUserId() ;
        if ( user_id != null)
        {
            parseLoginCallBack.onSuccess(user_id);
            return;
        }


        // login user
        User userProvidedData = getPreferencesManager().getUserProvidedData() ;
        if(userProvidedData != null){
            // login with provided data
            createParseUser(userProvidedData, new ParseSignUpCallBack() {
                @Override
                public void onSuccess(User user) {
                        login(user , parseLoginCallBack);
                }

                @Override
                public void onFail(ParseException e) {
                    Log.e("Appgain" , "ParseSignUp : " +e.getMessage()) ;
                    parseLoginCallBack.onFail(e);
                }
            });

        }else {
            // login anonymous
            login(parseLoginCallBack);
        }
    }

    /**
     *
     * get sdk keys
     * if SDKKeys exist call SDKInitCallBack onSuccess
     * else call getCredentials API
     *  save data
     */
    public static void getSdkKeys(final SDKInitCallBack sdkInitCallBack){
        SDKKeys appGainCredentials = getPreferencesManager().getAppGainCredentials();
        if (appGainCredentials !=null){
            sdkInitCallBack.onSuccess(appGainCredentials);
        }

        else{
            Call<SDKKeys> call = Injector.Api().getCredentials(Config.CREDENTIALS_URL(Appgain.getAppID()));
            call.enqueue(new CallbackWithRetry<SDKKeys>(call , null) {
                @Override
                public void onResponse(Call<SDKKeys> call, final Response<SDKKeys> response) {
                        if (response.isSuccessful()&&response.body()!=null){
                            getPreferencesManager().saveAppGainCredentials(response.body());
                            if (sdkInitCallBack!=null)
                                sdkInitCallBack.onSuccess(response.body());
                        }else {
                            if (sdkInitCallBack!=null)
                                try {
                                    sdkInitCallBack.onFailure(Utils.getAppGainFailure(response.errorBody().string()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            Timber.tag("getSdkKeys").e(response.toString());
                        }
                }
            });
        }
    }

    /** get credentials() :
     * call getSdkKeys()
     * call parseSetup()
    */
    public static void getCredentials(final ParseInitCallBack authListener){
        getSdkKeys(new SDKInitCallBack() {
            @Override
            public void onSuccess(final SDKKeys sdkKeys) {

                // parse setup
                parseSetup(sdkKeys.getParseAppId(), sdkKeys.getParseServerUrl(), new ParseLoginCallBack() {
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
                        Timber.tag("AppGainParseSetup").e(  "getAppGainCredentials " + e.toString())  ;
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
    private static void parsePushSetup(ParseUser parseUser) {
        // install parse for push notification

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user",parseUser);
        installation.put("userId" ,parseUser.getObjectId() );
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("Appgain" , "ParseInstallation : " +e.toString()) ;
                }else {
                    // ParseInstallation succeed
                    Timber.e("ParseInstallation succeed" );
                }
            }
        });
    }


    /**
     * parse login as anonymous user in case of no username , password , email provided before
     */
    private static   void login(final ParseLoginCallBack parseLoginListener){
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


    /**
     * parse login with user provided data
     */
    private static void login(final User user , final ParseLoginCallBack parseLoginListener){
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
                    e.printStackTrace();
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
    private static void createParseUser(final  User user, final ParseSignUpCallBack parseSignUpListener){
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


    /**
     * call Appgain sdk smart link match api
     */
    private static void smartLinkMatch() {
        SmartLinkMatch.enqueue(new SmartLinkMatchCallBack() {
            @Override
            public void onSmartLinkCreated(@Nullable SmartLinkMatchResponse response) {
                changeAppFirstRun();
            }

            @Override
            public void onSmartLinkFail(@Nullable BaseResponse failure) {
                changeAppFirstRun();
                Timber.e("Inti_smart_link_match" , failure.getMessage()) ;
            }
        });
    }

    /**
     *
     * @return if app first run
     */
    public static boolean isFirstRun() {
        return getInstance().getPreferencesManager().isFirstRun();
    }

    /**
     * change app first run to false
     */
    public static void changeAppFirstRun() {
        getInstance().getPreferencesManager().saveFirstRun();
    }

    public static void enableLog() {
        Timber.plant(new Timber.DebugTree());
    }



    /**
     *  saveNotificationChannel()
     *  save Notification Channel object to parse
     */
    private static void saveNotificationChannel(String userId) {
        if (TextUtils.isEmpty(userId)){
            Timber.tag("saveNotificationChannel").e("userId =  null || empty  >> " + userId);
            return;
        }
        ParseObject notificationChannel = new ParseObject("NotificationChannels");
        notificationChannel.put("type", "appPush");
        notificationChannel.put("appPush", true);
        notificationChannel.put("userId", userId);
        notificationChannel.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Timber.tag("saveNotificationChannel").e(e.toString());
                }else {
                    // ParseInstallation succeed
                    Timber.tag("saveNotificationChannel").e("ParseInstallation succeed" );
                }
            }
        });
    }

    /**
     * clear all PreferencesManager data
     */
    public  static  void clear (){
        getPreferencesManager().clear();
    }

    /**
     * class methods  end
     **/
    //------------------------------------------------------------------------------------------------------------------------------------

}

package io.appgain.sdk.Controller;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.fcm.ParseFCM;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.User;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.Utils.Validator;
import io.appgain.sdk.interfaces.ParseAuthCallBack;
import io.appgain.sdk.interfaces.ParsePushSetupCallBack;
import io.appgain.sdk.interfaces.SDKInitCallBack;
import io.appgain.sdk.interfaces.AppgainSDKInitCallBack;
import io.appgain.sdk.interfaces.ParseLoginCallBack;
import io.appgain.sdk.interfaces.ParseSignUpCallBack;
import io.appgain.sdk.interfaces.UpdateNotificationCallBack;
import io.appgain.sdk.interfaces.UpdateUserIdCallBack;
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
    public  static void initialize(final Context context , String appID , final String appApiKey) throws Exception {
        initialize(context , appID , appApiKey , null);
    }


    /**
     *  initialize  case with  given  user data with callback
     *
     // case user  provide user id
     //     case static user id not equal  parse user id (user id set after initialize)
     //     >update userId
     //      case static user id  equal  parse user id (user id set before initialize)
     //     >update userId
     *
     */
    public    static void initialize(final Context context , String appID , String appApiKey , final AppgainSDKInitCallBack appgainSDKInitCallBack) throws Exception{


        validateAppData(context,appID,appApiKey);
        getInstance().setContext(context);
        getInstance().setAppID(appID);
        getInstance().setAppApiKey(appApiKey);

        // get Credentials
        setupServerKeys(new SDKInitCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys) {
                if(appgainSDKInitCallBack !=null){
                    appgainSDKInitCallBack.onSuccess();
                }
                Log.e("AppGaInitialize" , "success") ;
            }

            @Override
            public void onFailure(BaseResponse failure) {
                Log.e("AppGaInitialize" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                if (appgainSDKInitCallBack !=null)
                    appgainSDKInitCallBack.onFail(failure);
            }
        });


    }



    /**
     *  initialize  case with  given  user data
     *  save appID , appApiKey
     *  call parse  login with anonymous data if user have backend support  and generate its id
     *  save parse user  generated id
     *  init SDK keys
     *  save sdk keys
     */
    public  static void initializeWithParseAnonymous( Context context , String appID , final String appApiKey, String parseApplicationId , String serverName ,  AppgainSDKInitCallBack appgainSDKInitCallBack) throws Exception {
        initializeWithParse(context , appID , appApiKey  , parseApplicationId , serverName, (User) getTempUser() , appgainSDKInitCallBack) ;
    }



    /**
     *  initialize  case with  given  user data
     *  save appID , appApiKey
     * call parse  login with username  , email and password  if user have backend support  and generate its id
     *  save parse user  generated id
     *  init SDK keys
     *  save sdk keys
     */
    public  static void initializeWithParse(Context context , String appID , String appApiKey , String parseApplicationId , String serverName , final User user , final AppgainSDKInitCallBack appgainSDKInitCallBack) throws Exception{
        validateAppData(context,appID,appApiKey , parseApplicationId , serverName);
        setContext(context);
        setAppID(appID);
        setAppApiKey(appApiKey);

        if (isUserValid(user))
            getInstance().getPreferencesManager().saveUserProvidedData(user);

        parseSetup(parseApplicationId, serverName, new ParseLoginCallBack() {
            @Override
            public void onSuccess(String userId) {
                parsePushSetup(userId, new ParsePushSetupCallBack() {
                    @Override
                    public void onSucess() {
                        setupServerKeys(new SDKInitCallBack() {
                            @Override
                            public void onSuccess(SDKKeys sdkKeys) {
                                if(appgainSDKInitCallBack !=null){
                                    appgainSDKInitCallBack.onSuccess();
                                }
                                Log.d("initializeWithParse" , "success") ;
                            }

                            @Override
                            public void onFailure(BaseResponse failure) {
                                Log.e("initializeWithParse" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                                if (appgainSDKInitCallBack !=null)
                                    appgainSDKInitCallBack.onFail(failure);
                            }
                        });
                    }

                    @Override
                    public void onFailure(ParseException e) {
                        BaseResponse failure = new BaseResponse(e.getCode() + "", "Parse setup " + e.getMessage());
                        if (appgainSDKInitCallBack!=null)
                            appgainSDKInitCallBack.onFail(failure);
                        Log.e("initializeWithParse" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                    }
                });
            }

            @Override
            public void onFail(ParseException e) {
                if (e!=null){
                    Log.e("Appgain" , "NotificationChannels update " + e.toString());
                    if (appgainSDKInitCallBack !=null)
                        appgainSDKInitCallBack.onFail(new BaseResponse(e.getCode()+"" , e.getMessage()));
                }
            }
        });


    }

    private static boolean isUserValid(User user) throws Exception {
        if (user == null){
            return false;
        }
        else {
            Validator.isNull(user.getUsername() , "Initialize user name ");
            Validator.isNull(user.getEmail() , "Initialize user email ");
            Validator.isNull(user.getPassword() , "Initialize user password ");
            return true;
        }
    }

    private static void validateAppData(Context context, String appID, String appApiKey ) throws Exception {
        Validator.isNull(context , " context ");
        Validator.isNull(appID , " appgain app id ");
        Validator.isNull(appApiKey , " appgain app api Key ");
    }

    private static void validateAppData(Context context, String appID, String appApiKey , String server , String applicationId) throws Exception {
        Validator.isNull(context , " context ");
        Validator.isNull(appID , " appgain app id ");
        Validator.isNull(appApiKey , " appgain app api Key ");
        Validator.isNull(server , " parse server name ");
        Validator.isNull(applicationId , " parse application id ");
    }


    private static User getTempUser() {
        String randChars = createRandomString(6);
        return new User(randChars , randChars , randChars+"@user.temp");
    }

    public static String createRandomString(int length) {
        String alphabet= "abcdefghijklmnopqrstuvwxyz";
        StringBuilder s = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = alphabet.charAt(random.nextInt(26));
            s.append(c);
        }
        return s.toString();
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
        if (context == null){
            parseLoginCallBack.onFail(new ParseException(404 , "Application context cannot be null "));
            return;
        }

        //

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
        String user_id = getInstance().getPreferencesManager().getUserId() ;
        Log.e("user_id" , user_id+"") ;
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

    private static String userId ;
    public static void updateUserId(String userId , UpdateUserIdCallBack callBack){
        getInstance().userId = userId ;
        updateParseUserId(userId , callBack);
    }
    // update parse installation , users , notification channel
    private static void updateParseUserId(final String userId , final UpdateUserIdCallBack callBack) {

        // update instalition
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        final String old_userID = (String) installation.get("userId");
        Timber.e("updateParseUserId" +" old "+ old_userID + "  new  " + userId );
        // case Appgain sdk  initialized
        if (userId == null || (old_userID !=null&& old_userID.equals(userId)) ){
            return;
        }
        installation.put("userId" , userId);
        ArrayList arrayList = new ArrayList() ;
        arrayList.add("AE");
        installation.put("channels" , arrayList);
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("Appgain" , "ParseInstallation update : " +e.toString()) ;
                    // remove sttatic user id
                    getInstance().userId = null ;
                    if (callBack !=null)
                        callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                }else {
                    // ParseInstallation succeed
                    Timber.e("ParseInstallation update succeed" );
                    // update preference manger usr id
                    getInstance().getPreferencesManager().saveId(userId);
                    // update userId key on users object
                    updateUserIdInParseUserObject(userId , callBack);

                }
            }
        });
    }

    private static void updateUserIdInParseUserObject(final String userId, final UpdateUserIdCallBack callBack) {
        final ParseUser parseUser = ParseUser.getCurrentUser() ;
        parseUser.put("userId" , userId);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("Appgain" , "updateUserIdInParseUserObject update : " +e.toString()) ;
                    // remove sttatic user id
                    getInstance().userId = null ;
                    if (callBack !=null)
                        callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));

                }else {
                    // update notification chancels
                    updateNotificationsChannelUserID(userId,parseUser.getObjectId() , callBack);
                }
            }
        });
    }


    private static void updateNotificationsChannelUserID(final String userId, String old_userID, final UpdateUserIdCallBack callBack) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("NotificationChannels") ;
        parseQuery.whereEqualTo("userId" , old_userID )
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null){
                            Timber.e("parse objects size" + objects.size() );
                            if (!objects.isEmpty())
                            {
                                for (ParseObject parseObject: objects) {
                                    parseObject.put("userId" , userId);
                                    parseObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e!=null){
                                                Log.e("Appgain" , "NotificationChannels update " + e.toString());
                                                if (callBack !=null)
                                                    callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                                            }else {
                                                getInstance().getPreferencesManager().saveId(userId);
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
     *
     * get sdk keys
     * if SDKKeys exist call SDKInitCallBack onSuccess
     * else call AppgainParseAuth API
     *  save data
     */
    public static void setupServerKeys(final SDKInitCallBack sdkInitCallBack){
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
                        Timber.tag("setupServerKeys").e(response.toString());
                    }
                }
            });
        }
    }

    /** get credentials() :
     * call setupServerKeys()
     * call parseSetup()
     */

    public static void setupParse(String parseAppId , String serverName , final ParseLoginCallBack authListener){
        parseSetup(parseAppId , serverName, new ParseLoginCallBack() {
            @Override
            public void onSuccess(final String userId) {
                parsePushSetup(userId , new ParsePushSetupCallBack(){
                    @Override
                    public void onSucess() {
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
        setupServerKeys(new SDKInitCallBack() {
            @Override
            public void onSuccess(final SDKKeys sdkKeys) {
                // parse setup
                parseSetup(sdkKeys.getParseAppId(), sdkKeys.getParseServerUrl(), new ParseLoginCallBack() {
                    @Override
                    public void onSuccess(final String userId) {
                        parsePushSetup(userId , new ParsePushSetupCallBack(){
                            @Override
                            public void onSucess() {
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
    private static void parsePushSetup(String userId , final ParsePushSetupCallBack callBack) {
        // install parse for push notification

        final ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        if (TextUtils.isEmpty(installation.getString("userId")) ||  !TextUtils.equals(installation.getString("userId") , Appgain.getPreferencesManager().getUserId()) ){
            installation.put("user",ParseUser.getCurrentUser());
            installation.put("userId" ,userId);
            ArrayList arrayList = new ArrayList() ;
            arrayList.add("AE");
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
//                                FirebaseApp.initializeApp(context);
                                ParseFCM.register(getContext());
                            }
                        });


//                        saveFCMToken();
                        insertUserIdInParseUserObject(installation.getString("userId") , callBack);
                    }
                }
            });
        }else {
            callBack.onSucess();
        }
    }



    private static void insertUserIdInParseUserObject(final String userId, final ParsePushSetupCallBack callBack) {
        final ParseUser parseUser = ParseUser.getCurrentUser() ;
        parseUser.put("userId" , userId);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("Appgain" , "insertUserIdInParseUserObject: " +e.toString()) ;
                    // remove sttatic user id
                    getInstance().userId = null ;
                    if (callBack !=null)
                        callBack.onFailure(e);
                }else {
                    // save notification chancels
                    saveNotificationChannel(userId, callBack);
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
                    Timber.e( "Anonymous login sucess " );
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
                    getInstance().getPreferencesManager().saveId(parseUser.getObjectId());
                    if (parseLoginListener!=null){}
                    parseLoginListener.onSuccess(parseUser.getObjectId());
                } else {
                    e.printStackTrace();
                    Timber.e( "user login failed. "+e.toString());
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
        ParseQuery.getQuery("User").whereEqualTo("userEmail" , user.getEmail()).whereEqualTo("username" , user.getUsername()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e!=null){
                    if (e.getCode() == 119 && e.getMessage().contains("This user is not allowed to access non-existent class: User")){
                        signUpNewUser(user,parseSignUpListener);
                    }
                    else if (parseSignUpListener!=null)
                        parseSignUpListener.onFail(e);
                    Timber.e(e.toString());
                }else {
                    if (objects!=null && objects.size()!=0){
                        Timber.e("createParseUser : " + e.getCode() +e.toString() );
                        parseSignUpListener.onSuccess(user);
                    }else {
                        signUpNewUser(user,parseSignUpListener);
                    }
                }
            }
        });

    }

    private static void signUpNewUser(final User user, final ParseSignUpCallBack parseSignUpListener) {
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
    private static void saveNotificationChannel(final String notficationChanelUserId , final ParsePushSetupCallBack callBack) {
        if (TextUtils.isEmpty(notficationChanelUserId)){
            Timber.tag("saveNotificationChannel").e("userId =  null || empty  >> " + notficationChanelUserId);
            return;
        }

        ParseQuery.getQuery("NotificationChannels").whereEqualTo("userId" , notficationChanelUserId).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e!=null){
                    if (callBack!=null)
                        callBack.onFailure(e);
                    Timber.e(e.toString());
                }else {
                    if (objects!=null && objects.size()!=0){
                        Timber.tag("saveNotificationChannel").e("already exist" );
                        callBack.onSucess();
                    }else {
                        ParseObject notificationChannel = new ParseObject("NotificationChannels");
                        notificationChannel.put("type", "appPush");
                        notificationChannel.put("appPush", getNotificationStatus());
                        notificationChannel.put("userId", notficationChanelUserId);
                        getInstance().getPreferencesManager().saveId(notficationChanelUserId);
                        notificationChannel.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e!=null){
                                    Timber.tag("saveNotificationChannel").e(e.toString());
                                    callBack.onFailure(e);
                                }else {
                                    // ParseInstallation succeed
                                    Timber.tag("saveNotificationChannel").e("ParseInstallation succeed" );
                                    callBack.onSucess();
                                }
                            }
                        });
                    }
                }
            }
        });

    }
    public static  boolean getNotificationStatus(){
        return getPreferencesManager().getNotificationStatus();
    }
    public static void changeNotification(final boolean apppush , final UpdateNotificationCallBack callBack){
        ParseQuery.getQuery("NotificationChannels").whereEqualTo("userId" , Appgain.getPreferencesManager().getUserId()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e!=null){
                    if (callBack!=null)
                        callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                    Timber.e(e.toString());
                }else {
                    for (ParseObject object : objects){
                        if (!object.getString("type").equals("appPush"))
                            continue;
                        object.put("appPush" , apppush);
                        object.saveInBackground();
                    }
                    if (callBack!=null)
                        callBack.onSuccess();
                    getPreferencesManager().saveNotificationStatus(apppush);
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

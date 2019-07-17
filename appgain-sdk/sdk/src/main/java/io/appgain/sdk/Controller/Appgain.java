package io.appgain.sdk.Controller;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Random;

import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.User;
import io.appgain.sdk.Utils.Validator;
import io.appgain.sdk.interfaces.ParseAuthCallBack;
import io.appgain.sdk.interfaces.ParsePushSetupCallBack;
import io.appgain.sdk.interfaces.SDKInitCallBack;
import io.appgain.sdk.interfaces.AppgainSDKInitCallBack;
import io.appgain.sdk.interfaces.ParseLoginCallBack;
import io.appgain.sdk.interfaces.UpdateUserIdCallBack;
import timber.log.Timber;

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
     */
    public    static void initialize(final Context context , String appID , String appApiKey , final AppgainSDKInitCallBack appgainSDKInitCallBack) throws Exception{
        validateAppData(context,appID,appApiKey);
        setContext(context);
        // get Credentials
        AppgainSuitUtil.setupServerKeys(appID , appApiKey , new SDKInitCallBack() {
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
        initializeWithParse(context,appID,appApiKey,parseApplicationId,serverName,null,user , appgainSDKInitCallBack);
    }
    public  static void initializeWithParse(Context context , String appID , String appApiKey , String parseApplicationId , String serverName ,String clientKey ,  final User user , final AppgainSDKInitCallBack appgainSDKInitCallBack) throws Exception{
        validateAppData(context,appID,appApiKey , parseApplicationId , serverName);
        setContext(context);
        setAppID(appID);
        setAppApiKey(appApiKey);

        if (isUserValid(user))
            getInstance().getPreferencesManager().saveUserProvidedData(user);

        try {
            if (ParseUser.getCurrentUser() !=null)
                ParseUser.logOut();
        }catch (Exception e){
            e.printStackTrace();
        }

        ParseUtil.setupParse(parseApplicationId, serverName , clientKey, new ParseLoginCallBack() {
            @Override
            public void onSuccess(final String userId) {

                AppgainSuitUtil.setupServerKeys(new SDKInitCallBack() {
                    @Override
                    public void onSuccess(SDKKeys sdkKeys) {
                        if (user.getEmail().contains("@user.temp")){
                            if(appgainSDKInitCallBack !=null){
                                appgainSDKInitCallBack.onSuccess();
                            }
                            Log.d("initializeWithParse" , "success") ;
                        }else {
                            NotificationChannelUtils.saveEmailNotificationChannel(userId,user.getEmail(), new ParsePushSetupCallBack() {
                                @Override
                                public void onSuccess() {
                                    if(appgainSDKInitCallBack !=null){
                                        appgainSDKInitCallBack.onSuccess();
                                    }
                                    Log.d("initializeWithParse" , "success") ;
                                }

                                @Override
                                public void onFailure(ParseException e) {
                                    if(appgainSDKInitCallBack !=null){
                                        appgainSDKInitCallBack.onSuccess();
                                    }
                                    Log.d("initializeWithParse" , "success") ;
                                }
                            });

                        }
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
    public static PreferencesManager getPreferencesManager(){
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


    public static void updateUserId(String userId , UpdateUserIdCallBack callBack){
        ParseUtil.updateParseUserId(userId , callBack);
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
     * clear all PreferencesManager data
     */
    public  static  void clear (){
        getPreferencesManager().clear();
    }

    public static void AppgainParseAuth(ParseAuthCallBack authListener) {
        ParseUtil.AppgainParseAuth(authListener);
    }

    /**
     * class methods  end
     **/
    //------------------------------------------------------------------------------------------------------------------------------------

}

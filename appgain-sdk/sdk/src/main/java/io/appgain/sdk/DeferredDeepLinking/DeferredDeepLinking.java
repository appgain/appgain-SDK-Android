package io.appgain.sdk.DeferredDeepLinking;


import android.util.Log;
import android.webkit.WebView;

import com.parse.ParseUser;

import java.io.IOException;
import java.util.Map;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.AppgainSuitUtil;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Service.onRequestFailure;
import io.appgain.sdk.DeferredDeepLinking.ResponseModels.DeferredDeepLinkingResponse;
import io.appgain.sdk.Controller.Utils;
import io.appgain.sdk.Controller.Validator;
import io.appgain.sdk.interfaces.ParseAuthCallBack;
import io.appgain.sdk.interfaces.SDKInitCallBack;
import retrofit2.Response;
import timber.log.Timber;

import static io.appgain.sdk.Controller.Appgain.getInstance;

/*
  * Created by developers@appgain.io on 2/20/2018.
 */

/**
 * LandingPage class made to access to Smart Link Match  API
 */


public class DeferredDeepLinking {

    public  static DeferredDeepLinkingResponse matchedSDL = null;


    /**
     * enqueue()
     * call > AppgainParseAuth()
     *   AppgainParseAuth() > onSuccess()
     *      check sdkKeys
     *      call matchApi()
     *   AppgainParseAuth() > onFailure()
     *      log error
     */
    static public void enqueue(final DeferredDeepLinkingCallBack smartLinkMatchListener){
       Appgain.AppgainParseAuth(new ParseAuthCallBack() {
           @Override
           public void onSuccess(SDKKeys sdkKeys, String parseUserId) {
               if (sdkKeys !=null ){
                   matchApi(sdkKeys.getAppSubDomainName()  , parseUserId,smartLinkMatchListener);
               }else {
                   Timber.e("SDKKeys" + sdkKeys );
               }
           }

           @Override
           public void onFailure(BaseResponse failure) {
               Log.e("SmartLinkMatch" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
               smartLinkMatchListener.onFail(failure);
           }
       });
    }

    /**
     * @param internalUserId in normal case enqueue() pass user id created by Parse
     *               but in this case  enqueue() pass user id givin by user
     * enqueue()
     * call > AppgainParseAuth()
     *   AppgainParseAuth() > onSuccess()
     *      check sdkKeys
     *      call matchApi()
     *   AppgainParseAuth() > onFailure()
     *      log error
     *
     */

    static public void enqueue(final String internalUserId , final DeferredDeepLinkingCallBack smartLinkMatchListener){
        if (Validator.isNull(internalUserId)){
            enqueue(smartLinkMatchListener);
            return;
        }
        AppgainSuitUtil.setupServerKeys(new SDKInitCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys) {
                if (sdkKeys !=null ){
                    matchApi(sdkKeys.getAppSubDomainName()  , internalUserId ,smartLinkMatchListener);
                }else {
                    Timber.e("SDKKeys" + sdkKeys );
                }
            }

            @Override
            public void onFailure(BaseResponse failure) {
                Log.e("SmartLinkMatch" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                smartLinkMatchListener.onFail(failure);
            }
        });
    }


    /**
     * automatorApi() calling fireAutomator API
     * @param internalUserId may be from parse or giving by user  independent on enqueue() function
     */
    private static void matchApi(String subDomain, String internalUserId, final DeferredDeepLinkingCallBack smartLinkMatchListener){
        retrofit2.Call<DeferredDeepLinkingResponse> call ;
        if (internalUserId!=null){
            call = Injector.Api(
                    new WebView(getInstance().getContext()).getSettings().getUserAgentString()
            ).smartLinkMatch(Config.SMART_LINK_MATCH(subDomain) ,
                   internalUserId ,
                    Appgain.isFirstRun()
           ) ;
       }else {
         call = Injector.Api(new WebView(getInstance().getContext()).getSettings().getUserAgentString()).smartLinkMatch(
                   Config.SMART_LINK_MATCH(subDomain) ,
                 Appgain.isFirstRun()
           ) ;
       }
        call.enqueue(new CallbackWithRetry<DeferredDeepLinkingResponse>(call, new onRequestFailure() {
            @Override
            public void onFailure(Throwable t) {
                Timber.e("onFailure"  +  t.toString());
                if (smartLinkMatchListener!=null)
                    smartLinkMatchListener.onFail(new BaseResponse(Config.NETWORK_FAILED, t.getMessage()));
            }
        }) {
            @Override
            public void onResponse(retrofit2.Call<DeferredDeepLinkingResponse> call, Response<DeferredDeepLinkingResponse> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    if (smartLinkMatchListener!=null)
                        smartLinkMatchListener.onMatch(response.body());
                    updateUserRecodedWithSmartDeepLinkParams(response.body()) ;
                    if (Appgain.isFirstRun())
                    saveMatchedSDL(response.body());
                    changeAppFirstRun();
                }else {
                    clearMatchedSDL();
                    changeAppFirstRun();
                    if (smartLinkMatchListener!=null)
                        try {
                            smartLinkMatchListener.onFail(Utils.getAppGainFailure(response.errorBody().string()));
                            Timber.e("onFailure"  +  response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }

            }
        });
    }

    private static void changeAppFirstRun() {
        Appgain.getPreferencesManager().saveFirstRun();
    }


    private static void updateUserRecodedWithSmartDeepLinkParams(DeferredDeepLinkingResponse response) {
        try {
            for (final Map<String,String> map : response.getExtraData().getParams()){

                ParseUser currentUser = ParseUser.getCurrentUser();

                    if (map.entrySet().iterator().hasNext())
                    {
                        Map.Entry<String, String> entry = map.entrySet().iterator().next();
                        currentUser.put(entry.getKey() , entry.getValue());
                    }
                    if (response.getDeferredDeepLink()!=null && (Appgain.isFirstRun())){

                        currentUser.put("SDL",response.getDeferredDeepLink());
                        currentUser.put("smartlink_id",response.getSmart_link_id());
                    }
                    currentUser.saveInBackground();
            }
        }catch (Exception e){
            Timber.e("updateUserRecodedWithSmartDeepLinkParams error  : " +e.toString() );
            e.printStackTrace();
        }
    }

    public static DeferredDeepLinkingResponse getMatchedSDL() {
        return matchedSDL;
    }
    public static void saveMatchedSDL(DeferredDeepLinkingResponse matchedSDL) {
        DeferredDeepLinking.matchedSDL = matchedSDL;
    }
    public static void clearMatchedSDL() {
        saveMatchedSDL(null);
    }

}

package io.appgain.sdk.SmartLinkMatch;


import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Service.onRequestFailure;
import io.appgain.sdk.SmartLinkMatch.ResponseModels.SmartLinkMatchResponse;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.interfaces.ParseInitCallBack;
import io.appgain.sdk.interfaces.SDKInitCallBack;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 2/20/2018.
 */

/**
 * LandingPage class made to access to Smart Link Match  API
 */


public class SmartLinkMatch {


    /**
     * enqueue()
     * call > getCredentials()
     *   getCredentials() > onSuccess()
     *      check sdkKeys
     *      call matchApi()
     *   getCredentials() > onFailure()
     *      log error
     */
    static public void enqueue(final SmartLinkMatchCallBack smartLinkMatchListener){
       Appgain.getCredentials(new ParseInitCallBack() {
           @Override
           public void onSuccess(SDKKeys sdkKeys, String userId) {
               if (sdkKeys !=null ){
                   matchApi(sdkKeys.getAppSubDomainName()  , userId , Appgain.isFirstRun(),smartLinkMatchListener);
               }else {
                   Timber.e("SDKKeys" + sdkKeys );
               }
           }

           @Override
           public void onFailure(BaseResponse failure) {
               Log.e("SmartLinkMatch" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
               smartLinkMatchListener.onSmartLinkFail(failure);
           }
       });
    }

    /**
     * @param userId in normal case enqueue() pass user id created by Parse
     *               but in this case  enqueue() pass user id givin by user
     * enqueue()
     * call > getCredentials()
     *   getCredentials() > onSuccess()
     *      check sdkKeys
     *      call matchApi()
     *   getCredentials() > onFailure()
     *      log error
     *
     */

    static public void enqueue(final String userId , final SmartLinkMatchCallBack smartLinkMatchListener){
        Appgain.getSdkKeys(new SDKInitCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys) {
                if (sdkKeys !=null ){
                    matchApi(sdkKeys.getAppSubDomainName()  , userId , Appgain.isFirstRun(),smartLinkMatchListener);
                }else {
                    Timber.e("SDKKeys" + sdkKeys );
                }
            }

            @Override
            public void onFailure(BaseResponse failure) {
                Log.e("SmartLinkMatch" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                smartLinkMatchListener.onSmartLinkFail(failure);
            }
        });
    }


    /**
     * automatorApi() calling fireAutomator API
     * @param userId may be from parse or giving by user  independent on enqueue() function
     */
    private static void matchApi(String subDomain, String userId, boolean firstRun, final SmartLinkMatchCallBack smartLinkMatchListener){


        retrofit2.Call<SmartLinkMatchResponse> call ;
        if (userId!=null){
            call = Injector.Api(new WebView(Appgain.getInstance().getContext()).getSettings().getUserAgentString()).smartLinkMatch(
                   Config.SMART_LINK_MATCH(subDomain) ,
                   userId ,
                   firstRun
           ) ;
       }else {
         call = Injector.Api(new WebView(Appgain.getInstance().getContext()).getSettings().getUserAgentString()).smartLinkMatch(
                   Config.SMART_LINK_MATCH(subDomain) ,
                   firstRun
           ) ;
       }
        call.enqueue(new CallbackWithRetry<SmartLinkMatchResponse>(call, new onRequestFailure() {
            @Override
            public void onFailure(Throwable t) {
                Timber.e("onFailure"  +  t.toString());
                if (smartLinkMatchListener!=null)
                    smartLinkMatchListener.onSmartLinkFail(new BaseResponse(Config.NETWORK_FAILED, t.getMessage()));
            }
        }) {
            @Override
            public void onResponse(retrofit2.Call<SmartLinkMatchResponse> call, Response<SmartLinkMatchResponse> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    if (smartLinkMatchListener!=null)
                        smartLinkMatchListener.onSmartLinkCreated(response.body());
                }else {
                    if (smartLinkMatchListener!=null)
                        try {
                            smartLinkMatchListener.onSmartLinkFail(Utils.getAppGainFailure(response.errorBody().string()));
                            Timber.e("onFailure"  +  response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }

            }
        });
    }


}

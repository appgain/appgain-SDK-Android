package io.appgain.sdk.DeferredDeepLinking;


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
import io.appgain.sdk.DeferredDeepLinking.ResponseModels.DeferredDeepLinkingResponse;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.Utils.Validator;
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


public class DeferredDeepLinking {


    /**
     * enqueue()
     * call > getCredentials()
     *   getCredentials() > onSuccess()
     *      check sdkKeys
     *      call matchApi()
     *   getCredentials() > onFailure()
     *      log error
     */
    static public void enqueue(final DeferredDeepLinkingCallBack smartLinkMatchListener){
       Appgain.getCredentials(new ParseInitCallBack() {
           @Override
           public void onSuccess(SDKKeys sdkKeys, String parseUserId) {
               if (sdkKeys !=null ){
                   matchApi(sdkKeys.getAppSubDomainName()  , parseUserId, Appgain.isFirstRun(),smartLinkMatchListener);
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
     * call > getCredentials()
     *   getCredentials() > onSuccess()
     *      check sdkKeys
     *      call matchApi()
     *   getCredentials() > onFailure()
     *      log error
     *
     */

    static public void enqueue(final String internalUserId , final DeferredDeepLinkingCallBack smartLinkMatchListener){
        if (Validator.isNull(internalUserId)){
            enqueue(smartLinkMatchListener);
            return;
        }
        Appgain.getSdkKeys(new SDKInitCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys) {
                if (sdkKeys !=null ){
                    matchApi(sdkKeys.getAppSubDomainName()  , internalUserId , Appgain.isFirstRun(),smartLinkMatchListener);
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
    private static void matchApi(String subDomain, String internalUserId, boolean firstRun, final DeferredDeepLinkingCallBack smartLinkMatchListener){


        retrofit2.Call<DeferredDeepLinkingResponse> call ;
        if (internalUserId!=null){
            call = Injector.Api(new WebView(Appgain.getInstance().getContext()).getSettings().getUserAgentString()).smartLinkMatch(
                   Config.SMART_LINK_MATCH(subDomain) ,
                   internalUserId ,
                   firstRun
           ) ;
       }else {
         call = Injector.Api(new WebView(Appgain.getInstance().getContext()).getSettings().getUserAgentString()).smartLinkMatch(
                   Config.SMART_LINK_MATCH(subDomain) ,
                   firstRun
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
                }else {
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


}

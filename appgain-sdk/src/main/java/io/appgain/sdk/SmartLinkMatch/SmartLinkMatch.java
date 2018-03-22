package io.appgain.sdk.SmartLinkMatch;


import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;

import io.appgain.sdk.Controller.AppGain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.SdkKeys;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Service.onRequestFailure;
import io.appgain.sdk.SmartLinkCreate.Models.SmartLinkResponse;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.interfaces.Auth2Listener;
import io.appgain.sdk.interfaces.AuthListener;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Sotra on 2/20/2018.
 */

public class SmartLinkMatch {


    static public void enqueue(final SmartLinkMatchListener smartLinkMatchListener){

       AppGain.getCredentials(new Auth2Listener() {
           @Override
           public void onSuccess(SdkKeys sdkKeys, String userId) {
               if (sdkKeys !=null ){
                   matchApi(sdkKeys.getAppSubDomainName()  , userId , AppGain.isFirstRun(),smartLinkMatchListener);
               }else {
                   Timber.e("SdkKeys" + sdkKeys );
               }
           }

           @Override
           public void onFailure(BaseResponse failure) {
               Log.e("SmartLinkMatch" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
               smartLinkMatchListener.onSmartLinkFail(failure);
           }
       });
    }
    static public void enqueue(final String userId , final SmartLinkMatchListener smartLinkMatchListener){

       AppGain.getCredentials(new Auth2Listener() {
           @Override
           public void onSuccess(SdkKeys sdkKeys, String parseUserId) {
               if (sdkKeys !=null ){
                   matchApi(sdkKeys.getAppSubDomainName()  , userId , AppGain.isFirstRun(),smartLinkMatchListener);
               }else {
                   Timber.e("SdkKeys" + sdkKeys );
               }
           }

           @Override
           public void onFailure(BaseResponse failure) {
               Log.e("SmartLinkMatch" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
               smartLinkMatchListener.onSmartLinkFail(failure);
           }
       });
    }

    private static void matchApi(String subDomain, String userId, boolean firstRun, final SmartLinkMatchListener smartLinkMatchListener){


        retrofit2.Call<SmartLinkMatchResponse> call ;
        if (userId!=null){
            call = Injector.Api(new WebView(AppGain.getInstance().getContext()).getSettings().getUserAgentString()).smartLinkMatch(
                   Config.SMART_LINK_MATCH(subDomain) ,
                   userId ,
                   firstRun
           ) ;
       }else {
         call = Injector.Api(new WebView(AppGain.getInstance().getContext()).getSettings().getUserAgentString()).smartLinkMatch(
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

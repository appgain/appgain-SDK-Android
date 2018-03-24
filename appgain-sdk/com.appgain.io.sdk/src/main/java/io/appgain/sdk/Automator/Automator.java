package io.appgain.sdk.Automator;


import android.util.Log;

import java.io.IOException;

import io.appgain.sdk.Controller.AppGain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SdkKeys;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Service.onRequestFailure;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.interfaces.Auth2Listener;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Sotra on 3/15/2018.
 */

public class Automator {


    static public void enqueue(final String triggerPoint , final AutomatorListener automatorListener){

        AppGain.getCredentials(new Auth2Listener() {
            @Override
            public void onSuccess(SdkKeys sdkKeys, String userId) {
                if (sdkKeys !=null && userId !=null){
                    automatorApi(userId , triggerPoint , automatorListener);
                }else {
                    Timber.e("SdkKeys" + sdkKeys +" userId" + userId);
                    automatorListener.onFail(new BaseResponse("404",  Config.NO_BACKEND));
                }
            }

            @Override
            public void onFailure(BaseResponse failure) {
                Log.e("SmartLinkMatch" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                automatorListener.onFail(failure);
            }
        });
    }

    private static void automatorApi(String userId , String triggerPoint  , final AutomatorListener listener ){

        Call<AutomatorResponse> call = Injector.Api().fireAutomator(
                Config.AUTOMATOR(AppGain.getAppID(),triggerPoint , userId)
        );

        call.enqueue(new CallbackWithRetry<AutomatorResponse>(call, new onRequestFailure() {
            @Override
            public void onFailure(Throwable t) {
                if (listener!=null)
                    listener.onFail(new BaseResponse(Config.NETWORK_FAILED , t.getMessage()));
            }
        }) {
            @Override
            public void onResponse(Call<AutomatorResponse> call, Response<AutomatorResponse> response) {
                if (response.isSuccessful() && response.body() !=null){
                    if (listener!=null)
                    listener.onAutomatorCreated(response.body());
                }else {
                    if (listener!=null)
                        try {
                            listener.onFail(Utils.getAppGainFailure(response.errorBody().string()));
                            Timber.e("onFailure"  +  response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        });

    }
}

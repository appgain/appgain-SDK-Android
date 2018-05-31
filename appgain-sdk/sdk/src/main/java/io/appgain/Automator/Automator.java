package io.appgain.Automator;


import android.util.Log;

import java.io.IOException;

import io.appgain.Controller.Appgain;
import io.appgain.Controller.Config;
import io.appgain.Model.BaseResponse;
import io.appgain.Model.SDKKeys;
import io.appgain.Service.CallbackWithRetry;
import io.appgain.Service.Injector;
import io.appgain.Service.onRequestFailure;
import io.appgain.Utils.Utils;
import io.appgain.interfaces.ParseInitCallBack;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 3/15/2018.
 */

/**
 * Automator class made to access to Appgain Automator API
 */
public class Automator {

    /**
     * enqueue()
     * call > getCredentials()
     *   getCredentials() > onSuccess()
     *      check sdkKeys , userId
     *      call automatorApi()
     *   getCredentials() > onFailure()
     *      log error
     */

    static public void enqueue(final String triggerPoint , final AutomatorCallBack automatorCallBack){

        Appgain.getCredentials(new ParseInitCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys, String userId) {
                if (sdkKeys !=null && userId !=null){
                    automatorApi(userId , triggerPoint , automatorCallBack);
                }else {
                    Timber.e("SDKKeys" + sdkKeys +" userId" + userId);
                    automatorCallBack.onFail(new BaseResponse("404",  Config.NO_BACKEND));
                }
            }

            @Override
            public void onFailure(BaseResponse failure) {
                Log.e("SmartLinkMatch" , "Code " + failure.getStatus()+"Message: " +failure.getMessage()) ;
                automatorCallBack.onFail(failure);
            }
        });
    }

    /**
     * automatorApi() calling fireAutomator API
     * @param userId >  from parse auth automatorCallBack  (mandatory)
     */
    private static void automatorApi(String userId , String triggerPoint  , final AutomatorCallBack automatorCallBack ){

        Call<AutomatorResponse> call = Injector.Api().fireAutomator(
                Config.AUTOMATOR(Appgain.getAppID(),triggerPoint , userId)
        );

        call.enqueue(new CallbackWithRetry<AutomatorResponse>(call, new onRequestFailure() {
            @Override
            public void onFailure(Throwable t) {
                if (automatorCallBack!=null)
                    automatorCallBack.onFail(new BaseResponse(Config.NETWORK_FAILED , t.getMessage()));
            }
        }) {
            @Override
            public void onResponse(Call<AutomatorResponse> call, Response<AutomatorResponse> response) {
                if (response.isSuccessful() && response.body() !=null){
                    if (automatorCallBack!=null)
                    automatorCallBack.onAutomatorCreated(response.body());
                }else {
                    if (automatorCallBack!=null)
                        try {
                            automatorCallBack.onFail(Utils.getAppGainFailure(response.errorBody().string()));
                            Timber.e("onFailure"  +  response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        });

    }
}

package io.appgain.sdk.Automator;


import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Service.onRequestFailure;
import io.appgain.sdk.Controller.Utils;
import io.appgain.sdk.Controller.Validator;
import io.appgain.sdk.interfaces.ParseAuthCallBack;
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
     * call > AppgainParseAuth()
     *   AppgainParseAuth() > onSuccess()
     *      check sdkKeys , userId
     *      call automatorApi()
     *   AppgainParseAuth() > onFailure()
     *      log error
     */
    static public void enqueue(final String triggerPointName ,  final Map<String,String> personalizationFiled , final AutomatorCallBack automatorCallBack)throws Exception{
        Validator.isNull(triggerPointName , "trigger point name");
        automatorApi(Appgain.getPreferencesManager().getUserId(), triggerPointName , personalizationFiled ,automatorCallBack );
    }
    static public void enqueue(final String triggerPointName , @Nullable final String userId , final Map<String,String> personalizationFiled , final AutomatorCallBack automatorCallBack) throws Exception {

        Validator.isNull(userId , "user id ");
        Validator.isNull(triggerPointName , "trigger point name");
        automatorApi(userId , triggerPointName , personalizationFiled , automatorCallBack);
    }
    /**
     * automatorApi() calling fireAutomator API
     * @param userId >  from parse auth automatorCallBack  (mandatory)
     */
    private static void automatorApi(String userId , String triggerPoint  , Map<String,String> personalizationFileds, final AutomatorCallBack automatorCallBack ){

        Call<AutomatorResponse> call = Injector.Api().fireAutomator(
                Config.AUTOMATOR(Appgain.getAppID(),triggerPoint , userId) ,personalizationFileds
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
                    automatorCallBack.onAutomatorFired(response.body());
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

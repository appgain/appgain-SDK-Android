package io.appgain.PushNotfication;



import android.content.Intent;

import com.google.gson.Gson;

import java.io.Serializable;

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

import static com.parse.ParsePushBroadcastReceiver.KEY_PUSH_DATA;

/**
 * Created by developers@appgain.io on 3/15/2018.
 */


/**
 * call to access to AppGAin app-push  record status API
 */

class AppgainAppPushApi implements Serializable{


    /**
     *
     * @param Action  received from AppGainPushReceiver (open m dismiss )or from AppGainPushConversion  ( conversion )
     * @param data  intent from push receive method
     * recordPushStatus()  deliver request body to recoded status API
     *   call getCredentials
     *   create request bod
     *   call AppgainAppPushApi.enqueue
     */
    public  static void recordPushStatus(final String Action , final Intent data , final RecordPushStatusCallback recordPushStatusListener){
        Appgain.getCredentials(new ParseInitCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys, String userId) {

                if (sdkKeys ==null || userId ==null){
                    Timber.e("SDKKeys" + sdkKeys +" userId" + userId);
                    recordPushStatusListener.onFail(new BaseResponse("404" , Config.NO_BACKEND));
                    return;
                }
                PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(data.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);

                RecordStatusRequestBody pushModel = new RecordStatusRequestBody(
                        userId ,
                        pushDataReciveModel.getCampaignName() ,
                        pushDataReciveModel.getCampaignId() ,
                        new RecordStatusRequestBody.Action(Action , Config.NA)
                );
                AppgainAppPushApi.enqueue(pushModel , recordPushStatusListener);
            }

            @Override
            public void onFailure(BaseResponse failure) {
                recordPushStatusListener.onFail(failure);
            }
        });

    }


    /**
     * @param pushModel >  request body  for recordPushStatus API
     *enqueue() method to access to Appgain recordPushStatus API
     */
    public static void enqueue(RecordStatusRequestBody pushModel , final RecordPushStatusCallback callback){
        if( Appgain.getApiKey()==null){
            callback.onFail(new BaseResponse("404" , " Api key  not found  , make sure u initiate the sdk "));
            return;
        }
        if (Appgain.getAppID() == null){
            callback.onFail(new BaseResponse("404" , " App id not found  , make sure u initiate the sdk "));
            return;
        }
        Call<BaseResponse> call   = Injector.Api().recordPushStatus(
                Config.RECODE_PUSH_STATUS(Appgain.getAppID()),
                pushModel
        );
        call.enqueue(new CallbackWithRetry<BaseResponse>(call, new onRequestFailure() {
            @Override
            public void onFailure(Throwable t) {
                if (callback !=null)
                    callback.onFail(new BaseResponse(Config.NETWORK_FAILED, t.getMessage()));
            }
        }) {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body()!=null){
                    callback.onSuccess(response.body());
                }else {
                    if (callback!=null)
                        try {
                            callback.onSuccess(Utils.getAppGainFailure(response.errorBody().string()));
                            Timber.e("onFailure"  +  response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        });
    }
}

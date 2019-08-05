package io.appgain.sdk.PushNotfication;



import android.content.Intent;

import com.google.gson.Gson;

import java.io.Serializable;

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

import static com.parse.ParsePushBroadcastReceiver.KEY_PUSH_DATA;

/**
 * Created by developers@appgain.io on 3/15/2018.
 */


/**
 * call to access to AppGAin app-push  record status API
 */

public class AppgainAppPushApi implements Serializable{


    /**
     *
     * @param Action  received from AppGainPushReceiver (open , dismiss )or from AppGainPushConversion  ( conversion )
     * @param data  intent from push receive method
     * recordPushStatus()  deliver request body to recoded status API
     *   call AppgainParseAuth
     *   create request bod
     *   call AppgainAppPushApi.enqueue
     */
    public  static void recordPushStatus(final String Action , final Intent data , final RecordPushStatusCallback recordPushStatusListener) throws Exception {
        recordPushStatus(Action , data , null , recordPushStatusListener);
    }
    public  static void recordPushStatus(final String Action , final Intent data , final  String internalUSerId , final RecordPushStatusCallback recordPushStatusListener) throws Exception {
        Validator.isNull(Action , "recordPushStatus() action ") ;
        Validator.isNull(data , "recordPushStatus() data ") ;
        PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(data.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        recordPushStatus(Action , pushDataReciveModel.getCampaignName() , pushDataReciveModel.getCampaignId() , internalUSerId , recordPushStatusListener);
    }
    public  static void recordPushStatus(final String Action  , final String campaignName , final String campaignId , final RecordPushStatusCallback recordPushStatusListener) throws Exception {
        recordPushStatus(Action,campaignName,campaignId , null);
    }
    public  static void recordPushStatus(final String Action  , final String campaignName , final String campaignId, final  String internalUSerId , final RecordPushStatusCallback recordPushStatusListener) throws Exception {
        Validator.isNull(Action , "recordPushStatus() action ") ;
        Validator.isNull(campaignName , "recordPushStatus() action ") ;
        Validator.isNull(campaignId , "recordPushStatus() action ") ;
        Appgain.AppgainParseAuth(new ParseAuthCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys, String parseUserId) {

                if (sdkKeys ==null || parseUserId ==null){
                    Timber.e("SDKKeys" + sdkKeys +" userId" + parseUserId);
                    recordPushStatusListener.onFail(new BaseResponse("404" , Config.NO_BACKEND));
                    return;
                }

                RecordStatusRequestBody pushModel = new RecordStatusRequestBody(
                        internalUSerId== null ? parseUserId : internalUSerId,
                        campaignName ,
                        campaignId ,
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
    private static void enqueue(RecordStatusRequestBody pushModel , final RecordPushStatusCallback callback){
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

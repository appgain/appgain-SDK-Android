package io.appgain.sdk.PushNotfication;



import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.Serializable;

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

import static com.parse.ParsePushBroadcastReceiver.KEY_PUSH_DATA;

/**
 * Created by Sotra on 3/15/2018.
 */

 class PushApi implements Serializable{


    public  static void recordPushStatus(final String Action , final Intent data , final RecordPushStatusListener recordPushStatusListener){
        AppGain.getCredentials(new Auth2Listener() {
            @Override
            public void onSuccess(SdkKeys sdkKeys, String userId) {

                if (sdkKeys ==null || userId ==null){
                    Timber.e("SdkKeys" + sdkKeys +" userId" + userId);
                    recordPushStatusListener.onFail(new BaseResponse("404" , Config.NO_BACKEND));
                    return;
                }
                PushDataReciveModel pushDataReciveModel = new Gson().fromJson(data.getStringExtra(KEY_PUSH_DATA) , PushDataReciveModel.class);

                PushModel pushModel = new PushModel(
                        userId ,
                        pushDataReciveModel.getCampaignName() ,
                        pushDataReciveModel.getCampaignId() ,
                        new PushModel.Action(Action , Config.NA)
                );
                PushApi.enqueue(pushModel , recordPushStatusListener);
            }

            @Override
            public void onFailure(BaseResponse failure) {
                recordPushStatusListener.onFail(failure);
            }
        });

    }

    public static void enqueue(PushModel pushModel , final RecordPushStatusListener listener){
        if( AppGain.getApiKey()==null){
            listener.onFail(new BaseResponse("404" , " Api key  not found  , make sure u initiate the sdk "));
            return;
        }
        if (AppGain.getAppID() == null){
            listener.onFail(new BaseResponse("404" , " App id not found  , make sure u initiate the sdk "));
            return;
        }
        Call<BaseResponse> call   = Injector.Api().recordPushStatus(
                Config.RECODE_PUSH_STATUS(AppGain.getAppID()),
                pushModel
        );
        call.enqueue(new CallbackWithRetry<BaseResponse>(call, new onRequestFailure() {
            @Override
            public void onFailure(Throwable t) {
                if (listener !=null)
                    listener.onFail(new BaseResponse(Config.NETWORK_FAILED, t.getMessage()));
            }
        }) {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body()!=null){
                    listener.onSuccess(response.body());
                }else {
                    if (listener!=null)
                        try {
                            listener.onSuccess(Utils.getAppGainFailure(response.errorBody().string()));
                            Timber.e("onFailure"  +  response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        });
    }
}

package io.appgain.sdk.Controller;

import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.interfaces.SDKInitCallBack;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 7/2/2019.
 */
public class AppgainSuitUtil {

    /**
     *
     * get sdk keys
     * if SDKKeys exist call SDKInitCallBack onSuccess
     * else call AppgainParseAuth API
     *  save data
     */
    public static void setupServerKeys(String appID , String appApiKey  , final SDKInitCallBack sdkInitCallBack){
        SDKKeys appGainCredentials =Appgain. getPreferencesManager().getAppGainCredentials();
        if (appGainCredentials !=null && appGainCredentials.getAppID().equals(appID)){
            sdkInitCallBack.onSuccess(appGainCredentials);
        }
        else{
            Appgain.setAppID(appID);
            Appgain.setAppApiKey(appApiKey);
            Call<SDKKeys> call = Injector.Api().getCredentials(Config.CREDENTIALS_URL(Appgain.getAppID()));
            call.enqueue(new CallbackWithRetry<SDKKeys>(call , null) {
                @Override
                public void onResponse(Call<SDKKeys> call, final Response<SDKKeys> response) {
                    if (response.isSuccessful()&&response.body()!=null){
                        Appgain.getPreferencesManager().saveAppGainCredentials(response.body());
                        if (sdkInitCallBack!=null)
                            sdkInitCallBack.onSuccess(response.body());
                    }else {
                        if (sdkInitCallBack!=null)
                            try {
                                sdkInitCallBack.onFailure(Utils.getAppGainFailure(response.errorBody().string()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        Timber.tag("setupServerKeys").e(response.toString());
                    }
                }
            });
        }
    }

    public static void setupServerKeys(final SDKInitCallBack sdkInitCallBack){
        SDKKeys appGainCredentials =Appgain. getPreferencesManager().getAppGainCredentials();
        if (appGainCredentials !=null){
            sdkInitCallBack.onSuccess(appGainCredentials);
        }
        else{
            Call<SDKKeys> call = Injector.Api().getCredentials(Config.CREDENTIALS_URL(Appgain.getAppID()));
            call.enqueue(new CallbackWithRetry<SDKKeys>(call , null) {
                @Override
                public void onResponse(Call<SDKKeys> call, final Response<SDKKeys> response) {
                    if (response.isSuccessful()&&response.body()!=null){
                        Appgain.getPreferencesManager().saveAppGainCredentials(response.body());
                        if (sdkInitCallBack!=null)
                            sdkInitCallBack.onSuccess(response.body());
                    }else {
                        if (sdkInitCallBack!=null)
                            try {
                                sdkInitCallBack.onFailure(Utils.getAppGainFailure(response.errorBody().string()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        Timber.tag("setupServerKeys").e(response.toString());
                    }
                }
            });
        }
    }

}

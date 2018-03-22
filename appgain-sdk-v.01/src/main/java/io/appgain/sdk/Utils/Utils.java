package io.appgain.sdk.Utils;

import com.google.gson.Gson;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by Sotra on 2/13/2018.
 */

public class Utils {

    public static BaseResponse getAppGainFailure(String error){
        return  new Gson().fromJson(error, BaseResponse.class)    ;
    }
}

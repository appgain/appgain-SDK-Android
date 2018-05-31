package io.appgain.Utils;

import com.google.gson.Gson;

import io.appgain.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 2/13/2018.
 */

public class Utils {

    /**
     * getAppGainFailure convert Retrofit not  successful  response to BaseResponse Model
     */
    public static BaseResponse getAppGainFailure(String error){
        return  new Gson().fromJson(error, BaseResponse.class)    ;
    }
}

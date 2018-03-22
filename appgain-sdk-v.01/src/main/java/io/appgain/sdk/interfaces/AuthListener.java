package io.appgain.sdk.interfaces;


import io.appgain.sdk.Model.SdkKeys;
import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by Sotra on 2/13/2018.
 */

public interface AuthListener {
    void  onSuccess(SdkKeys sdkKeys);
    void  onFailure(BaseResponse failure);
}
package io.appgain.sdk.interfaces;


import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SdkKeys;

/**
 * Created by Sotra on 2/13/2018.
 */

public interface Auth2Listener {
    void  onSuccess(SdkKeys sdkKeys , String userId);
    void  onFailure(BaseResponse failure);
}
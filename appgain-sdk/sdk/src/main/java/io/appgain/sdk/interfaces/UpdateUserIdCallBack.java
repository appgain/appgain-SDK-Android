package io.appgain.sdk.interfaces;


import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;

/**
 * Created by developers@appgain.io on 2/13/2018.
 */

/**
 * SDK init API response interface
 */

public interface UpdateUserIdCallBack {
    void  onSuccess();
    void  onFailure(BaseResponse failure);
}
package io.appgain.interfaces;


import io.appgain.Model.SDKKeys;
import io.appgain.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 2/13/2018.
 */

/**
 * SDK init API response interface
 */

public interface SDKInitCallBack {
    void  onSuccess(SDKKeys sdkKeys);
    void  onFailure(BaseResponse failure);
}
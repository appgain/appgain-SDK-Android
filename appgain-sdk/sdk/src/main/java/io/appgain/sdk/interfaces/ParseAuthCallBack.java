package io.appgain.sdk.interfaces;


import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;

/**
 * Created by developers@appgain.io on 2/13/2018.
 */

/**
 * Parse init  response interface
 */

public interface ParseAuthCallBack {
    void  onSuccess(SDKKeys sdkKeys, String parseUserId);
    void  onFailure(BaseResponse failure);
}
package io.appgain.interfaces;


import io.appgain.Model.BaseResponse;
import io.appgain.Model.SDKKeys;

/**
 * Created by developers@appgain.io on 2/13/2018.
 */

/**
 * Parse init  response interface
 */

public interface ParseInitCallBack {
    void  onSuccess(SDKKeys sdkKeys , String userId);
    void  onFailure(BaseResponse failure);
}
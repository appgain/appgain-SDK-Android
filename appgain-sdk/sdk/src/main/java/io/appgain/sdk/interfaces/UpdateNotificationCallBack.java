package io.appgain.sdk.interfaces;


import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 2/13/2018.
 */

/**
 * SDK init API response interface
 */

public interface UpdateNotificationCallBack {
    void  onSuccess();
    void  onFailure(BaseResponse failure);
}
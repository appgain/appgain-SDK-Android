package io.appgain.sdk.interfaces;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 3/15/2018.
 */


/**
 * {@link Appgain} init response interface
 */
public interface AppgainSDKInitCallBack {
    void onSuccess();
    void onFail(BaseResponse failure);
}

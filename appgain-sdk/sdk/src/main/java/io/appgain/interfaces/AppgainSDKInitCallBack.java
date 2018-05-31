package io.appgain.interfaces;

import io.appgain.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 3/15/2018.
 */


/**
 * {@link io.appgain.Controller.Appgain} init response interface
 */
public interface AppgainSDKInitCallBack {
    void onSuccess();
    void onFail(BaseResponse failure);
}

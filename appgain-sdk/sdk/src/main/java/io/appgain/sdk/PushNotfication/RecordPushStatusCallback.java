package io.appgain.sdk.PushNotfication;

import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 * interface for  {@link AppgainAppPushApi} status Api  response
 */

public  interface RecordPushStatusCallback {
    void onSuccess(@Nullable BaseResponse response);
    void onFail(@Nullable BaseResponse failure);
}

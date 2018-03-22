package io.appgain.sdk.PushNotfication;

import android.support.annotation.Nullable;

import io.appgain.sdk.LandingPageCreate.LandingPageResponse;
import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by Sotra on 2/12/2018.
 */

public  interface RecordPushStatusListener {
    void onSuccess(@Nullable BaseResponse response);
    void onFail(@Nullable BaseResponse failure);
}

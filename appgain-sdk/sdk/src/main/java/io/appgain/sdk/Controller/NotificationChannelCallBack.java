package io.appgain.sdk.Controller;

import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse;

public  interface NotificationChannelCallBack {
    void onSucces();
    void onFail(@Nullable BaseResponse failure);
}

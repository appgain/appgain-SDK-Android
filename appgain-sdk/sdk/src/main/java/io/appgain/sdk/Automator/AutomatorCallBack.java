package io.appgain.sdk.Automator;

import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 * {@link Automator} API response interface
 */

public  interface AutomatorCallBack {
    void onAutomatorFired(@Nullable AutomatorResponse response);
    void onFail(@Nullable BaseResponse failure);
}

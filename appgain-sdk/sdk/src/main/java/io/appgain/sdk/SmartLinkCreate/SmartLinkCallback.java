package io.appgain.sdk.SmartLinkCreate;

import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.SmartLinkCreate.Models.SmartDeepLinkResponse;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */


/**
 *  {@link SmartDeepLinkCreator} success response callback
 */

public  interface SmartLinkCallback {
    void onSmartDeepLinkCreated(@Nullable SmartDeepLinkResponse response);
    void onSmartDeepLinkFail(@Nullable BaseResponse failure);
}

package io.appgain.SmartLinkCreate;

import android.support.annotation.Nullable;

import io.appgain.Model.BaseResponse;
import io.appgain.SmartLinkCreate.Models.SmartLinkResponse;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */


/**
 *  {@link SmartLinkCreator} success response callback
 */

public  interface SmartLinkCallback {
    void onSmartLinkCreated(@Nullable SmartLinkResponse response);
    void onSmartLinkFail(@Nullable BaseResponse failure);
}

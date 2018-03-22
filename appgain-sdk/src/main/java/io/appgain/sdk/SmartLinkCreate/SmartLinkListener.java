package io.appgain.sdk.SmartLinkCreate;

import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.SmartLinkCreate.Models.SmartLinkResponse;

/**
 * Created by Sotra on 2/12/2018.
 */

public  interface SmartLinkListener {
    void onSmartLinkCreated(@Nullable SmartLinkResponse response);
    void onSmartLinkFail(@Nullable BaseResponse failure);
}

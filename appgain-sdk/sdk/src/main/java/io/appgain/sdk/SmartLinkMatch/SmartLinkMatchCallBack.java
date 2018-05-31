package io.appgain.sdk.SmartLinkMatch;

import android.support.annotation.Nullable;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.SmartLinkMatch.ResponseModels.SmartLinkMatchResponse;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 * {@link SmartLinkMatch} API response interface
 */

public  interface SmartLinkMatchCallBack {
    void onSmartLinkCreated(@Nullable SmartLinkMatchResponse response);
    void onSmartLinkFail(@Nullable BaseResponse failure);
}

package io.appgain.sdk.DeferredDeepLinking;

import android.support.annotation.Nullable;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.DeferredDeepLinking.ResponseModels.DeferredDeepLinkingResponse;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 * {@link DeferredDeepLinking} API response interface
 */

public  interface DeferredDeepLinkingCallBack {
    void onMatch(@Nullable DeferredDeepLinkingResponse response);
    void onFail(@Nullable BaseResponse failure);
}

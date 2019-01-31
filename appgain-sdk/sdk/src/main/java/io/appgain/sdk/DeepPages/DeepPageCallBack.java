package io.appgain.sdk.DeepPages;

import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse; /**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 *  {@link DeepPage} success response callback
 */
public  interface DeepPageCallBack {
    void onDeepPageCreated(@Nullable DeepPageResponse response);
    void onDeepPageFail(@Nullable BaseResponse failure);
}

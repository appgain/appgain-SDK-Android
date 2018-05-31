package io.appgain.LandingPageCreate;

import android.support.annotation.Nullable;

import io.appgain.Model.BaseResponse; /**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 *  {@link LandingPage} success response callback
 */
public  interface LandingPageCallBack {
    void onLandingPageCreated(@Nullable LandingPageResponse response);
    void onLandingPageFail(@Nullable BaseResponse failure);
}

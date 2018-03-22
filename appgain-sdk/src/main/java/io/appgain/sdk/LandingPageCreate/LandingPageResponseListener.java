package io.appgain.sdk.LandingPageCreate;

import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by Sotra on 2/12/2018.
 */

public  interface LandingPageResponseListener {
    void onLandingPageCreated(@Nullable LandingPageResponse response);
    void onLandingPageFail(@Nullable BaseResponse failure);
}

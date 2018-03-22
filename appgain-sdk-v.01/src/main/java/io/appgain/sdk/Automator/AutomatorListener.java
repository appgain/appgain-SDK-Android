package io.appgain.sdk.Automator;

import android.support.annotation.Nullable;

import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.SmartLinkMatch.SmartLinkMatchResponse;

/**
 * Created by Sotra on 2/12/2018.
 */

public  interface AutomatorListener {
    void onAutomatorCreated(@Nullable AutomatorResponse response);
    void onFail(@Nullable BaseResponse failure);
}

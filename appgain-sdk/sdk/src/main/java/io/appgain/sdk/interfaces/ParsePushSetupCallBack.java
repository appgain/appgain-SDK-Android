package io.appgain.sdk.interfaces;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 5/16/2018.
 */

public interface ParsePushSetupCallBack {
    void onSucess();
    void onFailure(BaseResponse baseResponse);
}

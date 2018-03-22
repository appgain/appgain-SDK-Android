package io.appgain.sdk.interfaces;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by Sotra on 3/15/2018.
 */

public interface InitListener {
    void onSuccess();
    void onFail(BaseResponse failure);
}

package io.appgain.sdk.Automator;

import java.io.Serializable;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by Sotra on 3/15/2018.
 */

public class AutomatorResponse extends BaseResponse {
    @Override
    public String toString() {
        return "AutomatorResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

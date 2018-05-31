package io.appgain.sdk.Automator;

import java.io.Serializable;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 3/15/2018.
 */

/**
 *   {@link Automator } success response object
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

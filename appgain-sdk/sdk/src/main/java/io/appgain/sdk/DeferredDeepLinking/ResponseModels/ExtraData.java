
/*
 * created by developers@appgain.io
 */

package io.appgain.sdk.DeferredDeepLinking.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 *
 */

public class ExtraData {

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("params")
    @Expose
    private List<Map<String,String>> params;

    public List<Map<String, String>> getParams() {
        return params;
    }

    public void setParams(List<Map<String, String>> params) {
        this.params = params;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ExtraData{" +
                "userId='" + userId + '\'' +
                ", params=" + params +
                '}';
    }
}

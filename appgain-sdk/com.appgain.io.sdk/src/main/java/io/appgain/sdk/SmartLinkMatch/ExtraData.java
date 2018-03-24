
package io.appgain.sdk.SmartLinkMatch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtraData {

    @SerializedName("userId")
    @Expose
    private String userId;

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
                '}';
    }
}


/*
 * created by developers@appgain.io
 */

package io.appgain.sdk.SmartLinkMatch.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.appgain.sdk.SmartLinkMatch.ResponseModels.ExtraData;

public class SmartLinkMatchResponse implements Serializable {

    @SerializedName("extra_data")
    @Expose
    private ExtraData extraData;
    @SerializedName("smart_link_primary")
    @Expose
    private String smartLinkPrimary;

    public ExtraData getExtraData() {
        return extraData;
    }

    public void setExtraData(ExtraData extraData) {
        this.extraData = extraData;
    }

    public String getSmartLinkPrimary() {
        return smartLinkPrimary;
    }

    public void setSmartLinkPrimary(String smartLinkPrimary) {
        this.smartLinkPrimary = smartLinkPrimary;
    }

    @Override
    public String toString() {
        return "SmartLinkMatchResponse{" +
                "extraData=" + extraData +
                ", smartLinkPrimary='" + smartLinkPrimary + '\'' +
                '}';
    }
}

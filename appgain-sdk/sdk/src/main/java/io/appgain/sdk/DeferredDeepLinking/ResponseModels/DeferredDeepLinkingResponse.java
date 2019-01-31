
/*
 * created by developers@appgain.io
 */

package io.appgain.sdk.DeferredDeepLinking.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeferredDeepLinkingResponse implements Serializable {

    @SerializedName("extra_data")
    @Expose
    private ExtraData extraData;
    @SerializedName("smart_link_primary")
    @Expose
    private String deferredDeepLink;

    public ExtraData getExtraData() {
        return extraData;
    }

    public void setExtraData(ExtraData extraData) {
        this.extraData = extraData;
    }

    public String getDeferredDeepLink() {
        return deferredDeepLink;
    }

    public void setDeferredDeepLink(String smartLinkPrimary) {
        this.deferredDeepLink = smartLinkPrimary;
    }

    @Override
    public String toString() {
        return "SmartLinkMatchResponse{" +
                "extraData=" + extraData +
                ", deferredDeepLink='" + deferredDeepLink + '\'' +
                '}';
    }
}

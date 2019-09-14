
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

    @SerializedName("smart_link_id")
    @Expose
    private String smart_link_id;

    @SerializedName("smart_link_url")
    @Expose
    private String smart_link_url;

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

    public String getSmart_link_id() {
        return smart_link_id;
    }

    public void setSmart_link_id(String smart_link_id) {
        this.smart_link_id = smart_link_id;
    }

    public String getSmart_link_url() {
        return smart_link_url;
    }

    public void setSmart_link_url(String smart_link_url) {
        this.smart_link_url = smart_link_url;
    }

    @Override
    public String toString() {
        return "DeferredDeepLinkingResponse{" +
                "extraData=" + extraData +
                ", deferredDeepLink='" + deferredDeepLink + '\'' +
                ", smart_link_id='" + smart_link_id + '\'' +
                ", smart_link_url='" + smart_link_url + '\'' +
                '}';
    }
}

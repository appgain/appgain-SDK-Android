
package io.appgain.sdk.LandingPageCreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.appgain.sdk.SmartLinkMatch.ExtraData;

public class LandingPageResponse implements Serializable {

    @SerializedName("link")
    @Expose
    private String link;


    public String getLink() {
        return link;
    }
}

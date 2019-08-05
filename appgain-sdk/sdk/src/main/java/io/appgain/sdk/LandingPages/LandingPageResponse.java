
package io.appgain.sdk.LandingPages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by developers@appgain.io on 2/16/2018.
 */


/**
 * {@link LandingPage} success response object
 */


public class LandingPageResponse implements Serializable {

    @SerializedName("link")
    @Expose
    private String link;

    public String getLink() {
        return link;
    }
}

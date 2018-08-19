package io.appgain.sdk.SmartLinkCreate.Models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

public class MobileTarget implements Serializable {

    public static final String ANDROID = "Android";
    public static final String IOS = "IOS";
    //Required
    @NonNull
    @SerializedName("primary")
    private String primary ;

    //Required
    @NonNull
    @SerializedName("fallback")
    private String fallback ;



    public MobileTarget(String primary, String fallback) {
        this.primary = primary;
        this.fallback = fallback;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getFallback() {
        return fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }

    @Override
    public String toString() {
        return "MobileTarget{" +
                "primary='" + primary + '\'' +
                ", fallback='" + fallback + '\'' +
                '}';
    }
}

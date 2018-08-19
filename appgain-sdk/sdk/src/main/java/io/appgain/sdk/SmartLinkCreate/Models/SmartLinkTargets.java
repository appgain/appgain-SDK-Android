package io.appgain.sdk.SmartLinkCreate.Models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

public class SmartLinkTargets implements Serializable{
    //Required
    @NonNull
    @SerializedName("web")
    private String web ;

    //Required
    @NonNull
    @SerializedName("ios")
    private  MobileTarget ios ;

    //Required
    @NonNull
    @SerializedName("android")
    private  MobileTarget android ;

    public SmartLinkTargets() {
    }

    public SmartLinkTargets(@NonNull String web, @NonNull MobileTarget ios, @NonNull MobileTarget android) {
        this.web = web;
        this.ios = ios;
        this.android = android;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public MobileTarget getIos() {
        return ios;
    }

    public void setIos(MobileTarget ios) {
        this.ios = ios;
    }

    public MobileTarget getAndroid() {
        return android;
    }

    public void setAndroid(MobileTarget android) {
        this.android = android;
    }

    @Override
    public String toString() {
        return "SmartLinkTargets{" +
                "web='" + web + '\'' +
                ", ios=" + ios +
                ", android=" + android +
                '}';
    }
}

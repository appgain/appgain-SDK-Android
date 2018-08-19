package io.appgain.sdk.DeepPages.RequestModels;

import java.io.Serializable;

/**
 * Created by developers@appgain.io on 2/16/2018.
 */

public class Targets  implements Serializable{
    private String android ;
    private String web ;
    private String ios ;

    public Targets(String web , String android, String ios) {
        this.android = android;
        this.web = web;
        this.ios = ios;
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getIos() {
        return ios;
    }

    public void setIos(String ios) {
        this.ios = ios;
    }

    @Override
    public String toString() {
        return "Targets{" +
                "android='" + android + '\'' +
                ", web='" + web + '\'' +
                ", ios='" + ios + '\'' +
                '}';
    }
}

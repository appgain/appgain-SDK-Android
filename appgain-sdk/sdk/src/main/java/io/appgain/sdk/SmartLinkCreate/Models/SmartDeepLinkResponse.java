package io.appgain.sdk.SmartLinkCreate.Models;

import com.google.gson.annotations.SerializedName;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

public class SmartDeepLinkResponse extends BaseResponse {



    @SerializedName("smartlink")
    private  String smartDeepLink;


    public String getSmartDeepLink() {
        return smartDeepLink;
    }

    public void setSmartDeepLink(String smartDeepLink) {
        this.smartDeepLink = smartDeepLink;
    }


    @Override
    public String toString() {
        return "SmartLinkResponse{" +
                "status='" + status + '\'' +
                ", smartDeepLink='" + smartDeepLink + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

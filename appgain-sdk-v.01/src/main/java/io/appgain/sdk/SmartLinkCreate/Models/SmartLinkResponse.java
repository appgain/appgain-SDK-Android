package io.appgain.sdk.SmartLinkCreate.Models;

import com.google.gson.annotations.SerializedName;

import io.appgain.sdk.Model.BaseResponse;

/**
 * Created by Sotra on 2/12/2018.
 */

public class SmartLinkResponse extends BaseResponse {



    @SerializedName("smartlink")
    private  String smartLink;


    public String getSmartLink() {
        return smartLink;
    }

    public void setSmartLink(String smartLink) {
        this.smartLink = smartLink;
    }


    @Override
    public String toString() {
        return "SmartLinkResponse{" +
                "status='" + status + '\'' +
                ", smartLink='" + smartLink + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

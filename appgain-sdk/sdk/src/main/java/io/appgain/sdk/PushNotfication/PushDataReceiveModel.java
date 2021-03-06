package io.appgain.sdk.PushNotfication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by developers@appgain.io on 3/15/2018.
 */


/**
 * class to hold  Parse receive method data  request body API
 */


public class PushDataReceiveModel implements Serializable {


    @SerializedName("alert")
    @Expose
    private String alert;
    @SerializedName("sound")
    @Expose
    private String sound;
    @SerializedName("campaignName")
    @Expose
    private String campaignName;
    @SerializedName("campaign_id")
    @Expose
    private String campaignId;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("html")
    @Expose
    private String html;
    @SerializedName("orientation")
    @Expose
    private String orientation;

    public  static  final  String WEB_VIEW_TYPE = "webview" ;
    public  static  final  String VIDEO_TYPE = "video" ;
    public  static  final  String GIF_TYPE = "GIF" ;
    public static final String WEB_VIEW_HTML_TYPE = "HtmlWebView";



    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PushDataReceiveModel{" +
                "alert='" + alert + '\'' +
                ", sound='" + sound + '\'' +
                ", campaignName='" + campaignName + '\'' +
                ", campaignId='" + campaignId + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}

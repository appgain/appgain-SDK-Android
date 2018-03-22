package io.appgain.sdk.PushNotfication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sotra on 3/15/2018.
 */

public class PushDataReciveModel implements Serializable {
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

    @Override
    public String toString() {
        return "PushDataReciveModel{" +
                "alert='" + alert + '\'' +
                ", sound='" + sound + '\'' +
                ", campaignName='" + campaignName + '\'' +
                ", campaignId='" + campaignId + '\'' +
                '}';
    }
}

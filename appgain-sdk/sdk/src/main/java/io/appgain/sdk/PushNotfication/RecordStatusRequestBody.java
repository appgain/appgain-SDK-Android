package io.appgain.sdk.PushNotfication;

import java.io.Serializable;

/**
 * Created by developers@appgain.io on 3/15/2018.
 */


/**
 * class for recordPushStatus  request body API
 */

public class RecordStatusRequestBody implements Serializable{

    private final String channel= "apppush" ;
    private String userId ;
    private String campaign_name ;
    private String campaign_id ;
    private Action action ;

    public RecordStatusRequestBody(String userId, String campaign_name, String campaign_id, Action action) {
        this.userId = userId;
        this.campaign_name = campaign_name;
        this.campaign_id = campaign_id;
        this.action = action;
    }

    public String getChannel() {
        return channel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "RecordStatusRequestBody{" +
                "channel='" + channel + '\'' +
                ", userId='" + userId + '\'' +
                ", campaign_name='" + campaign_name + '\'' +
                ", campaign_id='" + campaign_id + '\'' +
                ", action=" + action +
                '}';
    }

    public static class Action implements  Serializable{
        private  String name  , value  ;

        public Action(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }



        public void setValue(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return "Action{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}

package io.appgain.sdk.SmartLinkCreate.Models;

import java.io.Serializable;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

public class SocialMediaDescription implements Serializable {
    private  String header ;

    public SocialMediaDescription() {
    }

    public SocialMediaDescription(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

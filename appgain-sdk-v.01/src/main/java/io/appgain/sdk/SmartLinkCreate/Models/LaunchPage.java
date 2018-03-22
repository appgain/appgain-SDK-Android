package io.appgain.sdk.SmartLinkCreate.Models;

import java.io.Serializable;

/**
 * Created by Sotra on 2/12/2018.
 */

public class LaunchPage implements Serializable {
    private  String header ;

    public LaunchPage() {
    }

    public LaunchPage(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

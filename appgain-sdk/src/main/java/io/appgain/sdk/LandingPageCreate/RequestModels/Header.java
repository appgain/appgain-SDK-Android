package io.appgain.sdk.LandingPageCreate.RequestModels;

import java.io.Serializable;

/**
 * Created by Sotra on 2/16/2018.
 */

public class Header implements Serializable{
    private  String text ;

    public Header(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Header{" +
                "text='" + text + '\'' +
                '}';
    }
}

package io.appgain.sdk.LandingPages.RequestModels;

import java.io.Serializable;

/**
 * Created by developers@appgain.io on 2/16/2018.
 */

final  public class Image  implements Serializable{

    private String src ;

    public Image(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "Image{" +
                "src='" + src + '\'' +
                '}';
    }
}

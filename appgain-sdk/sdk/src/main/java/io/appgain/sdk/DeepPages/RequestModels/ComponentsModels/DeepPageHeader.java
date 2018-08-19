package io.appgain.sdk.DeepPages.RequestModels.ComponentsModels;

import com.google.gson.annotations.SerializedName;

import io.appgain.sdk.DeepPages.RequestModels.Header;
import io.appgain.sdk.DeepPages.RequestModels.Image;

/**
 * Created by developers@appgain.io on 2/16/2018.
 */

final public class DeepPageHeader extends Component{
    @SerializedName("type")
    final private String type = "basic.h+logo" ;
    @SerializedName("logo")
    private Image logo;
    @SerializedName("header")
    private Header topic;

    public DeepPageHeader(String image, String header) {

        this.logo = new Image(image);
        this.topic = new Header(header);
    }

    public String getType() {
        return type;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public Header getTopic() {
        return topic;
    }

    public void setTopic(Header topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "LogoType{" +
                "type='" + type + '\'' +
                ", logo=" + logo +
                ", topic=" + topic +
                '}';
    }
}

package io.appgain.LandingPageCreate.RequestModels.ComponentsModels;

import com.google.gson.annotations.SerializedName;

import io.appgain.LandingPageCreate.RequestModels.Header;
import io.appgain.LandingPageCreate.RequestModels.Image;

/**
 * Created by Sotra on 2/16/2018.
 */

final public class LogoType  extends Component{
    @SerializedName("type")
    final private String type = "basic.h+logo" ;
    @SerializedName("logo")
    private Image image ;
    @SerializedName("header")
    private Header header ;

    public LogoType(String image, String header) {

        this.image = new Image(image);
        this.header = new Header(header);
    }

    public String getType() {
        return type;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "LogoType{" +
                "type='" + type + '\'' +
                ", image=" + image +
                ", header=" + header +
                '}';
    }
}

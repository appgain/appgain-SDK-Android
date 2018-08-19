package io.appgain.sdk.DeepPages.RequestModels.ComponentsModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by developers@appgain.io on 2/16/2018.
 */

final public class InfoImage extends Component {

    @SerializedName("type")
    final private String type = "basic.img" ;

    @SerializedName("src")
    private String imageUrl ;

    public InfoImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public String toString() {
        return "ImageType{" +
                "type='" + type + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

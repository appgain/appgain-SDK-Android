package io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sotra on 2/16/2018.
 */

final public class ParagraphType  extends Component{
    @SerializedName("type")
    final private String type = "basic.p" ;
    @SerializedName("content")
    private String content ;

    public ParagraphType(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ParagraphType{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

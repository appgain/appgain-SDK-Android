package io.appgain.LandingPageCreate.RequestModels.ComponentsModels;

import com.google.gson.annotations.SerializedName;

import io.appgain.LandingPageCreate.RequestModels.Targets;

/**
 * Created by Sotra on 2/16/2018.
 */

final public class ButtonType extends Component {

    @SerializedName("type")
    final private String type = "basic.btn" ;

    @SerializedName("text")
    private String text ;

    @SerializedName("alt-text")
    private String altText ;

    @SerializedName("targets")
    private Targets targets ;

    public ButtonType(String text, String altText, Targets targets) {
        this.text = text;
        this.altText = altText;
        this.targets = targets;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Targets getTargets() {
        return targets;
    }

    public void setTargets(Targets targets) {
        this.targets = targets;
    }

    @Override
    public String toString() {
        return "ButtonType{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", altText='" + altText + '\'' +
                ", targets=" + targets +
                '}';
    }
}

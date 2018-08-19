package io.appgain.sdk.DeepPages.RequestModels.ComponentsModels;

import com.google.gson.annotations.SerializedName;

import io.appgain.sdk.DeepPages.RequestModels.Targets;

/**
 * Created by developers@appgain.io on 2/16/2018.
 */

final public class ButtonType extends Component {

    @SerializedName("type")
    final private String type = "basic.btn" ;

    @SerializedName("text")
    private String text ;


    @SerializedName("targets")
    private Targets targets ;

    public ButtonType(String text, String web , String android, String ios) {
        this.text = text;
        this.targets = new Targets(web , android , ios);
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
                ", targets=" + targets +
                '}';
    }
}

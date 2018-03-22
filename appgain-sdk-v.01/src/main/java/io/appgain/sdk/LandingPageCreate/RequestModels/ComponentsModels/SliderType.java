package io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.appgain.sdk.LandingPageCreate.RequestModels.Image;

/**
 * Created by Sotra on 2/16/2018.
 */

final public class SliderType extends  Component {

    @SerializedName("type")
    final private String type = "basic.slider" ;

    @SerializedName("slider")
    private ArrayList<Image> slider ;

    // milliseconds
    @SerializedName("speed")
    private int speed ;

    // milliseconds
    @SerializedName("autoplay")
    private int autoPlay ;

    @SerializedName("direction")
    private String direction ;


    public SliderType(int speed, int autoPlay, String direction , String... images ) {
        this.speed = speed;
        this.autoPlay = autoPlay;
        this.direction = direction;
        this.slider = new ArrayList<>() ;
        for (String image  : images){
            this.slider.add(new Image(image)) ;
        }
    }


    public String getType() {
        return type;
    }

    public ArrayList<Image> getSlider() {
        return slider;
    }

    public void setSlider( String... images) {
        this.slider = new ArrayList<>() ;
        for (String image  : images){
            this.slider.add(new Image(image)) ;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(int autoPlay) {
        this.autoPlay = autoPlay;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

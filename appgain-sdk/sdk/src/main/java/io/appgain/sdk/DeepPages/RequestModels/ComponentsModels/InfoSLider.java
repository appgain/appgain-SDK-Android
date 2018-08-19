package io.appgain.sdk.DeepPages.RequestModels.ComponentsModels;

import android.media.effect.Effect;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.appgain.sdk.DeepPages.RequestModels.Image;

/**
 * Created by developers@appgain.io on 2/16/2018.
 */

final public class InfoSLider extends  Component {

    @SerializedName("type")
    final private String type = "basic.slider" ;

    @SerializedName("slider")
    private ArrayList<Image> slider ;

    // milliseconds
    @SerializedName("speed")
    private int speed =300;

    // milliseconds
    @SerializedName("autoplay")
    private int autoPlay=2000 ;

    // milliseconds
    @SerializedName("spacebetween")
    private int spacebetween=20 ;

    // milliseconds
    @SerializedName("watchSlidesProgress")
    private boolean watchSlidesProgress=false ;

    // milliseconds
    @SerializedName("effect")
    private String effect= SliderEffects.slide.name();


    @SerializedName("direction")
    private String direction = SliderDirection.horizontal.name() ;



    public InfoSLider(String... images) {
        this.slider = new ArrayList<>() ;
        for (String image  : images){
            this.slider.add(new Image(image)) ;
        }
    }

    public InfoSLider(int speed, int autoPlay, int spacebetween, boolean watchSlidesProgress, SliderEffects effect, SliderDirection direction , String... images) {
        this.speed = speed;
        this.autoPlay = autoPlay;
        this.spacebetween = spacebetween;
        this.watchSlidesProgress = watchSlidesProgress;
        this.effect = effect.name();
        this.direction = direction.name();
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


    public  enum SliderDirection {
        horizontal , vertical
    }

    public  enum SliderEffects {
        slide , fade , cube , coverflow  , flip
    }


}

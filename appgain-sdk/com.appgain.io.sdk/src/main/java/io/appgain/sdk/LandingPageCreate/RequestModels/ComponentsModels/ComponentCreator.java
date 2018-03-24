package io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels;

import java.io.Serializable;
import java.util.ArrayList;

import io.appgain.sdk.LandingPageCreate.RequestModels.Image;

/**
 * Created by Sotra on 2/16/2018.
 */

public  class ComponentCreator implements Serializable{

    static private LogoType logoType ;
    static private ButtonType buttonType ;
    static private ParagraphType paragraphType ;
    static  private SliderType sliderType ;
    static  private ImageType imageType ;

    public static void setLogoType(LogoType logoType) {
        ComponentCreator.logoType = logoType;
    }

    public static void setButtonType(ButtonType buttonType) {
        ComponentCreator.buttonType = buttonType;
    }

    public static void setParagraphType(ParagraphType paragraphType) {
        ComponentCreator.paragraphType = paragraphType;
    }

    public static void ImageType(SliderType sliderType) {
        ComponentCreator.sliderType = sliderType;
    }

    public static void ImageType(ImageType imageType) {
        ComponentCreator.imageType = imageType;
    }


    static public ArrayList<Component> getList(){
        ArrayList<Component> components = new ArrayList<>() ;
        if (logoType!=null)
        components.add(logoType);
        if (buttonType!=null)
            components.add(buttonType);
        if (paragraphType!=null)
            components.add(paragraphType);
        if (sliderType!=null)
            components.add(sliderType);
        if (imageType!=null)
            components.add(imageType);
        return components ;
    }
}

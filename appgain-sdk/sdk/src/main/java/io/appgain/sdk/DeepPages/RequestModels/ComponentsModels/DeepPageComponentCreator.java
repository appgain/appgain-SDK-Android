package io.appgain.sdk.DeepPages.RequestModels.ComponentsModels;

import java.io.Serializable;
import java.util.ArrayList;

import io.appgain.sdk.Config.Errors;
import io.appgain.sdk.Config.REGEX;
import io.appgain.sdk.DeepPages.RequestModels.Image;
import io.appgain.sdk.Utils.Validator;

/**
 * Created by developers@appgain.io on 2/16/2018.
 */

public  class DeepPageComponentCreator implements Serializable{

    static private DeepPageHeader header;
    static private ArrayList<ButtonType> buttonTypes = new ArrayList<>();
    static private DeepPageContent deepPageContent;
    static  private InfoSLider sliderType ;
    static  private InfoImage infoImage;

    public static void setHeader(DeepPageHeader logoType) {
        DeepPageComponentCreator.header = logoType;
    }

    public static void setButtonTypes(ButtonType buttonTypes) {
        DeepPageComponentCreator.buttonTypes.add(buttonTypes);
    }

    public static void setDeepPageContent(DeepPageContent deepPageContent) {
        DeepPageComponentCreator.deepPageContent = deepPageContent;
    }

    public static void SliderTybe(InfoSLider sliderType) {
        DeepPageComponentCreator.sliderType = sliderType;
    }

    public static void ImageType(InfoImage imageType) {
        DeepPageComponentCreator.infoImage = imageType;
    }


    public static DeepPageHeader getHeader() {
        return header;
    }

    public static ArrayList<ButtonType> getButtonTypes() {
        return buttonTypes;
    }

    public static DeepPageContent getDeepPageContent() {
        return deepPageContent;
    }

    public static InfoSLider getSliderType() {
        return sliderType;
    }

    public static InfoImage getInfoImage() {
        return infoImage;
    }

    static public ArrayList<Component> getList(){
        ArrayList<Component> components = new ArrayList<>() ;
        if (header !=null)
        components.add(header);
        if (deepPageContent !=null)
            components.add(deepPageContent);
        if (sliderType!=null)
            components.add(sliderType);
        if (infoImage !=null)
            components.add(infoImage);
        if (buttonTypes !=null)
            components.addAll(buttonTypes);
        return components ;
    }


    public void validate()throws Exception{
        
        // validate slider or image (info)
        if (infoImage==null && sliderType==null){
            throw new Exception(new NullPointerException(Errors.DEEP_PAGE_INFO_SECTION+Errors.NULL_ERROR));
        }else {
            if (infoImage!=null)
                validateInfoImage();
            else
                validatInfoeSlider();
        }
        // validate header
        validateHeader();
        // validate content
        validateContent();
        //button (optional)
        validateButtons();
    }



    void validateButtons() throws Exception{
        for (int i=0;i<buttonTypes.size();i++){
            validateButton(i);
        }
    }

    void validateButton( int pos ) throws Exception {

        ButtonType buttonType = buttonTypes.get(pos) ;
        String deepPageButtonError= Errors.DEEP_PAGE_BUTTON+" ("+ pos+")" ;

        // object validation
        Validator.isNull(buttonType, deepPageButtonError) ;

        // text validation
        Validator.isNull(buttonType.getText(), deepPageButtonError+Errors._TEXT) ;


        // web validation
        Validator.isNull(buttonType.getTargets().getWeb(), deepPageButtonError+Errors._WEB_TARGET) ;
        Validator.isMatch(buttonType.getTargets().getWeb(),REGEX.URL , deepPageButtonError+Errors._WEB_TARGET +  Errors.NOT_VALID_URL );

        // android validation
        Validator.isNull(buttonType.getTargets().getAndroid(), deepPageButtonError+Errors._ANDROID_TARGET) ;
        Validator.isMatch(buttonType.getTargets().getAndroid(),REGEX.URL , deepPageButtonError+Errors._ANDROID_TARGET +  Errors.NOT_VALID_URL );

        // ios validation
        Validator.isNull(buttonType.getTargets().getIos(), deepPageButtonError+Errors._IOS_TARGET) ;
        Validator.isMatch(buttonType.getTargets().getIos(),REGEX.URL , deepPageButtonError+Errors._IOS_TARGET +  Errors.NOT_VALID_URL );
    }



    void validateHeader() throws Exception{
        Validator.isNull(header, Errors.DEEP_PAGE_HEADER) ;

        Validator.isNull(header.getTopic().getText() , Errors.DEEP_PAGE_LOGO_TOPIC) ;
        Validator.isMatch(header.getTopic().getText() , REGEX.DEEP_PAGE_TOPIC_LENGTH_REGX, Errors.DEEP_PAGE_HEADER_TOPIC_REGX_ERROR );

        Validator.isNull(header.getLogo().getSrc() , Errors.DEEP_PAGE_LOGO_IMAGE) ;
        Validator.isMatch(header.getLogo().getSrc() , REGEX.URL, Errors.DEEP_PAGE_LOGO_IMAGE+Errors.NOT_VALID_URL );

    }


    void validateContent() throws Exception{
        Validator.isNull(deepPageContent, Errors.DEEP_PAGE_CONTENT) ;
        Validator.isNull(deepPageContent.getContent() , Errors.DEEP_PAGE_CONTENT) ;
        Validator.isMatch(deepPageContent.getContent() , REGEX.SUB_TOPIC_LENGTH_REGX, Errors.DEEP_PAGE_CONTENT_REGX_ERROR );
    }

    void validateInfoImage() throws Exception{
        Validator.isNull(infoImage, Errors.DEEP_PAGE_INFO_IMAGE) ;
        Validator.isNull(infoImage.getImageUrl(), Errors.DEEP_PAGE_INFO_IMAGE) ;
        Validator.isMatch(infoImage.getImageUrl(), REGEX.URL, Errors.DEEP_PAGE_INFO_IMAGE+Errors.NOT_VALID_URL );
    }


    void validatInfoeSlider() throws Exception{
        Validator.isNull(sliderType, Errors.DEEP_PAGE_INFO_SLIDER) ;
        Validator.isNull(sliderType.getSlider(), Errors.DEEP_PAGE_INFO_SLIDER) ;
        int pos = 0 ;
        for (Image image : sliderType.getSlider()){
            Validator.isNull(image.getSrc(), Errors.DEEP_PAGE_INFO_SLIDER_IMAGE + " pos:" +"("+pos+")") ;
            Validator.isMatch(image.getSrc(),REGEX.URL, Errors.DEEP_PAGE_INFO_SLIDER_IMAGE + " pos:" +"("+pos+") " + Errors.NOT_VALID_URL ) ;
            pos++ ;
        }
    }



}

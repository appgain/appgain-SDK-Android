package io.appgain.sdk.Config;

/**
 * Created by developers@appgain.io on 8/18/2018.
 */
public class Errors {
    public static final String NULL_ERROR = " cannot be empty or null ";
    private static final String SNART_DEEP_LINK = "Smart Deep Link";
    private static final String DEEP_PAGE = "Deep Page";
    public static final String DEEP_PAGE_HEADER = DEEP_PAGE+" header";
    public static final String DEEP_PAGE_CONTENT= DEEP_PAGE+" content";
    public static final String DEEP_PAGE_BUTTON= DEEP_PAGE+" button";

    public static final String DEEP_PAGE_LOGO_TOPIC = DEEP_PAGE+" topic text ";
    public static final String DEEP_PAGE_LOGO_IMAGE = DEEP_PAGE+" logo image";
    public static final String DEEP_PAGE_INFO_IMAGE = DEEP_PAGE+" info image";
    public static final String DEEP_PAGE_INFO_SLIDER = DEEP_PAGE+" info slider";
    public static final String DEEP_PAGE_INFO_SLIDER_IMAGE =DEEP_PAGE_INFO_SLIDER+" image";
    public static final String DEEP_PAGE_INFO_SECTION = Errors.DEEP_PAGE+" info section ";
    public static final String LANDING__PAGE_SM = Errors.DEEP_PAGE + " social media";

    public static String SMART_LINK_NAME = SNART_DEEP_LINK+" Name ";
    public static String LANDING_PAGE_LABEL = DEEP_PAGE+" Label ";
    public static final String SMART_LINK_NAME_REGX_ERROR = SMART_LINK_NAME+" must be from 4 to 100 character ";
    public static final String LANDING_PAGE_LABEL_REGX_ERROR = LANDING_PAGE_LABEL +" Label must be from 4 to 100 character ";
    public static final String DEEP_PAGE_HEADER_TOPIC_REGX_ERROR = DEEP_PAGE_LOGO_TOPIC+"  must be from 5 to 50 character ";
    public static final String DEEP_PAGE_CONTENT_REGX_ERROR = DEEP_PAGE_CONTENT+"  must be from 5 to 500 character ";
    public static final String SMART_LINK_SM_TITLE_REGX_ERROR = SNART_DEEP_LINK+" social media Title must be from 5 to 15 character ";
    public static final String SMART_LINK_SM_DESCRIPTION_REGX_ERROR = SNART_DEEP_LINK+"  social media description must be from 5 to 30 character ";
    public static final String SMART_LINK_SLUG_REGX_ERROR = SNART_DEEP_LINK+" Slug must be from 5 to 100 character ";
    public static final String LANDING__PAGE_SLUG_REGX_ERROR = DEEP_PAGE+" Slug must be from 5 to 100 character ";
    public static final String SMART_LINK_SLUG_CANNOT_CONTAINS = SNART_DEEP_LINK+" Slug Cannot Contains: ";
    public static final String LANDING_PAGE_SLUG_CANNOT_CONTAINS = DEEP_PAGE+" Slug Cannot Contains: ";
    public static final String WHITE_SPACE = "white space";
    public static final String NO_SMART_LINK_MOBILE_TARGETS = "Please add smart link web , android , IOS  targets to create smart link ";
    public static final String NOT_VALID_URL =  " is not valid url";
    public static final String MOBILE_TARGET_FALLBACK = " Mobile Target Fallback ";
    public static final String MOBILE_TARGET_PRIMARY = " Mobile Target Primary";
    public static String SMART_LINK_SM_TITLE = SNART_DEEP_LINK+" social Media Title ";
    public static String LANDING__PAGE_SM_TITLE = DEEP_PAGE+" social Media Title ";
    public static String SMART_LINK_SM_DESCRIPTION = SNART_DEEP_LINK+" social Media Description ";
    public static String LANDING_PAGE_SM_DESCRIPTION = DEEP_PAGE+" social Media Description ";
    public static String SMART_LINK_SM_IMAGE = SNART_DEEP_LINK+" social Media Image ";
    public static String LANDING_PAGE_SM_IMAGE = DEEP_PAGE+" social Media Image ";

    public static String SMART_LINK_WEB_TARGET = SNART_DEEP_LINK+" web Target";
    public static String _WEB_TARGET =" web Target";
    public static String _ANDROID_TARGET =" android Target";
    public static String _IOS_TARGET =" ios Target";
    public static String LINK_LENGHT = " Link length must be from 4 to 500 character";
    public static String _TEXT = " text";
}

package io.appgain.Controller; /**
 * Created by developers@appgain.io on 2/13/2018.
 */

/**
 * call for apis url preparing
 */
public final class Config {
    public  final static String API_URL ="https://api.appgain.io/" ;
    public  final static String APPS_URL =API_URL+"apps/" ;
    public  final static String APPGAIN_IO =".appgain.io/" ;
    public static final String NA = "NA";
    public static final String NO_BACKEND = "no backend for this suit id ";
    public static String NETWORK_FAILED = "network failed";



    public static String CREDENTIALS_URL(String appID) {
        return API_URL +appID+"/initSDK";
    }


    public static String LANDING_PAGE_CREATE(String appID) {
        return APPS_URL +appID+"/landingpages";
    }

    public static String SMART_LINK_CREATE(String appID) {
        return  APPS_URL+appID+"/smartlinks";
    }

    public static String SMART_LINK_MATCH(String appSubDomainName) {
        return "https://"+appSubDomainName+APPGAIN_IO+"smartlinks/match";
    }

    public static String RECODE_PUSH_STATUS(String appID ) {
        return "https://notify"+APPGAIN_IO+appID + "/recordstatus"  ;
    }

    public static String AUTOMATOR(String appID , String triggerName , String userId) {
        return "https://automator"+APPGAIN_IO+"automessages/"+appID + "/firevent/"+triggerName+"/"+userId  ;
    }
}

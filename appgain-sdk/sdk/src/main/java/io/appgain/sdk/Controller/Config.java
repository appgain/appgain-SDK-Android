package io.appgain.sdk.Controller; /**
 * Created by developers@appgain.io on 2/13/2018.
 */

import android.content.ComponentName;
import android.content.Intent;

/**
 * call for apis url preparing
 */
public final class Config {


    public static final String USER_ID_KEY = "userId";
    public static final String USERS_TABLE = "_User";
    public   static String API_URL ="https://api.appgain.io/" ;
    public   static String APPS_URL =API_URL+"apps/" ;
    public   static String APPGAIN_IO =".appgain.io/" ;
    public static final String NA = "NA";
    public static final String NO_BACKEND = "no backend for this suit id ";
    public static String NETWORK_FAILED = "network failed";
    public static boolean io =true;
    public final static String PurchaseTransactions = "PurchaseTransactions";
    public final static String AppSession = "appSessions";
    public final static String NotificationChannels = "NotificationChannels";


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

    public static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
    };

}

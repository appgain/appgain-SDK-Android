package io.appgain.sdk.InstallationReferrer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

import io.appgain.sdk.Controller.Appgain;


/**
 * Created by developers@appgain.io on 6/23/2019.
 */
public class ReferrerReciver extends BroadcastReceiver {
// add this to receive install referrer from google play
//        <!-- Used for install referrer tracking-->
//        <receiver
//    android:name="io.appgain.sdk.InstallationReferrer.ReferrerReciver"
//    android:exported="true">
//            <intent-filter>
//                <action android:name="com.android.vending.INSTALL_REFERRER" />
//                <action android:name="android.intent.action.PACKAGE_FIRST_LAUNCH" />
//            </intent-filter>
//        </receiver>
//


//    adb shell
//    "referrer" "utm_source=test_source\&utm_medium=test_medium\&utm_term=test_term\&utm_content=test_content\&utm_campaign=test_name"
//    am broadcast -a com.android.vending.INSTALL_REFERRER -n com.quantatil.ikhar/io.appgain.sdk.InstallationReferrer.ReferrerReciver --es  referrer "utm_source=test"


    @Override
    public void onReceive(Context context, Intent intent) {
       if (intent.getExtras() !=null){
           ParseUser currentUser = ParseUser.getCurrentUser();
           for (String key: intent.getExtras().keySet()) {
               Log.d(TAG , "Extras key " +  key + " value: " + intent.getExtras().get(key)) ;
               currentUser.put("E_"+key , intent.getExtras().get(key)+"");
           }
           currentUser.saveInBackground();
       }
        getUtmSource();
    }

    public static String TAG = "InstallReferrer" ;
    public  static  void getUtmSource (){
        final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(Appgain.getContext()).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        try {
                            Log.d(TAG, "InstallReferrer connected ");
                            ReferrerDetails response = referrerClient.getInstallReferrer();
                            Log.d(TAG,  "referrer " +   response.getInstallReferrer() );
                            Log.d(TAG,  "click time" +   response.getReferrerClickTimestampSeconds() );
                            Log.d(TAG,  "install time " +   response.getInstallBeginTimestampSeconds() );
                            // todo update parse userObject
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            Map<String , String> sourceMap = getMap(response.getInstallReferrer());
                            for (String key:  sourceMap.keySet()) {
                                String value = sourceMap.get(key);
                                Log.d(TAG , "google_play_utm_ key " +  key + " value: " + value) ;
                                currentUser.put(key , value);
                            }
                            currentUser.put("google_play_utm_source_time" , response.getInstallBeginTimestampSeconds());
                            currentUser.saveInBackground();
                            referrerClient.endConnection();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        Log.d(TAG, "InstallReferrer not supported");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        Log.d(TAG, "Unable to connect to the service");
                        break;
                    default:
                        Log.d(TAG, "responseCode not found.");
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private static Map<String, String> getMap(String installReferrer) {
        Map<String , String> map = new HashMap<>() ;
        String[] parts = installReferrer.split(",");
        for (String part  : parts){
            String[] source = part.split("=");
            if (source.length ==2){
                map.put(source[0] , source[1]) ;
            }
        }
        return map;
    }


}

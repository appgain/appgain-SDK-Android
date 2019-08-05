package io.appgain.sdk.PowerUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;


import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.R;

import static android.content.Context.POWER_SERVICE;

/**
 * Created by developers@appgain.io on 6/7/2019.
 */
public class PowerUtils {
    public static void startPowerMangerIntents(Activity activity ) {
        startPowerMangerIntents(activity,null);
    }

    /**
     *
     * @param activity
     * @param message specify a message for user to request operating in background
     */
    public static void startPowerMangerIntents(Activity activity , String message) {
        if (message == null){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Please allow ")
                    .append(activity.getPackageName())
                    .append("to receive notifications on background");
            message = stringBuilder.toString();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);
            if (powerManager != null && !powerManager.isIgnoringBatteryOptimizations(activity.getPackageName())){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                if (intent.resolveActivity(activity.getPackageManager())!=null){
                   startDialog(activity , message , intent) ;
                }else {
                    callLaunchersSystemPowerManageActivities(activity , message);
                }
            }
        }else {
            if (!Appgain.getInstance().getPreferencesManager().getIsBatteryOptimized())
                callLaunchersSystemPowerManageActivities(activity , message);
        }
    }


    private static void callLaunchersSystemPowerManageActivities(Activity activity , String message) {
      callLaunchersSystemPowerManageActivities(activity , message , null , null);
    }
    private static void callLaunchersSystemPowerManageActivities(Activity activity , String message , String confirmMessage  , String cancelMessage) {
        for (final Intent powerIntent : Config.POWERMANAGER_INTENTS){
            try {
                if (activity.getPackageManager().resolveActivity(powerIntent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    startDialog(activity , message,confirmMessage , cancelMessage , powerIntent);
                    return;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private static void startDialog(Activity activity, String message , Intent intent) {
       startDialog(activity, message ,activity.getString( R.string.confirm) , activity.getString(R.string.cancel) , intent);
    }
    private static void startDialog(final Activity activity, String message , String confirmMessage  , String cancelMessage, final Intent intent) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(confirmMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Appgain.getInstance().getPreferencesManager().setIsBatteryOptimized(false);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton(cancelMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Appgain.getInstance().getPreferencesManager().setIsBatteryOptimized(false);
                    }
                })
                .create().show();
    }

}

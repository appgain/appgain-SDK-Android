package io.appgain.sdk.PushNotfication.OverKeyGuardActivities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.R;

import static android.content.Context.POWER_SERVICE;

class PowerMangerUtils {

    private PowerManager.WakeLock wakeLock;
    private Activity hostActivity  ;

     private PowerMangerUtils(Activity hostActivity) {
        this.hostActivity = hostActivity ;
    }

    public static PowerMangerUtils getInstance(Activity hostActivity){
        return new PowerMangerUtils(hostActivity) ;
    }

    void prepareWindow(){
        Window window= this.hostActivity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }


    PowerManager.WakeLock getLock(){
        if (wakeLock!=null){
            return  wakeLock ;
        }else {
            PowerManager pm = (PowerManager) this.hostActivity.getSystemService(POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, Appgain.getContext().getPackageName());
            return  wakeLock ;
        }
    }
    void wakeLock(){
        this.getLock().acquire();
    }
    void release(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               getLock().release();
            }
        },1000);
    }
}

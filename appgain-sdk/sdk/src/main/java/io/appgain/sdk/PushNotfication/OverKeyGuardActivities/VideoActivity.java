package io.appgain.sdk.PushNotfication.OverKeyGuardActivities;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

import com.google.android.exoplayer2.ui.PlayerView;

import io.appgain.sdk.R;
import io.appgain.sdk.VideoUtils.ExoplayerUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class VideoActivity extends AppCompatActivity {
    private static final String ORIENTATION_KEY = "ORIENTATION";
    private final  PowerMangerUtils  powerMangerUtils =  PowerMangerUtils.getInstance(this); ;
    String webViewData;
    public static  final  String WEBVIEW_DATA = "url" ;
    private String oriantaion;
    private PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        powerMangerUtils.prepareWindow();
        super.onCreate(savedInstanceState);
        setupExtras(savedInstanceState);
            // set content view
            setContentView(R.layout.activity_lock_video_view);
            playNotificationSound();
            wakeLook();
            binViews();
            setupCloseButton();
    }


    private void playNotificationSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setupCloseButton() {
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    boolean canResolveIntent(Intent intent){
        if (intent.resolveActivity(getPackageManager()) != null) {
            return true;
        }
        return  false;
    }

    private void binViews() {
        // bind webview
        playerView = findViewById(R.id.playerView) ;
        ExoplayerUtils.play(getApplicationContext() , this , playerView  , true , webViewData) ;
    }

    private void wakeLook() {
        powerMangerUtils.wakeLock();
        powerMangerUtils.release();
    }

    private void setupExtras(Bundle savedInstanceState) {
        // check webview url
        if (getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().getString(WEBVIEW_DATA) != null){
            webViewData = getIntent().getExtras().getString(WEBVIEW_DATA);
            oriantaion = getIntent().getExtras().getString(ORIENTATION_KEY);
            if (oriantaion!=null)
                switch (oriantaion){
                    case "portrait": setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); break;
                    case "landscape": setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); break;
                }
        }
        else if (savedInstanceState==null)
            finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public static void start(Context context, String data, String oriantation){
        Intent intent = new Intent(context , VideoActivity.class) ;
        intent.putExtra(WEBVIEW_DATA, data) ;
        intent.putExtra(ORIENTATION_KEY, oriantation) ;
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

package io.appgain.sdk.PushNotfication.OverKeyGuardActivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import io.appgain.sdk.R;

public class YoutubeVideoActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    // use this wizard to enable youtube api  https://console.developers.google.com/flows/enableapi?apiid=youtube
    // the create new key with your SHA1
    static private  String DEVELOPER_KEY ;
    static private  String VIDEO = null;
    private static String VIDEO_ID = "VIDEO_ID";
    private static String DEV_KEY = "DEV_KEY";
    private int RECOVERY_DIALOG_REQUEST =1 ;
    private final  PowerMangerUtils  powerMangerUtils =  PowerMangerUtils.getInstance(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState==null){
            if (getIntent().getExtras().get(VIDEO_ID) == null)
                finish();
            else
                VIDEO = getIntent().getExtras().getString(VIDEO_ID);

            if (getIntent().getExtras().get(DEV_KEY) == null){
                Log.e("YoutubeVideoActivity" ,"onCreate : developer key not exist please check appgain.io docs " , new NullPointerException("developer key not exist") );
                finish();
            }

            else
                DEVELOPER_KEY = getIntent().getExtras().getString(DEV_KEY);
        }

        setContentView(R.layout.activity_lock_youtube);
        powerMangerUtils.wakeLock();
        powerMangerUtils.release();
        YouTubePlayerView youTubeView = findViewById(R.id.youtube_player_view);
        youTubeView.initialize(DEVELOPER_KEY, this);
        youTubeView.requestFocus();
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult error) {
        if (error.isUserRecoverableError()) {
            error.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(0), error.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        player.setShowFullscreenButton(false);
        player.loadVideo(VIDEO);

    }


    public static void start(Context context , String video_id , String devKey ){
        Intent intent = new Intent(context , YoutubeVideoActivity.class) ;
        intent.putExtra(VIDEO_ID, video_id) ;
        intent.putExtra(DEV_KEY, devKey) ;
        context.startActivity(intent);
    }
}
package io.appgain.sdk.PushNotfication.OverKeyGuardActivities;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import io.appgain.sdk.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class GIFActivity extends AppCompatActivity {

    private final  PowerMangerUtils  powerMangerUtils =  PowerMangerUtils.getInstance(this);
    ImageView overLockImageView ;
    String url;
    public static  final  String URL_KEY = "url" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        powerMangerUtils.prepareWindow();
        super.onCreate(savedInstanceState);
        setupExtras(savedInstanceState);
        // set content view
        setContentView(R.layout.activity_lock_gif);
        playNotificationSound();
        wakeLook();
        bindViews();
        setupGIF();
        setupFinish();

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

    private void bindViews() {
        // bind imageview
        overLockImageView = findViewById(R.id.overLockImageView) ;
    }

    private void wakeLook() {
        powerMangerUtils.wakeLock();
        powerMangerUtils.release();
    }

    private void setupExtras(Bundle savedInstanceState) {
        // check webview url
        if (getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().getString(URL_KEY) != null)
            url = getIntent().getExtras().getString(URL_KEY);
        else if (savedInstanceState==null)
            finish();
    }

    private void setupGIF() {
        // init imageview
        Glide
                .with(overLockImageView.getContext())
                .asGif()
                .load(url)
                .apply(new RequestOptions()
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.gif)
                        .error(R.drawable.brlink)
                )
                .into(overLockImageView);
    }

    private void setupFinish() {
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public static void start(Context context , String url ){
        Intent intent = new Intent(context , GIFActivity.class) ;
        intent.putExtra(URL_KEY, url) ;
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}

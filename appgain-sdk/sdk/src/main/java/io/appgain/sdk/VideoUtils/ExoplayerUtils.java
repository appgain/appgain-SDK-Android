package io.appgain.sdk.VideoUtils;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class ExoplayerUtils implements LifecycleObserver {

    private static ExoplayerUtils mInstance = null;
    private String TAG = "Exoplayer";
    private SimpleExoPlayer player;
    PlayerView playerView;
    String uriString = "";
//    private Context context;

    private boolean playWhenReady;
    //  currentWindow  ,  playbackPosition  use these to resume the playback state with setPlayWhenReady() and seekTo()
    private int currentWindow =0;
    private long playbackPosition=0;
    private MediaSource mediaSource;
    private LifecycleOwner lifecycleOwner;
    private Context context;


    public ExoplayerUtils(Context context, LifecycleOwner lifecycleOwner, PlayerView playerView, boolean playWhenReady, String uri) {
        this.playerView = playerView ;
        this.lifecycleOwner = lifecycleOwner ;
        this.playWhenReady = playWhenReady ;
        this.context = context ;
        this.uriString = uri ;
    }


    public static ExoplayerUtils play(Context context , LifecycleOwner lifecycleOwner , PlayerView playerView , boolean playWhenReady , String uri ) {
        mInstance =   new ExoplayerUtils(context , lifecycleOwner, playerView , playWhenReady ,uri) ;
        mInstance.lifecycleOwner.getLifecycle().addObserver(mInstance);
        return  mInstance  ;

    }
















    // streaming  Adaptive playback implies estimating available network bandwidth based on measured download speed
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private void initializePlayer() {
        if (player == null) {
            // a factory to create an AdaptiveVideoTrackSelection
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory),
                    new DefaultLoadControl());
        }
        playerView.setUseController(true);
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        // An ExoPlayer instance is waiting for media. We need to create a MediaSource
        //ExtractorMediaSource for an MP3 file
        Uri uri = Uri.parse(uriString);
          buildMediaSource(uri, new MediaSourceCallBack() {
            @Override
            public void deliverMediaSource(MediaSource mediaSource) {
                mInstance.mediaSource = mediaSource ;
                player.prepare(mediaSource, true, false);
            }
        });

    }
    @SuppressLint("StaticFieldLeak")
    private void buildMediaSource(Uri uri  , final MediaSourceCallBack mediaSourceCallBack) {
        String userAgent = "appgain" ;

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
            mediaSourceCallBack.deliverMediaSource(
                    new ExtractorMediaSource.Factory(
                            new DefaultHttpDataSourceFactory(userAgent)).
                            createMediaSource(uri)
            );
        } else if (uri.getLastPathSegment().contains("m3u8")) {
            DataSource.Factory manifestDataSourceFactory =
                    new DefaultHttpDataSourceFactory("ua");
            mediaSourceCallBack.deliverMediaSource(
                    new HlsMediaSource.Factory(
                            manifestDataSourceFactory).createMediaSource(uri)
            );
        }else {

            DataSource.Factory manifestDataSourceFactory =
                    new DefaultHttpDataSourceFactory(userAgent);
            DashChunkSource.Factory dashChunkSourceFactory =
                    new DefaultDashChunkSource.Factory(
                            new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER));

            mediaSourceCallBack.deliverMediaSource(
                    new DashMediaSource.Factory(dashChunkSourceFactory,
                            manifestDataSourceFactory).createMediaSource(uri)
            );
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        //android supports multiple windows. As our app can be visible but not active in split window mode, we need to initialize the player in onStart
        Log.e("onStart" , "onStart") ;
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        //    //hideSystemUi called in onResume is just an implementation detail to have a pure full screen experience:
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    public void hideSystemUi() {
        playerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    //we need to release resources with the yet to be created releasePlayer method in onPause and onStop
    //Before API Level 24 there is no guarantee of onStop being called.
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }
    //we need to release resources with the yet to be created releasePlayer method in onPause and onStop
    //) onStop is guaranteed to be called and in the paused mode our activity is eventually still visible
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }





    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory() {
        lifecycleOwner.getLifecycle().removeObserver(mInstance);
    }


    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    public void hideSystemUiFullScreen() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public  static  interface  MediaSourceCallBack {
        void  deliverMediaSource(MediaSource mediaSource);
    }
}

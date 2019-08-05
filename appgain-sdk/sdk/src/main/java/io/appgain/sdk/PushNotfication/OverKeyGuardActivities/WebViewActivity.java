package io.appgain.sdk.PushNotfication.OverKeyGuardActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.appgain.sdk.R;
import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class WebViewActivity extends AppCompatActivity {
    private static final String ORIENTATION_KEY = "ORIENTATION";
    private final  PowerMangerUtils  powerMangerUtils =  PowerMangerUtils.getInstance(this); ;
    WebView webView ;
    boolean isUrl =true;
    String webViewData;
    public static  final  String WEBVIEW_DATA = "url" ;
    private static String isUrlViewKEY = "isUrlViewKEY";
    private String oriantaion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        powerMangerUtils.prepareWindow();
        super.onCreate(savedInstanceState);
        setupExtras(savedInstanceState);

        if (isUrl && !webViewData.startsWith("http")){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(webViewData.trim()));
            if (canResolveIntent(i))
                startActivity(i);
            finish();
        }else {
            // set content view
            setContentView(R.layout.activity_lock_web_view);
            playNotificationSound();
            wakeLook();
            binViews();
            setupWebView(savedInstanceState);
            setupCloseButton();
        }
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

    private void setupWebView(Bundle savedInstanceState) {
        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("WebView", consoleMessage.message());
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
        }
        // init webview
        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);
        else{
            if (isUrl){
                webView.loadUrl(webViewData);
            }else {
                webView.loadData(webViewData,"text/html; charset=utf-8", "UTF-8");
            }
        }

    }
    boolean canResolveIntent(Intent intent){
        if (intent.resolveActivity(getPackageManager()) != null) {
            return true;
        }
        return  false;
    }

    private void binViews() {
        // bind webview
        webView = findViewById(R.id.overLockWebView) ;
        Log.e("UA" , webView.getSettings().getUserAgentString()+"");
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.114 Mobile Safari/537.36");
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
            isUrl = getIntent().getExtras().getBoolean(isUrlViewKEY);
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
    public static void start(Context context, String data, boolean isUrl, String oriantation){
        Intent intent = new Intent(context , WebViewActivity.class) ;
        intent.putExtra(WEBVIEW_DATA, data) ;
        intent.putExtra(isUrlViewKEY, isUrl) ;
        intent.putExtra(ORIENTATION_KEY, oriantation) ;
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

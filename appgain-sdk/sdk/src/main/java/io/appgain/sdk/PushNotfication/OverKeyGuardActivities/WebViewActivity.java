package io.appgain.sdk.PushNotfication.OverKeyGuardActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import io.appgain.sdk.R;

public class WebViewActivity extends AppCompatActivity {
    private final  PowerMangerUtils  powerMangerUtils =  PowerMangerUtils.getInstance(this); ;
    WebView webView ;
    String webViewUrl ;
    public static  final  String WEBVIEW_URL_KEY = "url" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        powerMangerUtils.prepareWindow();
        super.onCreate(savedInstanceState);
        // check webview url
        if (getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().getString(WEBVIEW_URL_KEY) != null)
            webViewUrl = getIntent().getExtras().getString(WEBVIEW_URL_KEY);
        else if (savedInstanceState==null)
            finish();
        // set content view
        setContentView(R.layout.activity_lock_web_view);
        powerMangerUtils.wakeLock();
        // bind webview
        webView = findViewById(R.id.overLockWebView) ;
        // init webview
        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);
        else
            webView.loadUrl(webViewUrl);

        powerMangerUtils.release();

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
        Intent intent = new Intent(context , WebViewActivity.class) ;
        intent.putExtra(WEBVIEW_URL_KEY , url) ;
        context.startActivity(intent);
    }
}

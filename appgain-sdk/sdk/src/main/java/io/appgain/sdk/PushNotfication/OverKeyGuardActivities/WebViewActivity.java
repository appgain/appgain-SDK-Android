package io.appgain.sdk.PushNotfication.OverKeyGuardActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.appgain.sdk.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class WebViewActivity extends AppCompatActivity {
    private final  PowerMangerUtils  powerMangerUtils =  PowerMangerUtils.getInstance(this); ;
    WebView webView ;
    boolean isUrl ;
    String webViewData;
    public static  final  String WEBVIEW_DATA = "url" ;
    private static String isUrlViewKEY = "isUrlViewKEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        powerMangerUtils.prepareWindow();
        super.onCreate(savedInstanceState);
        // check webview url
        if (getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().getString(WEBVIEW_DATA) != null){
            webViewData = getIntent().getExtras().getString(WEBVIEW_DATA);
            isUrl = getIntent().getExtras().getBoolean(isUrlViewKEY);

        }

        else if (savedInstanceState==null)
            finish();
        // set content view
        setContentView(R.layout.activity_lock_web_view);
        powerMangerUtils.wakeLock();
        // bind webview
        webView = findViewById(R.id.overLockWebView) ;

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
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
    public static void start(Context context , String data , boolean isUrl ){
        Intent intent = new Intent(context , WebViewActivity.class) ;
        intent.putExtra(WEBVIEW_DATA, data) ;
        intent.putExtra(isUrlViewKEY, isUrl) ;
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

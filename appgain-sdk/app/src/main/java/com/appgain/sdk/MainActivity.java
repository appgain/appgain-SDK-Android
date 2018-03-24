package com.appgain.sdk;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.appgain.sdk.Automator.Automator;
import io.appgain.sdk.Automator.AutomatorListener;
import io.appgain.sdk.Automator.AutomatorResponse;
import io.appgain.sdk.Controller.AppGain;
import io.appgain.sdk.LandingPageCreate.LandingPage;
import io.appgain.sdk.LandingPageCreate.LandingPageResponse;
import io.appgain.sdk.LandingPageCreate.LandingPageResponseListener;
import io.appgain.sdk.LandingPageCreate.RequestModels.Targets;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.User;
import io.appgain.sdk.PushNotfication.AppGainPushConversation;
import io.appgain.sdk.SmartLinkCreate.Models.LaunchPage;
import io.appgain.sdk.SmartLinkCreate.Models.MobileTarget;
import io.appgain.sdk.SmartLinkCreate.Models.SmartLinkResponse;
import io.appgain.sdk.SmartLinkCreate.SmartLinkCreator;
import io.appgain.sdk.SmartLinkCreate.SmartLinkListener;
import io.appgain.sdk.SmartLinkMatch.SmartLinkMatch;
import io.appgain.sdk.SmartLinkMatch.SmartLinkMatchListener;
import io.appgain.sdk.SmartLinkMatch.SmartLinkMatchResponse;
import io.appgain.sdk.interfaces.InitListener;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.container_view)
    View container_view ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.automator)
    void automator(){
        showLoading(true);
        if (!sdk_inti){
            showLoading(false);
            showDialog( "please inti first");
            return;
        }

        Automator.enqueue("DUMM", new AutomatorListener() {
            @Override
            public void onAutomatorCreated(@Nullable AutomatorResponse response) {
                showLoading(false);
                  showDialog(response.getMessage());
                Timber.e(response.toString());
            }

            @Override
            public void onFail(@Nullable BaseResponse failure) {
                showLoading(false);
                showDialog(failure.getMessage());
                Timber.e(failure.toString());
            }
        });

    }


    @OnClick(R.id.landing_page_create)
    void landingPageCreate(){
        showLoading(true);
        if (!sdk_inti){
            showLoading(false);
            showDialog( "please inti first");
            return;
        }

        Targets targets =  new Targets("tel:01125840548" , "Openpopup://param?title=test%20landingpage%20popup&text=this%20is%20my%20test%20data%20to%20test%20popup" , "sms:01125840548&body=test%20creating");

       LandingPage landingPage  =  new LandingPage.Builder()
                .withLang("en")
                .withWebPushSubscription(true)
                .withLogo("https://i.imgur.com/HwieXuR.jpg" , "test create landingpage")
                .withParagraph("this is a test for creating landingpage par")
                .withButton("test first button" , "test first button" , targets)
                .withImage("https://i.imgur.com/HwieXuR.jpg")
                .withLabel("testcreate")
                .withSocialMedia("test create" , "test create landingpage" ,"https://i.imgur.com/HwieXuR.jpg")
                .withImageDefault(false)
                .build();
        landingPage.enqueue(new LandingPageResponseListener() {
                    @Override
                    public void onLandingPageCreated(@Nullable LandingPageResponse response) {
                        showLoading(false);

                        showLinkDialog("landing page has worked " , response.getLink());
                    }

                    @Override
                    public void onLandingPageFail(@Nullable BaseResponse failure) {
                        showLoading(false);
                        showDialog(failure.getMessage());
                        Timber.e(failure.toString());
                    }
                });


    }

     boolean sdk_inti = false;

    @OnClick(R.id.smart_link_create_button)
    void smartLinkCreate(){
        if (!sdk_inti){
            showDialog( "please inti first");
            return;
        }
        showLoading(true);
        // inti values
         SmartLinkCreator smartLinkCreate = new SmartLinkCreator.Builder()
                 .withDescription("test Facebook Android")
                 .withName("name")
                 .withAndroid(DUMMY.ANDROID_PRIMARY , DUMMY.ANDROID_FALLBACK)
                 .withIos( DUMMY.IOS_PRIMARY,DUMMY.IOS_FALLBACK )
                 .withWeb("https://techcrunch.com/")
                 .withLaunchPage("Please Wait...")
                 .build();

         // get the link from api
         smartLinkCreate.enqueue(new SmartLinkListener() {
             @Override
             public void onSmartLinkCreated(@Nullable SmartLinkResponse response) {
                 showLoading(false);
                 showLinkDialog( "woohoo its worked" , response.getSmartLink());
                 Timber.tag("MainActivity" ).e( response.toString());
             }

             @Override
             public void onSmartLinkFail(@Nullable BaseResponse failure) {
                 showLoading(false);
                 showDialog("opps smartLinkCreate has failed");
                 Timber.e(failure.toString());
             }
         });
     }

    @OnClick(R.id.smart_link_match_button)
    void smartLinkMatch(){
        if (!sdk_inti){
            showLoading(false);
            showDialog( "please inti first");
            return;
        }
        showLoading(true);

        SmartLinkMatch.enqueue(new SmartLinkMatchListener() {
            @Override
            public void onSmartLinkCreated(@Nullable SmartLinkMatchResponse response) {
                showLoading(false);
                showLinkDialog("worked" , response.getSmartLinkPrimary());
            }

            @Override
            public void onSmartLinkFail(@Nullable BaseResponse failure) {
                showLoading(false);
                showDialog(failure.getMessage());
                Timber.e(failure.toString());
            }
        });
    }

    @OnClick(R.id.inti_anonymous)
    void inti_anonymous(){
        showLoading(true);
        AppGain.setContext(this);
        AppGain.clear();
         AppGain.initialize(
                 getApplicationContext(), DUMMY.APP_ID, DUMMY.APP_API_KEY,
                 new InitListener() {
                         @Override
                         public void onSuccess() {
                             showLoading(false);
                             sdk_inti= true ;
                             showDialog( "SDK initiated successfully");
                         }
                         @Override
                         public void onFail(BaseResponse failure) {
                             showLoading(false);
                         }
                 }
         );
     }
    @OnClick(R.id.inti_with_username)
    void inti_with_username(){
        showLoading(true);
        AppGain.setContext(this);
        AppGain.clear();
         AppGain.initialize(
                 getApplicationContext(),
                 DUMMY.APP_ID,
                 DUMMY.APP_API_KEY,
                 new User("sotra", "1234", "sotra@gmail.com"),
                 new InitListener() {
                     @Override
                     public void onSuccess() {
                         showLoading(false);
                         sdk_inti = true ;
                         showDialog( "SDK initiated successfully");
                     }

                     @Override
                     public void onFail(BaseResponse failure) {
                         showLoading(false);
                     }
                 }
         );
     }

    protected void showDialog(String string) {

            new AlertDialog
                    .Builder(this).setMessage(string)
                    .setPositiveButton("Ok" , null)
                    .create()
                    .show();
    }



    protected void showLinkDialog(String string , final String link ) {

        new AlertDialog
                .Builder(this).setMessage(string)
                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(browserIntent);
                    }
                })
                .create()
                .show();
    }


    @BindView(R.id.loading_bar)
    View loading_bar ;
    void showLoading(boolean show){
        loading_bar.setVisibility(show? View.VISIBLE : View.INVISIBLE);
    }

}

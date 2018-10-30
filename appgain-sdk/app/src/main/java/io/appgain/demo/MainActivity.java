package io.appgain.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.appgain.demo.DUMMY;
import io.appgain.demo.Dialogs.AutomatorDialog;
import io.appgain.demo.Dialogs.ConfigDialog;
import io.appgain.demo.Dialogs.SmartDeepLinkDialog;
import io.appgain.demo.Dialogs.UsernameDialog;
import io.appgain.demo.R;
import io.appgain.demo.app.AppController;
import io.appgain.sdk.Automator.Automator;
import io.appgain.sdk.Automator.AutomatorCallBack;
import io.appgain.sdk.Automator.AutomatorResponse;
import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.DeepPages.DeepPage;
import io.appgain.sdk.DeepPages.DeepPageCallBack;
import io.appgain.sdk.DeepPages.DeepPageResponse;
import io.appgain.sdk.DeepPages.RequestModels.Targets;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Model.User;
import io.appgain.sdk.SmartLinkCreate.Models.SmartDeepLinkResponse;
import io.appgain.sdk.SmartLinkCreate.SmartLinkCallback;
import io.appgain.sdk.SmartLinkCreate.SmartDeepLinkCreator;
import io.appgain.sdk.DeferredDeepLinking.ResponseModels.DeferredDeepLinkingResponse;
import io.appgain.sdk.DeferredDeepLinking.DeferredDeepLinking;
import io.appgain.sdk.DeferredDeepLinking.DeferredDeepLinkingCallBack;
import io.appgain.sdk.interfaces.AppgainSDKInitCallBack;
import io.appgain.sdk.interfaces.ParseInitCallBack;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.container_view)
    View container_view ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LoadingBar = loading_bar ;
//        Appgain.clear();
        ConfigDialog.getInstance().show(getSupportFragmentManager() , "ConfigDialog");
    }



    @OnClick(R.id.automator)
    void automator(){
        showLoading(true);
        if (!sdk_inti){
            showLoading(false);
            showDialog( "please inti first");
            return;
        }

        AutomatorDialog.getInstance(new AutomatorDialog.AutomatorDialogCallback() {
            @Override
            public void valueCallback(String trigger_point) {
                fireAutomatorTrigger(trigger_point);
            }
        }).show(getSupportFragmentManager(),"AutomatorDialog");

    }

    private void fireAutomatorTrigger(String trigger_point) {
        try {
            Automator.enqueue(trigger_point, new AutomatorCallBack() {
                @Override
                public void onAutomatorFired(@Nullable AutomatorResponse response) {
                    showLoading(false);
                    showDialog(response.getMessage());
                }

                @Override
                public void onFail(@Nullable BaseResponse failure) {
                    showLoading(false);
                    showDialog(failure.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showLoading(false);
            showDialog(e.getMessage());
        }
    }


    @OnClick(R.id.deep_page_create)
    void deepPageCreate(){
        showLoading(true);
        if (!sdk_inti){
            showLoading(false);
            showDialog( "please inti first");
            return;
        }


        try {
            DeepPage deepPage = new DeepPage.Builder()
                    .withWebPushSubscription(true)
                    .withHeader("https://i.imgur.com/HwieXuR.jpg", "test create deep page")
                    .withContent("this is a test for creating deep page par")
                    .withButton("test first button", "https://appgain.io/",  "tel:01125840548" , "sms:01125840548&body=test%20creating")
                    .withButton("test first button 2 ", "https://appgain.io/",  "tel:01125840548" , "sms:01125840548&body=test%20creating")
                    .withInfo("https://i.imgur.com/HwieXuR.jpg")
                    .withLabel("testcreate")
                    .withSocialMedia("test create", "test create deep page", "https://i.imgur.com/HwieXuR.jpg")
                    .build();

            deepPage.enqueue(new DeepPageCallBack() {
                @Override
                public void onDeepPageCreated(@Nullable DeepPageResponse response) {
                    showLoading(false);

                    showLinkDialog("deep page has worked " , response.getLink());
                }

                @Override
                public void onDeepPageFail(@Nullable BaseResponse failure) {
                    showLoading(false);
                    showDialog(failure.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showLoading(false);
            showDialog(e.getMessage());
        }




    }

    public static boolean sdk_inti = false;

    @OnClick(R.id.smart_deep_link_create_button)
    void smartDeepLinkCreate(){
        if (!sdk_inti){
            showDialog( "please inti first");
            return;
        }


        SmartDeepLinkDialog.getInstance(new SmartDeepLinkDialog.SmartDeepLinkCallback() {
            @Override
            public void valueCallback(SmartDeepLinkCreator smartDeepLinkCreator) {
                showLoading(true);
                // get the link from api
                smartDeepLinkCreator.enqueue(new SmartLinkCallback() {
                    @Override
                    public void onSmartDeepLinkCreated(@Nullable SmartDeepLinkResponse response) {
                        showLoading(false);
                        showLinkDialog( "woohoo its worked" , response.getSmartDeepLink());
                    }

                    @Override
                    public void onSmartDeepLinkFail(@Nullable BaseResponse failure) {
                        showLoading(false);
                        showDialog("oops smartDeepLinkCreator has failed \n" + failure.toString());
                    }
                });
            }
        }).show(getSupportFragmentManager() , "SmartDeepLinkDialog");

    }

    @OnClick(R.id.deferred_deep_linking_button)
    void deferredDeepLinking(){
        if (!sdk_inti){
            showLoading(false);
            showDialog( "please inti first");
            return;
        }
        showLoading(true);

        DeferredDeepLinking.enqueue(new DeferredDeepLinkingCallBack() {
            @Override
            public void onMatch(@Nullable DeferredDeepLinkingResponse response) {
                showLoading(false);
                showLinkDialog("worked" , response.getDeferredDeepLink() );
            }

            @Override
            public void onFail(@Nullable BaseResponse failure) {
                showLoading(false);
                showDialog(failure.getMessage());
            }
        });
    }

    @OnClick(R.id.inti_anonymous)
    void inti_anonymous(){
        showLoading(true);
        Appgain.setContext(this);
        Appgain.clear();
        try {
            Appgain.initialize(getApplicationContext(), DUMMY.APP_ID, DUMMY.APP_API_KEY, new AppgainSDKInitCallBack() {
                @Override
                public void onSuccess() {
                    showLoading(false);
                    sdk_inti= true ;
                    showUserID();
                }
                @Override
                public void onFail(BaseResponse failure)
                {
                    showLoading(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showLoading(false);
            showDialog(e.getMessage());
        }

    }

    @OnClick(R.id.inti_with_username)
    void inti_with_username(){
        UsernameDialog.getInstance(new UsernameDialog.UserinfoCallback() {
            @Override
            public void valueCallback(String username, String email, String password) {
                userNameInit(new User(username, password, email));
            }
        }).show(getSupportFragmentManager(),"UsernameDialog");
    }

    private void userNameInit(User user) {
        showLoading(true);
        Appgain.setContext(this);
        Appgain.clear();
        try {
            Appgain.initialize(
                    getApplicationContext(),
                    AppController.getKeys().getApp_id(),
                    AppController.getKeys().getApi_key(),
                    user,
                    new AppgainSDKInitCallBack() {
                        @Override
                        public void onSuccess() {
                            showLoading(false);
                            sdk_inti = true ;
                            showUserID();
                        }

                        @Override
                        public void onFail(BaseResponse failure) {
                            showLoading(false);
                            showDialog(failure.getMessage());
                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
            showLoading(false);
            showDialog(e.getMessage());
        }
    }

    private void showUserID() {
        Appgain.getCredentials(new ParseInitCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys, String parseUserId) {
                showDialog( "SDK initiated successfully"+"\n"+"user id = "+ parseUserId);

            }

            @Override
            public void onFailure(BaseResponse failure) {

            }
        });
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
    public  View loading_bar ;
    public  static View LoadingBar ;

    public static void showLoading(boolean show){
        if (getLoadingBar()  !=null)
            getLoadingBar().setVisibility(show? View.VISIBLE : View.INVISIBLE);
    }

    public static View getLoadingBar() {
        return LoadingBar;
    }



}

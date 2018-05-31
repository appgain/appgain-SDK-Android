package io.appgain.sdk.LandingPageCreate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels.ButtonType;
import io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels.Component;
import io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels.ComponentCreator;
import io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels.ImageType;
import io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels.LogoType;
import io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels.ParagraphType;
import io.appgain.sdk.LandingPageCreate.RequestModels.ComponentsModels.SliderType;
import io.appgain.sdk.LandingPageCreate.RequestModels.SocialMedia;
import io.appgain.sdk.LandingPageCreate.RequestModels.Targets;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Service.onRequestFailure;
import io.appgain.sdk.Utils.Utils;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 2/16/2018.
 */

/**
 * LandingPage class made to access to Appgain Landing page create  API
 */


public class LandingPage {

    @NonNull
    private LandingPage.Builder LandingPageBuilder ;

    public LandingPage(@NonNull Builder landingPageBuilder) {
        LandingPageBuilder = landingPageBuilder;
    }

    /**
     *  enqueue()
     *  check api key
     *  check app id
     *  call createLandingPage API
     */
    public void enqueue (final LandingPageCallBack callback){
        if( Appgain.getApiKey()==null){
            callback.onLandingPageFail(new BaseResponse("404" , " Api key  not found  , make sure u initiate the sdk "));
            return;
        }
        if (Appgain.getAppID() == null){
            callback.onLandingPageFail(new BaseResponse("404" , " App id not found  , make sure u initiate the sdk "));
            return;
        }

        Call<LandingPageResponse> call = Injector.Api().createLandingPage(
                Config.LANDING_PAGE_CREATE(Appgain.getAppID()) ,
                LandingPageBuilder
        );

        call.enqueue(new CallbackWithRetry<LandingPageResponse>(call, new onRequestFailure() {
            @Override
            public void onFailure(Throwable t) {
                if (callback !=null)
                    callback.onLandingPageFail(new BaseResponse(Config.NETWORK_FAILED, t.getMessage()));

            }
        }) {
            @Override
            public void onResponse(Call<LandingPageResponse> call, Response<LandingPageResponse> response) {

                if (response.isSuccessful() && response.body()!=null){
                    callback.onLandingPageCreated(response.body());
                }else {
                    if (callback!=null)
                        try {
                            callback.onLandingPageFail(Utils.getAppGainFailure(response.errorBody().string()));
                            Timber.e("onFailure"  +  response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        });
    }


    /**
     *  builder class for Appgain LandingPage  API  request body
     */
    public static final  class Builder implements Serializable{

        @NonNull
        @SerializedName("lang")
        private  String lang ;

        @NonNull
        @SerializedName("web_push_subscription")
        private boolean webPushSubscription ;

        @NonNull
        @SerializedName("components")
        private ArrayList<Component> components ;

        @NonNull
        @SerializedName("socialmedia_settings")
        private SocialMedia socialMedia ;

        @NonNull
        @SerializedName("label")
        private  String label ;

        @NonNull
        @SerializedName("image_default")
        private  boolean imageDefault ;

        @Nullable
        @SerializedName("slug")
        private  String slug ;

        private final transient ComponentCreator componentCreator = new ComponentCreator();

        @NonNull
        public LandingPage.Builder withLang(@NonNull String lang) {
            this.lang = lang;
            return  this ;
        }

        @NonNull
        public LandingPage.Builder withWebPushSubscription(@NonNull boolean webPushSubscription) {
            this.webPushSubscription = webPushSubscription;
            return  this ;
        }

        @NonNull
        public LandingPage.Builder withLogo(@NonNull String image , String header ) {
            this.componentCreator.setLogoType(new LogoType(image,header));
            this.components = this.componentCreator.getList();
            return  this ;
        }

        @NonNull
        public LandingPage.Builder withParagraph(@NonNull String paragraph) {
            this.componentCreator.setParagraphType(new ParagraphType(paragraph));
            this.components = this.componentCreator.getList();
            return  this ;
        }



        @NonNull
        public LandingPage.Builder withButton(@NonNull String text  , String altText , Targets targets) {
            this.componentCreator.setButtonType(new ButtonType(text,altText,targets));
            this.components = this.componentCreator.getList();
            return  this ;
        }


        @NonNull
        public LandingPage.Builder withImage(@NonNull String imageUrl) {
            this.componentCreator.ImageType(new ImageType(imageUrl));
            this.components = this.componentCreator.getList();
            return  this ;
        }

        @NonNull
        public LandingPage.Builder withSlider(@NonNull SliderType slider) {
            this.componentCreator.ImageType(slider);
            this.components = this.componentCreator.getList();
            return  this ;
        }

        @NonNull
        public LandingPage.Builder withSocialMedia(@NonNull String title  , @NonNull String description  , @NonNull String image) {
            this.socialMedia = new SocialMedia(title , description , image);
            return  this ;
        }

        @NonNull
        public LandingPage.Builder withLabel(@NonNull String label) {
            this.label = label;
            return  this ;
        }

        @NonNull
        public LandingPage.Builder withImageDefault(@NonNull boolean imageDefault) {
            this.imageDefault = imageDefault;
            return this ;
        }

        @NonNull
        public LandingPage.Builder withSlug(@Nullable String slug) {
            this.slug = slug;
            return this ;
        }

        @NonNull
        public  LandingPage build (){
            return new LandingPage(this) ;
        }


        @Override
        public String toString() {
            return "Builder{" +
                    "lang='" + lang + '\'' +
                    ", webPushSubscription=" + webPushSubscription +
                    ", components=" + components.toString() +
                    ", socialMedia=" + socialMedia.toString() +
                    ", label='" + label + '\'' +
                    ", imageDefault=" + imageDefault +
                    ", slug='" + slug + '\'' +
                    '}';
        }
    }
}

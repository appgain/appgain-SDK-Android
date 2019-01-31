package io.appgain.sdk.DeepPages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.appgain.sdk.Config.Errors;
import io.appgain.sdk.Config.ForbiddenInputs;
import io.appgain.sdk.Config.REGEX;
import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.DeepPages.RequestModels.ComponentsModels.ButtonType;
import io.appgain.sdk.DeepPages.RequestModels.ComponentsModels.Component;
import io.appgain.sdk.DeepPages.RequestModels.ComponentsModels.DeepPageComponentCreator;
import io.appgain.sdk.DeepPages.RequestModels.ComponentsModels.InfoImage;
import io.appgain.sdk.DeepPages.RequestModels.ComponentsModels.DeepPageHeader;
import io.appgain.sdk.DeepPages.RequestModels.ComponentsModels.DeepPageContent;
import io.appgain.sdk.DeepPages.RequestModels.ComponentsModels.InfoSLider;
import io.appgain.sdk.DeepPages.RequestModels.SocialMedia;
import io.appgain.sdk.DeepPages.RequestModels.Targets;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Service.onRequestFailure;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.Utils.Validator;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 2/16/2018.
 */

/**
 * LandingPage class made to access to Appgain Landing page create  API
 */


public class DeepPage {


    @NonNull
    private DeepPage.Builder LandingPageBuilder ;

    public DeepPage(@NonNull Builder landingPageBuilder) {
        LandingPageBuilder = landingPageBuilder;
    }

    /**
     *  enqueue()
     *  check api key
     *  check app id
     *  call createLandingPage API
     */
    public void enqueue (final DeepPageCallBack callback){
        if( Appgain.getApiKey()==null){
            callback.onDeepPageFail(new BaseResponse("404" , " Api key  not found  , make sure u initiate the sdk "));
            return;
        }
        if (Appgain.getAppID() == null){
            callback.onDeepPageFail(new BaseResponse("404" , " App id not found  , make sure u initiate the sdk "));
            return;
        }

        Call<DeepPageResponse> call = Injector.Api().createLandingPage(
                Config.LANDING_PAGE_CREATE(Appgain.getAppID()) ,
                LandingPageBuilder
        );

        call.enqueue(new CallbackWithRetry<DeepPageResponse>(call, new onRequestFailure() {
            @Override
            public void onFailure(Throwable t) {
                if (callback !=null)
                    callback.onDeepPageFail(new BaseResponse(Config.NETWORK_FAILED, t.getMessage()));

            }
        }) {
            @Override
            public void onResponse(Call<DeepPageResponse> call, Response<DeepPageResponse> response) {

                if (response.isSuccessful() && response.body()!=null){
                    callback.onDeepPageCreated(response.body());
                }else {
                    if (callback!=null)
                        try {
                            callback.onDeepPageFail(Utils.getAppGainFailure(response.errorBody().string()));
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
        private  String lang  = "en";

        @NonNull
        @SerializedName("web_push_subscription")
        private boolean webPushSubscription = true;

        @NonNull
        @SerializedName("trace_user_experience")
        private boolean heatMapTracking = false;

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
        private  boolean imageDefault  =true ;

        @Nullable
        @SerializedName("slug")
        private  String slug ;

        private final transient DeepPageComponentCreator componentCreator = new DeepPageComponentCreator();

        @NonNull
        public DeepPage.Builder withLang(@NonNull String lang) {
            this.lang = lang;
            return  this ;
        }

        @NonNull
        public DeepPage.Builder withWebPushSubscription(@NonNull boolean webPushSubscription) {
            this.webPushSubscription = webPushSubscription;
            return  this ;
        }
        @NonNull
        public DeepPage.Builder withHeatMapTracking(@NonNull boolean heatMapTracking) {
            this.heatMapTracking = heatMapTracking;
            return  this ;
        }

        @NonNull
        public DeepPage.Builder withHeader(@NonNull String logo , String topic ) {
            this.componentCreator.setHeader(new DeepPageHeader(logo,topic));
            this.components = this.componentCreator.getList();
            return  this ;
        }

        @NonNull
        public DeepPage.Builder withContent(@NonNull String content) {
            this.componentCreator.setDeepPageContent(new DeepPageContent(content));
            this.components = this.componentCreator.getList();
            return  this ;
        }



        @NonNull
        public DeepPage.Builder withButton(@NonNull String text ,  String web , String android, String ios) {
            this.componentCreator.setButtonTypes(new ButtonType(text, web ,  android,  ios));
            this.components = this.componentCreator.getList();
            return  this ;
        }


        @NonNull
        public DeepPage.Builder withInfo(@NonNull String imageUrl) {
            this.imageDefault =true;
            this.componentCreator.ImageType(new InfoImage(imageUrl));
            this.componentCreator.SliderTybe(null);
            this.components = this.componentCreator.getList();
            return  this ;
        }

        @NonNull
        public DeepPage.Builder withInfo(String... imagesUrls) {
            this.imageDefault =false;
            this.componentCreator.SliderTybe(new InfoSLider(imagesUrls));
            this.componentCreator.ImageType(null);
            this.components = this.componentCreator.getList();
            return  this ;
        }


        @NonNull
        public DeepPage.Builder withInfo(int speed, int autoPlay, int spacebetween, boolean watchSlidesProgress, InfoSLider.SliderEffects effect, InfoSLider.SliderDirection direction , String... imagesUrls) {
            this.imageDefault =false;
            this.componentCreator.SliderTybe(new InfoSLider(speed ,autoPlay , spacebetween , watchSlidesProgress , effect ,direction , imagesUrls));
            this.componentCreator.ImageType(null);
            this.components = this.componentCreator.getList();
            return  this ;
        }


        @NonNull
        public DeepPage.Builder withInfo(InfoSLider slider) {
            this.imageDefault =false;
            this.componentCreator.SliderTybe(slider);
            this.componentCreator.ImageType(null);
            this.components = this.componentCreator.getList();
            return  this ;
        }

        @NonNull
        public DeepPage.Builder withSocialMedia(@NonNull String title  , @NonNull String description  , @NonNull String image) {
            this.socialMedia = new SocialMedia(title , description , image);
            return  this ;
        }

        @NonNull
        public DeepPage.Builder withLabel(@NonNull String label) {
            this.label = label;
            return  this ;
        }


        @NonNull
        public DeepPage.Builder withSlug(@Nullable String slug) {
            this.slug = slug;
            return this ;
        }

        @NonNull
        public DeepPage build () throws Exception {
            validate() ;
            return new DeepPage(this) ;
        }


        private void validate( ) throws Exception  {
            validateSmartLinkSLug() ;
            validateName();
            this.componentCreator.validate();
            validaSocailMedia();
        }


        private void validateName() throws Exception {
            //name validation
            Validator.isNull(label , Errors.DEEP_PAGE_LABEL) ;
            Validator.isMatch(label , REGEX.NAME_LENGTH_REGX, Errors.DEEP_PAGE_LABEL_REGX_ERROR );

        }



        private   void validateSmartLinkSLug( ) throws Exception {
            if (!Validator.isNull(slug)){
                // Slug length validation
                Validator.isMatch(slug , REGEX.SLUG_LENGTH_REGX, Errors.DEEP_PAGE_SLUG_REGX_ERROR);
                // Slug forbidden names validation
                Validator.isContain(slug , ForbiddenInputs.slug , Errors.DEEP_PAGE_SLUG_CANNOT_CONTAINS);
                // Slug  has no white space validation
                Validator.isMatch(slug , REGEX.NO_WHITE_SPACE ,Errors.DEEP_PAGE_SLUG_CANNOT_CONTAINS+Errors.WHITE_SPACE);
                // Slug forbidden chars validation
                Validator.isContain(slug , ForbiddenInputs.getSlugSpecialChar(), Errors.DEEP_PAGE_SLUG_CANNOT_CONTAINS);
            }
        }





        private void validaSocailMedia() throws Exception {

            Validator.isNull(socialMedia, Errors.DEEP_PAGE_SM) ;


            Validator.isNull(socialMedia.getTitle() , Errors.DEEP_PAGE_SM_TITLE) ;
            Validator.isMatch(socialMedia.getTitle() , REGEX.SM_TITLE_LENGTH_REGX, Errors.DEEP_PAGE_SM_TITLE ) ;

            Validator.isNull(socialMedia.getDescription(), Errors.DEEP_PAGE_SM_DESCRIPTION) ;
            Validator.isMatch(socialMedia.getDescription() , REGEX.SM_DESCRIPTION_LENGTH_REGX, Errors.DEEP_PAGE_SM_DESCRIPTION) ;


            Validator.isNull(socialMedia.getImage(), Errors.DEEP_PAGE_SM_IMAGE) ;
            Validator.isMatch(socialMedia.getImage() , REGEX.URL, Errors.DEEP_PAGE_SM_IMAGE +  Errors.NOT_VALID_URL ) ;

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

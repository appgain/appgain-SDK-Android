package io.appgain.sdk.SmartLinkCreate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.appgain.sdk.Config.Errors;
import io.appgain.sdk.Config.ForbiddenInputs;
import io.appgain.sdk.Config.REGEX;
import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.SmartLinkCreate.Models.SocialMediaDescription;
import io.appgain.sdk.SmartLinkCreate.Models.MobileTarget;
import io.appgain.sdk.SmartLinkCreate.Models.SmartDeepLinkResponse;
import io.appgain.sdk.SmartLinkCreate.Models.SmartLinkTargets;
import io.appgain.sdk.Service.CallbackWithRetry;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.Service.onRequestFailure;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.Utils.Validator;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 * SmartLinkCreator class made to access to Appgain  smart link  API
 */

public final class SmartDeepLinkCreator implements Serializable{

    @NonNull
    private Builder smartLinkBuilder ;

    public SmartDeepLinkCreator(@NonNull Builder smartLinkBuilder ) {
       this.smartLinkBuilder = smartLinkBuilder ;
    }


    /**
     *  enqueue()
     *  check api key
     *  check app id
     *  call createSmartLink API
     */
    public void enqueue(final SmartLinkCallback smartLinkListener) {
        {

            if( Appgain.getApiKey()==null){
                smartLinkListener.onSmartDeepLinkFail(new BaseResponse("404" , " Api key  not found  , make sure u initiate the sdk "));
                return;
            }
            if (Appgain.getAppID() == null){
                smartLinkListener.onSmartDeepLinkFail(new BaseResponse("404" , " App id not found  , make sure u initiate the sdk "));
                return;
            }

            Call<SmartDeepLinkResponse> call = Injector.Api().createSmartLink(
                    Config.SMART_LINK_CREATE(Appgain.getAppID()) , smartLinkBuilder
            ) ;
            call.enqueue(new CallbackWithRetry<SmartDeepLinkResponse>(call, new onRequestFailure() {
                @Override
                public void onFailure(Throwable t) {
                    Timber.e("onFailure"  +  t.toString());
                    if (smartLinkListener!=null)
                        smartLinkListener.onSmartDeepLinkFail(new BaseResponse(Config.NETWORK_FAILED, t.getMessage()));
                }
            }) {
                @Override
                public void onResponse(Call<SmartDeepLinkResponse> call, Response<SmartDeepLinkResponse> response) {
                    if (response.isSuccessful()&&response.body()!=null){
                        if (smartLinkListener!=null)
                            smartLinkListener.onSmartDeepLinkCreated(response.body());
                    }else {
                        if (smartLinkListener!=null)
                            try {
                                smartLinkListener.onSmartDeepLinkFail(Utils.getAppGainFailure(response.errorBody().string()));
                                Timber.e("onFailure"  +  response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }

                }
            });
        }
    }



    /**
     *  builder class for Appgain LandingPage  API  request body
     */
    public static final  class Builder implements Serializable {

        //required
        @NonNull
        @SerializedName("name")
        private String name ;

        //required
        @NonNull
        @SerializedName("targates")
        private SmartLinkTargets smartLinkTargets ;

        // optional
        @Nullable
        @SerializedName("slug")
        private String slug ;

        //required
        @NonNull
        @SerializedName("description")
        private String socialMediaTitle ;

        //required
        @NonNull
        @SerializedName("launch_page")
        private SocialMediaDescription socialMediaDescription;

        // optional
        @Nullable
        @SerializedName("image")
        private String socialMediaImage;

        @NonNull
        public Builder withName(@NonNull String name) {
            this.name = name;
            return this ;
        }

        @NonNull
        public Builder withAndroid(@NonNull  String primary ,@NonNull  String fallback) {
            if (this.smartLinkTargets == null){
                this.smartLinkTargets = new SmartLinkTargets() ;
            }
            this.smartLinkTargets.setAndroid(new MobileTarget( primary , fallback));
            return  this ;
        }

        @NonNull
        public Builder withIos(@NonNull  String primary ,@NonNull  String fallback) {
            if (this.smartLinkTargets == null){
                this.smartLinkTargets = new SmartLinkTargets() ;
            }
            this.smartLinkTargets.setIos(new MobileTarget(primary , fallback));
            return  this ;
        }


        @NonNull
        public Builder withWeb(@NonNull String web) {
            if (this.smartLinkTargets == null){
                this.smartLinkTargets = new SmartLinkTargets() ;
            }
            this.smartLinkTargets.setWeb(web);
            return this ;
        }
        @NonNull
        public Builder withTargets(@NonNull SmartLinkTargets smartLinkTargets) {
            this.smartLinkTargets = smartLinkTargets;
            return this ;
        }
        @Nullable
        public Builder withSlug(@Nullable String slug) {
            this.slug = slug;
            return this ;
        }
        @NonNull
        public Builder withSocialMediaTitle(@NonNull String socialMediaTitle) {
            this.socialMediaTitle = socialMediaTitle;
            return this ;
        }
        @NonNull
        public Builder withSocialMediaDescription(@NonNull String socialMediaDescription) {
            this.socialMediaDescription = new SocialMediaDescription(socialMediaDescription);
            return this ;
        }
        @Nullable
        public Builder withSocialMediaImage(@Nullable String socialMediaImage) {
            this.socialMediaImage = socialMediaImage;
            return this ;
        }

        public SmartDeepLinkCreator build() throws Exception {
            validate() ;
            return new SmartDeepLinkCreator(this);
        }

        private void validate( ) throws Exception  {
            validateName();
            validateSmartLinkSLug() ;
            validMobileTarget();
            validaSocailMedia();
        }

        private void validateName() throws Exception {
            //name validation
            Validator.isNull(name , Errors.SMART_LINK_NAME) ;
            Validator.isMatch(name , REGEX.NAME_LENGTH_REGX, Errors.SMART_LINK_NAME_REGX_ERROR ) ;
        }

        private void validaSocailMedia() throws Exception {

            Validator.isNull(socialMediaTitle , Errors.SMART_LINK_SM_TITLE) ;
            Validator.isMatch(socialMediaTitle , REGEX.SM_TITLE_LENGTH_REGX, Errors.SMART_LINK_SM_TITLE_REGX_ERROR ) ;

            Validator.isNull(socialMediaDescription, Errors.SMART_LINK_SM_DESCRIPTION) ;
            Validator.isNull(socialMediaDescription.getHeader() , Errors.SMART_LINK_SM_DESCRIPTION) ;
            Validator.isMatch(socialMediaDescription.getHeader() , REGEX.SM_DESCRIPTION_LENGTH_REGX, Errors.SMART_LINK_SM_DESCRIPTION_REGX_ERROR ) ;

            if (socialMediaImage!=null)
                Validator.isMatch(socialMediaImage,REGEX.URL , Errors.SMART_LINK_SM_IMAGE +  Errors.NOT_VALID_URL );
        }

        private   void validateSmartLinkSLug( ) throws Exception {
            if (!Validator.isNull(slug)){
                // Slug length validation
                Validator.isMatch(slug , REGEX.SLUG_LENGTH_REGX,Errors.SMART_LINK_SLUG_REGX_ERROR);
                // Slug forbidden names validation
                Validator.isContain(slug , ForbiddenInputs.slug , Errors.SMART_LINK_SLUG_CANNOT_CONTAINS);
                // Slug  has no white space validation
                Validator.isMatch(slug , REGEX.NO_WHITE_SPACE ,Errors.SMART_LINK_SLUG_CANNOT_CONTAINS+Errors.WHITE_SPACE);
                // Slug forbidden chars validation
                Validator.isContain(slug , ForbiddenInputs.getSlugSpecialChar(), Errors.SMART_LINK_SLUG_CANNOT_CONTAINS);
            }
        }

        void validMobileTarget( ) throws Exception {
            if (smartLinkTargets != null){
                Validator.isMatch(smartLinkTargets.getWeb(),REGEX.URL , Errors.SMART_LINK_WEB_TARGET +  Errors.NOT_VALID_URL );
                validateMobileTarget(MobileTarget.ANDROID , smartLinkTargets.getAndroid()) ;
                validateMobileTarget(MobileTarget.IOS , smartLinkTargets.getIos()) ;
            }else {
                throw new Exception(new NullPointerException(Errors.NO_SMART_LINK_MOBILE_TARGETS)) ;
            }
        }

        private void validateMobileTarget(String target, MobileTarget mobileTarget) throws Exception {

            // nonNull validation
            Validator.isNull(mobileTarget.getPrimary(), target+Errors.MOBILE_TARGET_PRIMARY) ;
            // url validation
            Validator.isMatch(mobileTarget.getPrimary() , REGEX.URL , target+Errors.MOBILE_TARGET_PRIMARY +  Errors.NOT_VALID_URL );
            // length validation
            Validator.isMatch(mobileTarget.getPrimary() , REGEX.LINK_LENGTH_REGX , target+Errors.MOBILE_TARGET_PRIMARY + Errors.LINK_LENGHT ) ;

            // nonNull validation
            Validator.isNull(mobileTarget.getFallback(), target+ Errors.MOBILE_TARGET_FALLBACK) ;
            // url validation
            Validator.isMatch(mobileTarget.getFallback() , REGEX.URL , target+ Errors.MOBILE_TARGET_FALLBACK + Errors.NOT_VALID_URL );
            // length validation
            Validator.isMatch(mobileTarget.getFallback() , REGEX.LINK_LENGTH_REGX , target+ Errors.MOBILE_TARGET_FALLBACK + Errors.LINK_LENGHT ) ;

        }


    }


}

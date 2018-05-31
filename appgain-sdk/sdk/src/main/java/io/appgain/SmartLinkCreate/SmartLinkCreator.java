package io.appgain.SmartLinkCreate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.appgain.Controller.Appgain;
import io.appgain.Controller.Config;
import io.appgain.Model.BaseResponse;
import io.appgain.SmartLinkCreate.Models.LaunchPage;
import io.appgain.SmartLinkCreate.Models.MobileTarget;
import io.appgain.SmartLinkCreate.Models.SmartLinkResponse;
import io.appgain.SmartLinkCreate.Models.SmartLinkTargets;
import io.appgain.Service.CallbackWithRetry;
import io.appgain.Service.Injector;
import io.appgain.Service.onRequestFailure;
import io.appgain.Utils.Utils;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 * SmartLinkCreator class made to access to Appgain  smart link  API
 */

public final class SmartLinkCreator implements Serializable{

    @NonNull
    private Builder smartLinkBuilder ;

    public SmartLinkCreator(@NonNull Builder smartLinkBuilder ) {
       this.smartLinkBuilder = smartLinkBuilder ;
    }


    /**
     *  enqueue()
     *  check api key
     *  check app id
     *  call createSmartLink API
     */
    public void enqueue(final SmartLinkCallback smartLinkListener){
        {


            if( Appgain.getApiKey()==null){
                smartLinkListener.onSmartLinkFail(new BaseResponse("404" , " Api key  not found  , make sure u initiate the sdk "));
                return;
            }
            if (Appgain.getAppID() == null){
                smartLinkListener.onSmartLinkFail(new BaseResponse("404" , " App id not found  , make sure u initiate the sdk "));
                return;
            }

            Call<SmartLinkResponse> call = Injector.Api().createSmartLink(
                    Config.SMART_LINK_CREATE(Appgain.getAppID()) ,
                    smartLinkBuilder) ;
            call.enqueue(new CallbackWithRetry<SmartLinkResponse>(call, new onRequestFailure() {
                @Override
                public void onFailure(Throwable t) {
                    Timber.e("onFailure"  +  t.toString());
                    if (smartLinkListener!=null)
                        smartLinkListener.onSmartLinkFail(new BaseResponse(Config.NETWORK_FAILED, t.getMessage()));
                }
            }) {
                @Override
                public void onResponse(Call<SmartLinkResponse> call, Response<SmartLinkResponse> response) {
                    if (response.isSuccessful()&&response.body()!=null){
                        if (smartLinkListener!=null)
                            smartLinkListener.onSmartLinkCreated(response.body());
                    }else {
                        if (smartLinkListener!=null)
                            try {
                                smartLinkListener.onSmartLinkFail(Utils.getAppGainFailure(response.errorBody().string()));
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

        @SerializedName("name")
        private String name ;
        @SerializedName("targates")
        private SmartLinkTargets smartLinkTargets ;
        @SerializedName("slug")
        private String slug ;
        @SerializedName("description")
        private String description ;
        @SerializedName("launch_page")
        private LaunchPage launchPage ;
        @SerializedName("image")
        private String image ;

        @NonNull
        public Builder withName(@NonNull String name) {
            this.name = name;
            return this ;
        }

        @NonNull
        public Builder withAndroid(String primary , String fallback) {
            if (this.smartLinkTargets == null){
                this.smartLinkTargets = new SmartLinkTargets() ;
            }
            this.smartLinkTargets.setAndroid(new MobileTarget( primary , fallback));
            return  this ;
        }

        @NonNull
        public Builder withIos(String primary , String fallback) {
            if (this.smartLinkTargets == null){
                this.smartLinkTargets = new SmartLinkTargets() ;
            }
            this.smartLinkTargets.setIos(new MobileTarget(primary , fallback));
            return  this ;
        }


        @NonNull
        public Builder withWeb(String web) {
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
        public Builder withSlug(@NonNull String slug) {
            this.slug = slug;
            return this ;
        }
        @NonNull
        public Builder withDescription(@NonNull String description) {
            this.description = description;
            return this ;
        }
        @NonNull
        public Builder withLaunchPage(@NonNull String LaunchPageHeder) {
            this.launchPage = new LaunchPage(LaunchPageHeder);
            return this ;
        }
        @Nullable
        public Builder withImage(@NonNull String image) {
            this.image = image;
            return this ;
        }
        @Nullable
        public SmartLinkCreator build() {
            return new SmartLinkCreator(this);
        }


    }


}

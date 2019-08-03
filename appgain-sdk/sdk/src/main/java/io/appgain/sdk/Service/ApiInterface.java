package io.appgain.sdk.Service;


import com.google.gson.JsonObject;

import java.util.Map;

import io.appgain.sdk.Automator.AutomatorResponse;
import io.appgain.sdk.LandingPages.LandingPage;
import io.appgain.sdk.LandingPages.LandingPageResponse;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.PushNotfication.RecordStatusRequestBody;
import io.appgain.sdk.SmartLinkCreate.Models.SmartDeepLinkResponse;
import io.appgain.sdk.SmartLinkCreate.SmartDeepLinkCreator;
import io.appgain.sdk.DeferredDeepLinking.ResponseModels.DeferredDeepLinkingResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by developers@appgain.io on 8/29/2017.
 */

/**
 * interface for Retrofit call builder
 */

public interface ApiInterface {


    @GET
    Call<JsonObject> excuteUrl(
            @Url String url
    );

    @GET
    Call<SDKKeys> getCredentials(
            @Url String url
    );



    @POST
    Call<SmartDeepLinkResponse> createSmartLink(
            @Url String url ,
            @Body SmartDeepLinkCreator.Builder smartLinkBuilder
    );


    @GET
    Call<DeferredDeepLinkingResponse> smartLinkMatch(
            @Url String url  ,
            @Query("userId") String userID,
            @Query("isfirstRun") boolean firsRun
    );


    @GET
    Call<DeferredDeepLinkingResponse> smartLinkMatch(
            @Url String url  ,
            @Query("isfirstRun") boolean firsRun
    );



    @POST
    Call<LandingPageResponse> createLandingPage(
            @Url String url ,
            @Body LandingPage.Builder builder
    );


    @GET
    Call<AutomatorResponse> fireAutomator(
            @Url String url ,
            @QueryMap Map<String, String> fields
    );

    @POST
    Call<BaseResponse> recordPushStatus (
            @Url String url,
            @Body RecordStatusRequestBody pushModel
    );






}

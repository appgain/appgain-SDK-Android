package io.appgain.sdk.Service;


import io.appgain.sdk.Automator.AutomatorResponse;
import io.appgain.sdk.LandingPageCreate.LandingPage;
import io.appgain.sdk.LandingPageCreate.LandingPageResponse;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SdkKeys;
import io.appgain.sdk.PushNotfication.PushModel;
import io.appgain.sdk.SmartLinkCreate.Models.SmartLinkResponse;
import io.appgain.sdk.SmartLinkCreate.SmartLinkCreator;
import io.appgain.sdk.SmartLinkMatch.SmartLinkMatchResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Ahmed on 8/29/2017.
 */

public interface ApiInterface {


    @GET
    Call<SdkKeys> getCredentials(
            @Url String url
    );



    @POST
    Call<SmartLinkResponse> createSmartLink(
            @Url String url ,
            @Body SmartLinkCreator.Builder smartLinkBuilder
    );


    @GET
    Call<SmartLinkMatchResponse> smartLinkMatch(
            @Url String url  ,
            @Query("userId") String userID,
            @Query("isfirstRun") boolean firsRun
    );


    @GET
    Call<SmartLinkMatchResponse> smartLinkMatch(
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
            @Url String url
    );

    @POST
    Call<BaseResponse> recordPushStatus (
            @Url String url,
            @Body PushModel pushModel
    );






}

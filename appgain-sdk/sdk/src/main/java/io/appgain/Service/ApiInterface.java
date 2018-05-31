package io.appgain.Service;


import io.appgain.Automator.AutomatorResponse;
import io.appgain.LandingPageCreate.LandingPage;
import io.appgain.LandingPageCreate.LandingPageResponse;
import io.appgain.Model.BaseResponse;
import io.appgain.Model.SDKKeys;
import io.appgain.PushNotfication.RecordStatusRequestBody;
import io.appgain.SmartLinkCreate.Models.SmartLinkResponse;
import io.appgain.SmartLinkCreate.SmartLinkCreator;
import io.appgain.SmartLinkMatch.ResponseModels.SmartLinkMatchResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by developers@appgain.io on 8/29/2017.
 */

/**
 * interface for Retrofit call builder
 */

public interface ApiInterface {


    @GET
    Call<SDKKeys> getCredentials(
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
            @Body RecordStatusRequestBody pushModel
    );






}

package io.appgain.sdk.Service;


import java.io.IOException;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;


public class Injector
{

    public static Retrofit provideRetrofit (String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl( baseUrl )
                .client( provideOkHttpClient() )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
    }

    public static Retrofit provideRetrofit (String baseUrl , String userAgent)
    {
        return new Retrofit.Builder()
                .baseUrl( baseUrl )
                .client( provideOkHttpClient(userAgent) )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
    }
    public static OkHttpClient provideOkHttpClient ()
    {
        String ua = System.getProperty( "http.agent" ) ;
        Timber.e(ua);
        return provideOkHttpClientBuilder().build();
    }


    public static OkHttpClient.Builder provideOkHttpClientBuilder ()
    {
        String ua = System.getProperty( "http.agent" ) ;
        Timber.e(ua);
        return new OkHttpClient.Builder()
                .addInterceptor( provideHttpLoggingInterceptor() )
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("appApiKey", Appgain.getApiKey())
                                .addHeader("Content-Type", "application/json")
                                .addHeader("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25")
//                                .addHeader("User-Agent", System.getProperty( "http.agent" ))
                                .addHeader("Accept", "application/json").build();
                        return chain.proceed(request);
                    }
                });
    }




    private static OkHttpClient provideOkHttpClient (final String userAgent)
    {
        return new OkHttpClient.Builder()
                .addInterceptor( provideHttpLoggingInterceptor() )

                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("appApiKey", Appgain.getApiKey())
                                .addHeader("Content-Type", "application/json")
                                .addHeader("User-Agent", userAgent)
                                .addHeader("Accept", "application/json").build();
                        return chain.proceed(request);
                    }
                }).build();
    }


    private static HttpLoggingInterceptor provideHttpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (String message)
                    {
                      Timber.tag("HTTP").e(message );
                    }
                } );
        httpLoggingInterceptor.setLevel(  BODY  );
        return httpLoggingInterceptor;
    }

    public static  ApiInterface Api ()
    {
        return provideRetrofit(Config.API_URL).create(ApiInterface.class);
    }

    public static  ApiInterface Api (String userAgent)
    {
        return provideRetrofit(Config.API_URL , userAgent).create(ApiInterface.class);
    }




}

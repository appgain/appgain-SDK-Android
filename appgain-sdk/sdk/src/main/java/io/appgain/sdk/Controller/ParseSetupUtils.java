package io.appgain.sdk.Controller;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Controller.ParseLogin;
import io.appgain.sdk.Model.User;
import io.appgain.sdk.Service.Injector;
import io.appgain.sdk.interfaces.ParseLoginCallBack;
import io.appgain.sdk.interfaces.ParseSignUpCallBack;

import static com.parse.Parse.LOG_LEVEL_ERROR;

/**
 * Created by developers@appgain.io on 8/4/2019.
 */
public class ParseSetupUtils {

     public static  void parseSetup(String applicationId, String server, String clientKey, final ParseLoginCallBack parseLoginCallBack){

        // have no backend then  exit
        if (applicationId ==null || server ==null ){
            parseLoginCallBack.onFail(new ParseException(404 , Config.NO_BACKEND));
            return;
        }

        if (!server.isEmpty() && !server.endsWith("/")){
            server = server+"/" ;
        }
        if (Appgain.getContext() == null){
            parseLoginCallBack.onFail(new ParseException(404 , "Application context cannot be null "));
            return;
        }

        //

        if (clientKey!=null){
            // inti parse
            Parse.initialize(new Parse.Configuration.Builder(Appgain.getContext())
                    .clientBuilder(Injector.provideOkHttpClientBuilder())
                    .applicationId(applicationId)
                    .server(server)
                    .clientKey(clientKey)
                    .build()
            );
        }else {
            Parse.initialize(new Parse.Configuration.Builder(Appgain.getContext())
                    .clientBuilder(Injector.provideOkHttpClientBuilder())
                    .applicationId(applicationId)
                    .server(server)
                    .build()
            );
        }

        Parse.setLogLevel(LOG_LEVEL_ERROR);
//
        // if user is logged before rerun > id
        String user_id = Appgain.getPreferencesManager().getUserId() ;
        Log.e("user_id" , user_id+"") ;
        if ( user_id != null)
        {
            User userProvidedData = Appgain.getPreferencesManager().getUserProvidedData() ;
            if (userProvidedData!=null)
                ParseLogin.login(Appgain.getPreferencesManager().getUserProvidedData() , parseLoginCallBack);
            else
                ParseLogin.login(parseLoginCallBack);
        }else {
            User userProvidedData = Appgain.getPreferencesManager().getUserProvidedData() ;
            if(userProvidedData != null){
                // login with provided data
                ParseLogin.createParseUser(userProvidedData, new ParseSignUpCallBack() {
                    @Override
                    public void onSuccess(User user) {
                        ParseLogin.login(user , parseLoginCallBack);
                    }

                    @Override
                    public void onFail(ParseException e) {
                        Log.e("Appgain" , "ParseSignUp : " +e.getMessage()) ;
                        parseLoginCallBack.onFail(e);
                    }
                });
            }else {
                // login anonymous
                ParseLogin.login(parseLoginCallBack);
            }
        }

    }

}

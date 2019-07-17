package io.appgain.sdk.Controller;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

import io.appgain.sdk.Model.User;
import io.appgain.sdk.interfaces.ParseLoginCallBack;
import io.appgain.sdk.interfaces.ParseSignUpCallBack;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 7/2/2019.
 */
public class ParseLogin {


    /**
     * parse login as anonymous user in case of no username , password , email provided before
     */
    static   void login(final ParseLoginCallBack parseLoginListener){
        Timber.e("login as anonymous entered");
        ParseAnonymousUtils.logIn(new LogInCallback() {

            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null) {
                    Timber.e( "Anonymous login failed. "+e.toString());
                    if (parseLoginListener!=null)
                        parseLoginListener.onFail(e);
                } else {
                    Timber.e( "Anonymous login sucess " );
                    Appgain.getPreferencesManager().saveId(parseUser.getObjectId());
                    if (parseLoginListener!=null)
                        parseLoginListener.onSuccess(parseUser.getObjectId());

                }
            }
        });
    }


    /**
     * parse login with user provided data
     */
    static void login(final User user, final ParseLoginCallBack parseLoginListener){
        Timber.e("login with data entered");
        ParseUser.logInInBackground(user.getUsername() , user.getPassword(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    Appgain.getPreferencesManager().saveId(parseUser.getObjectId());
                    if (parseLoginListener!=null)
                    parseLoginListener.onSuccess(parseUser.getObjectId());
                } else {
                    e.printStackTrace();
                    Timber.e( "user login failed. "+e.toString());
                    if (parseLoginListener!=null)
                        parseLoginListener.onFail(e);
                }
            }
        });
    }


    /**
     * create parse  user
     */
    static void createParseUser(final User user, final ParseSignUpCallBack parseSignUpListener){
        ParseQuery.getQuery("User").whereEqualTo("userEmail" , user.getEmail()).whereEqualTo("username" , user.getUsername()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e!=null){
                    if (e.getCode() == 119 && e.getMessage().contains("This user is not allowed to access non-existent class: User")){
                        signUpNewUser(user,parseSignUpListener);
                    }
                    else if (parseSignUpListener!=null)
                        parseSignUpListener.onFail(e);
                    Timber.e(e.toString());
                }else {
                    if (objects!=null && objects.size()!=0){
                        Timber.e("createParseUser : " + e.getCode() +e.toString() );
                        parseSignUpListener.onSuccess(user);
                    }else {
                        signUpNewUser(user,parseSignUpListener);
                    }
                }
            }
        });

    }

    private static void signUpNewUser(final User user, final ParseSignUpCallBack parseSignUpListener) {
        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(user.getUsername());
        parseUser.setPassword(user.getPassword());
        parseUser.setEmail(user.getEmail());
        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Timber.e("createParseUser : "+"success");
                    if (parseSignUpListener!=null)
                        parseSignUpListener.onSuccess(user);
                } else {
                    if (e.getMessage().contains("already exists") || e.getCode()==202)
                    {
                        parseSignUpListener.onSuccess(user);
                        return;
                    }

                    Timber.e("createParseUser : " + e.getCode() +e.toString() );
                    if (parseSignUpListener!=null)
                        parseSignUpListener.onFail(e);
                }
            }
        });
    }

}

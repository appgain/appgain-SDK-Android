package io.appgain.sdk.interfaces;

import com.parse.ParseException;

import io.appgain.sdk.Model.User;

/**
 * Created by Sotra on 2/20/2018.
 */

public interface ParseSignUpListener {
        void onSuccess(User user) ;
        void onFail(ParseException failResponse );

}

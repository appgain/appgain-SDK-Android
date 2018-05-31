package io.appgain.interfaces;

import com.parse.ParseException;

import io.appgain.Model.User;
/**
 * Created by developers@appgain.io on 2/20/2018.
 */

/**
 * Parse Login response interface
 */

public interface ParseSignUpCallBack {
        void onSuccess(User user) ;
        void onFail(ParseException failResponse );

}

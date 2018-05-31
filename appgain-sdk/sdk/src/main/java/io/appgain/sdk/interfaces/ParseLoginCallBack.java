package io.appgain.sdk.interfaces;

import com.parse.ParseException;

/**
 * Created by developers@appgain.io on 2/20/2018.
 */



public interface ParseLoginCallBack {
        void onSuccess(String userId) ;
        void onFail(ParseException e);

}

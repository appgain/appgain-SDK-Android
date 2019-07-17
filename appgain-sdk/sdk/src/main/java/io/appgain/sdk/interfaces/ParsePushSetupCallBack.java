package io.appgain.sdk.interfaces;


import com.parse.ParseException;

/**
 * Created by developers@appgain.io on 5/16/2018.
 */

public interface ParsePushSetupCallBack {
    void onSuccess();
    void onFailure(ParseException e);
}

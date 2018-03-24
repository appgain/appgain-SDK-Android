package io.appgain.sdk.interfaces;

import com.parse.ParseException;

/**
 * Created by Sotra on 2/20/2018.
 */

public interface ParseLoginListener {
        void onSuccess(String userId) ;
        void onFail(ParseException e);

}

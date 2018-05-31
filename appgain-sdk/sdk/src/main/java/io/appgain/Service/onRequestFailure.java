package io.appgain.Service;

/**
 * Created by developers@appgain.io on 5/31/2017.
 */

/**
 *  interface for Retrofit call  onFailure
 *  called after getting any exaction from call or after retry tries end
 */
public interface onRequestFailure {
    void onFailure(Throwable t);
}

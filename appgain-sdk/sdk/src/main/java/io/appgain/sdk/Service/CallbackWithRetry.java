package io.appgain.sdk.Service;

import android.os.Handler;
import android.util.Log;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 5/9/2017.
 */

/**
 * CallbackWithRetry implement http client retry policy concept in SocketTimeoutException case
 */
public abstract class CallbackWithRetry<T> implements Callback<T> {

    public static int HTTP_REQUEST_RETRY_COUNT = 3;
    public static int HTTP_REQUEST_RETRY_INTERVAl_MILISECONDS = 0;
    private  final int TOTAL_RETRIES ;
    private  final int INTERVAL  ;
    private static final String TAG = CallbackWithRetry.class.getSimpleName();
    private final Call<T> call;
    private int retryCount = 0;

    onRequestFailure onRequestFailure ;


    public CallbackWithRetry(int TOTAL_RETRIES, int INTERVAL, Call<T> call , onRequestFailure onRequestFailure ) {
        this.TOTAL_RETRIES = TOTAL_RETRIES;
        this.INTERVAL = INTERVAL;
        this.call = call;
        this.onRequestFailure = onRequestFailure;
    }

    public CallbackWithRetry( Call<T> call , onRequestFailure onRequestFailure ) {
        this.TOTAL_RETRIES = HTTP_REQUEST_RETRY_COUNT;
        this.INTERVAL = HTTP_REQUEST_RETRY_INTERVAl_MILISECONDS ;
        this.call = call;
        this.onRequestFailure = onRequestFailure;
    }

    @Override
    public void onFailure(Call<T> call , Throwable t) {
        Timber.tag(TAG).e("onFailure"+ t.toString());

        if (t instanceof SocketTimeoutException){

            if (retryCount++ < TOTAL_RETRIES) {
                Timber.tag(TAG).e("Retrying... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
                retry();
            }else{
                if (onRequestFailure !=null)
                    onRequestFailure.onFailure(t);
            }
        }else {
            if (onRequestFailure !=null)
                onRequestFailure.onFailure(t);
        }
    }

    private void retry() {
        call.cancel();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                call.clone().enqueue(CallbackWithRetry.this);
            }
        },INTERVAL);
    }
}

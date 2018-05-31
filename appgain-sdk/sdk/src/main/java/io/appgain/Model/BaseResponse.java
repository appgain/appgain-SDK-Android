package io.appgain.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by developers@appgain.io on 2/12/2018.
 */

/**
 * server APIs base response
 */

public class BaseResponse implements Serializable{
    @NonNull protected String status;
    @NonNull protected String message ;

    public BaseResponse() {
    }

    public BaseResponse(@Nullable String cause, @NonNull String message) {
        this.status = cause;
        this.message = message;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

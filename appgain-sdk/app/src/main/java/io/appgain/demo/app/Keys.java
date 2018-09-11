package io.appgain.demo.app;

/**
 * Created by developers@appgain.io on 9/8/2018.
 */
public class Keys {
    String api_key ;
    String app_id ;
    boolean io  ;

    public Keys(String api_key, String app_id, boolean io ) {
        this.io  = io;
        this.app_id  = app_id;
        this.api_key  = api_key;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public boolean isIo() {
        return io;
    }

    public void setIo(boolean io) {
        this.io = io;
    }
}

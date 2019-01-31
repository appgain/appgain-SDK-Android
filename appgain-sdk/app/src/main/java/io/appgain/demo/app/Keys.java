package io.appgain.demo.app;

/**
 * Created by developers@appgain.io on 9/8/2018.
 */
public class Keys {
    String api_key ;
    String app_id ;
    boolean io  ;
    private String parseAppId;
    private String parseServerName;

    public Keys(String api_key, String app_id, String parseAppId, String parseServerName  , boolean io) {
        this.api_key = api_key;
        this.app_id = app_id;
        this.io = io;
        this.parseAppId = parseAppId;
        this.parseServerName = parseServerName;
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

    public String getParseAppId() {
        return parseAppId;
    }

    public void setParseAppId(String parseAppId) {
        this.parseAppId = parseAppId;
    }

    public String getParseServerName() {
        return parseServerName;
    }

    public void setParseServerName(String parseServerName) {
        this.parseServerName = parseServerName;
    }
}

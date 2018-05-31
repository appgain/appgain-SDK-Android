package io.appgain.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by developers@appgain.io on 2/13/2018.
 */

/**
 *  SDK init API success response
 */

public  class SDKKeys extends BaseResponse {
    @NonNull
    @SerializedName("AppID")
    private String appID;
    @NonNull
    @SerializedName("Parse-AppID")
    private String parseAppId;
    @NonNull
    @SerializedName("AppSubDomainName")
    private String appSubDomainName;
    @NonNull
    @SerializedName("Parse-serverUrl")
    private String parseServerUrl;
    @NonNull
    @SerializedName("Parse-masterKey")
    private String parseMasterKey;

    public SDKKeys(@Nullable String cause, @NonNull String message) {
        super(cause, message);
    }

    @NonNull
    public String getAppID() {
        return appID;
    }

    public void setAppID(@NonNull String appID) {
        this.appID = appID;
    }

    @NonNull
    public String getParseAppId() {
        return parseAppId;
    }

    public void setParseAppId(@NonNull String parseAppId) {
        this.parseAppId = parseAppId;
    }

    @NonNull
    public String getAppSubDomainName() {
        return appSubDomainName;
    }

    public void setAppSubDomainName(@NonNull String appSubDomainName) {
        this.appSubDomainName = appSubDomainName;
    }

    @NonNull
    public String getParseServerUrl() {
        return parseServerUrl;
    }

    public void setParseServerUrl(@NonNull String parseServerUrl) {
        this.parseServerUrl = parseServerUrl;
    }

    @NonNull
    public String getParseMasterKey() {
        return parseMasterKey;
    }

    public void setParseMasterKey(@NonNull String parseMasterKey) {
        this.parseMasterKey = parseMasterKey;
    }

    @Override
    public String toString() {
        return "SDKKeys{" +
                "appID='" + appID + '\'' +
                ", parseAppId='" + parseAppId + '\'' +
                ", appSubDomainName='" + appSubDomainName + '\'' +
                ", parseServerUrl='" + parseServerUrl + '\'' +
                ", parseMasterKey='" + parseMasterKey + '\'' +
                '}';
    }
}

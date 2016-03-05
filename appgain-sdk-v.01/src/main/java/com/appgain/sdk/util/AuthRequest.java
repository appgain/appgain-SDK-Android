package com.appgain.sdk.util;

import android.util.Base64;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mshaheen on 9/20/15.
 */
public class AuthRequest extends JsonObjectRequest {

    String username, password;

    public AuthRequest(int method, String url, JSONObject jsonRequest,
                       Response.Listener<JSONObject> listener,
                       Response.ErrorListener errorListener, String username, String password
    ) {
        super(method, url, jsonRequest, listener, errorListener);
        this.username = username;
        this.password = password;
    }

    public AuthRequest(String url, JSONObject jsonRequest,
                       Response.Listener<JSONObject> listener,
                       Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(
                "Authorization",
                String.format("Basic %s", Base64.encodeToString(
                        String.format("%s:%s", "username", "password").getBytes(), Base64.DEFAULT)));
        return params;
    }


}
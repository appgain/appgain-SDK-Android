package io.appgain.sdk.PushNotfication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.appgain.sdk.Utils.Utils;

import static io.appgain.sdk.Utils.Utils.KEY_PUSH_DATA;

/**
 * Created by developers@appgain.io on 5/28/2019.
 */
public class GetImageRequest {
    public static void imageRequest(Intent intent, final Callback callback) {
        final PushDataReceiveModel pushDataReciveModel = new Gson().fromJson(intent.getStringExtra(KEY_PUSH_DATA) , PushDataReceiveModel.class);
        if (pushDataReciveModel.getImageUrl()==null){
            callback.onFail(new Exception(new NullPointerException("image not found ")));
        }
        Utils.getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                InputStream in;
                try {
                    URL url = new URL(pushDataReciveModel.getImageUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    in = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(in);
                    callback.onSucess(myBitmap);
                } catch (MalformedURLException e) {
                    callback.onFail(e);
                } catch (IOException e) {
                    callback.onFail(e);
                }
            }
        });
    }

    public  static  interface  Callback{
            void onSucess(Bitmap bitmap);
            void onFail(Exception e);
    }
}



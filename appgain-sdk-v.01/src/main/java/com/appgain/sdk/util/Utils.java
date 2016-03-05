package com.appgain.sdk.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;


public class Utils {


    public static int getSDKVersion() {

        return Build.VERSION.SDK_INT;

    }


    public static int getScreenHeight(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();

        int height = 0;

        if (getSDKVersion() < 13) {
            height = display.getHeight();

        } else {
            display.getSize(size);
            height = size.y;
        }

        return height;

    }

    public static int getScreenWidth(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();

        int width = 0;

        if (getSDKVersion() < 13) {
            width = display.getWidth();

        } else {
            display.getSize(size);
            width = size.x;
        }

        return width;

    }



}

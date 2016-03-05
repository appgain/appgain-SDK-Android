package com.appgain.sdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by mshaheen on 9/20/15.
 */
public class SocialTools {

    public static Intent getOpenFacebookPage(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/161091464028680"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/dubaicalendar"));
        }
    }

    public static void shareImage(Bitmap imageBitmap, Activity activity) throws Exception {
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        Bitmap sBitmap1 = imageBitmap;

        String pathofBmp1 = MediaStore.Images.Media.insertImage(activity.getContentResolver(), sBitmap1, "MyCameraApp", null);
        Uri bmpUri1 = Uri.parse(pathofBmp1);
        imageUris.add(bmpUri1); // Add your image URIs here
        if (imageUris.isEmpty()) return;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sharingIntent.setType("image/*");
        sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        activity.startActivity(Intent.createChooser(sharingIntent,
                "Share image using"));

        imageUris.clear();


    }


}

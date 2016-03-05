package com.appgain.sdk.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Hashtable;


/**
 * Created by ti on 26/8/13.
 */
public class ViewUtils {


    private Typeface mDefaultFontBold, mDefaultFont, mSpecialFont, mFontAwsomeFont;
    private Context mContext;

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
    public static View sView;

    public ViewUtils(Context context) {


        mFontAwsomeFont = get(context, "fontawesome-webfont");
        mSpecialFont = get(context, "helvitica_thin");
        mDefaultFont = get(context, "helvetica_light");
        mDefaultFontBold = get(context, "helvetica");


        mContext = context;

    }

    public static Typeface get(Context c, String name) {
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                Typeface t = Typeface.createFromAsset(
                        c.getAssets(),
                        String.format("%s.ttf", name)
                );
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }


    public TextView createTextView(View view, boolean bold) {

        TextView objTextView = (TextView) view;
        if (bold)
            objTextView.setTypeface(mDefaultFontBold);
        else
            objTextView.setTypeface(mDefaultFont);
        return objTextView;
    }



    public TextView createLightTextView(View view, boolean bold) {

        TextView objTextView = (TextView) view;
        if (bold)
            objTextView.setTypeface(mDefaultFontBold);
        else
            objTextView.setTypeface(mSpecialFont);
        return objTextView;
    }


    public TextView createTextView(boolean bold) {

        TextView objTextView = new TextView(this.mContext);

        if (bold)
            objTextView.setTypeface(mDefaultFontBold);
        else
            objTextView.setTypeface(mDefaultFont);
        return objTextView;
    }


    public Button createButton(View view, boolean bold) {

        Button objButton = (Button) view;

        if (bold)
            objButton.setTypeface(mDefaultFontBold);
        else
            objButton.setTypeface(mDefaultFont);
        return objButton;
    }

    public String stringText(View view, boolean bold) {

        String stringText = toString();

        return stringText;
    }


    public TextView createIconTextView(View view) {

        TextView objTextView = (TextView) view;
        objTextView.setTypeface(mFontAwsomeFont);

        return objTextView;
    }


    public TextView createIconTextView() {

        TextView objTextView = new TextView(this.mContext);
        objTextView.setTypeface(mFontAwsomeFont);

        return objTextView;
    }

    public EditText createEditText(View view, boolean bold) {

        EditText objEditText = (EditText) view;
        if (bold)
            objEditText.setTypeface(mDefaultFontBold);
        else
            objEditText.setTypeface(mDefaultFont);
        return objEditText;

    }

    public EditText createEditText(boolean bold) {

        EditText objEditText = new EditText(mContext);
        if (bold)
            objEditText.setTypeface(mDefaultFontBold);
        else
            objEditText.setTypeface(mDefaultFont);
        return objEditText;

    }


    public void setView(View view) {
        sView = view;

    }

    public View getView() {
        return sView;

    }

}

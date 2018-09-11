package io.appgain.demo.Dialogs;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import io.appgain.demo.DUMMY;
import io.appgain.demo.R;
import io.appgain.demo.app.AppController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.appgain.sdk.SmartLinkCreate.SmartDeepLinkCreator;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 7/4/2018.
 */
public class SmartDeepLinkDialog extends BaseDialog {


    @BindView(R.id.link_name_input_layout)
    TextInputLayout link_name_input_layout ;
    @BindView(R.id.link_name_input)
    EditText link_name_input ;



    @BindView(R.id.android_fallback_link_input_layout)
    TextInputLayout android_fallback_link_input_layout ;
    @BindView(R.id.android_fallback_link_input)
    EditText android_fallback_link_input ;



    @BindView(R.id.android_primary_link_input_layout)
    TextInputLayout android_primary_link_input_layout ;
    @BindView(R.id.android_primary_link_input)
    EditText android_primary_link_input ;

    @BindView(R.id.ios_fallback_link_input_layout)
    TextInputLayout ios_fallback_link_input_layout ;
    @BindView(R.id.ios_fallback_link_input)
    EditText ios_fallback_link_input ;



    @BindView(R.id.ios_primary_link_input_layout)
    TextInputLayout ios_primary_link_input_layout ;
    @BindView(R.id.ios_primary_link_input)
    EditText ios_primary_link_input ;





    @BindView(R.id.web_link_name_input_layout)
    TextInputLayout web_link_name_input_layout ;
    @BindView(R.id.web_link_name_input)
    EditText web_link_name_input ;

    @BindView(R.id.sm_title_input_layout)
    TextInputLayout sm_title_input_layout ;
    @BindView(R.id.sm_title_input)
    EditText sm_title_input ;



    @BindView(R.id.sm_description_input_layout)
    TextInputLayout sm_description_input_layout ;
    @BindView(R.id.sm_description_input)
    EditText sm_description_input ;



    @BindView(R.id.sm_image_input_layout)
    TextInputLayout sm_image_input_layout ;
    @BindView(R.id.sm_image_input)
    EditText sm_image_input ;





    SmartDeepLinkCallback automatorDialogCallback;

    public static SmartDeepLinkDialog getInstance(SmartDeepLinkCallback automatorDialogCallback) {
        SmartDeepLinkDialog usernameDialog =  new SmartDeepLinkDialog();
        usernameDialog.automatorDialogCallback = automatorDialogCallback ;
        usernameDialog.setCancelable(false);
        return usernameDialog;
    }

    @Override
    protected View createDialogView() {
        View v = LayoutInflater.from(getContext())
                .inflate(R.layout.smart_deep_link_dialog, null);
        ButterKnife.bind(this, v);
        if (AppController.DEBUG_MODE);
        {
            link_name_input.setText("smart deep link test ");

            ios_fallback_link_input.setText(DUMMY.IOS_FALLBACK);
            ios_primary_link_input.setText(DUMMY.IOS_PRIMARY);

            android_fallback_link_input.setText(DUMMY.ANDROID_FALLBACK);
            android_primary_link_input.setText(DUMMY.ANDROID_PRIMARY);

            web_link_name_input.setText("https://www.appgain.io/");

            sm_title_input.setText("Please Wait");
            sm_description_input.setText("social description");
            sm_image_input.setText("https://i.imgur.com/HwieXuR.jpg");
        }
        return v;
    }




 boolean validate(){
        return
                !isEditTextEmpty(link_name_input , link_name_input_layout)
                && !isEditTextEmpty(ios_fallback_link_input , ios_fallback_link_input_layout)
                && !isEditTextEmpty(ios_primary_link_input , ios_primary_link_input_layout)
                && !isEditTextEmpty(android_fallback_link_input , android_fallback_link_input_layout)
                && !isEditTextEmpty(android_primary_link_input , android_primary_link_input_layout)
                && !isEditTextEmpty(web_link_name_input , web_link_name_input_layout)
                && !isEditTextEmpty(sm_title_input , sm_title_input_layout)
                && !isEditTextEmpty(sm_description_input , sm_description_input_layout)
                && !isEditTextEmpty(sm_image_input , sm_image_input_layout);
    }

static public boolean isEditTextEmpty(EditText editText , TextInputLayout textInputLayout){

        if (TextUtils.isEmpty(editText.getText().toString()))
        {
            textInputLayout.setError("Required filed");
            editText.requestFocus();
            return true;
        }
        else{
            textInputLayout.setError(null);
            return false;
        }

    }

    @OnClick(R.id.dialog_confirm)
    void confirm(){
        if (validate()){

            try {
                // inti values
                SmartDeepLinkCreator smartDeepLinkCreator = new SmartDeepLinkCreator.Builder()
                        .withName(link_name_input.getText().toString())
                        .withAndroid(android_primary_link_input.getText().toString() , android_fallback_link_input.getText().toString())
                        .withIos(ios_primary_link_input.getText().toString() , ios_fallback_link_input.getText().toString())
                        .withWeb(web_link_name_input.getText().toString())
                        .withSocialMediaTitle(sm_title_input.getText().toString())
                        .withSocialMediaDescription(sm_description_input.getText().toString())
                        .withSocialMediaImage(sm_image_input.getText().toString())
                        .build();



                automatorDialogCallback.valueCallback(
                        smartDeepLinkCreator
                );
                dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                showDialog(e.getMessage());
                Timber.e(e.toString());
            }

        }
    }

    private void showDialog(String msg) {
        new AlertDialog
                .Builder(getActivity()).setMessage(msg)
                .setPositiveButton("Ok" , null)
                .create()
                .show();
    }


    public static interface SmartDeepLinkCallback {
        void valueCallback(SmartDeepLinkCreator deepLinkCreator);
    }






}

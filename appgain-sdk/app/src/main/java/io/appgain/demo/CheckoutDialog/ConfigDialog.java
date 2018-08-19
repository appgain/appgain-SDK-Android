package io.appgain.demo.CheckoutDialog;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.appgain.demo.CheckoutDialog.BaseDialog;
import io.appgain.demo.DUMMY;
import io.appgain.demo.R;
import io.appgain.sdk.Controller.Config;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 7/4/2018.
 */
public class ConfigDialog extends BaseDialog {


    @BindView(R.id.app_id_input_layout)
    TextInputLayout app_id_input_layout ;
    @BindView(R.id.app_id_input)
    EditText app_id_input ;
    @BindView(R.id.api_key_input_layout)
    TextInputLayout api_key_input_layout ;
    @BindView(R.id.api_key_input)
    EditText api_key_input ;


    @BindView(R.id.dialog_action_layout)
    View dialog_action_layout ;



    public static ConfigDialog getInstance() {
        ConfigDialog checkoutDialog =  new ConfigDialog();
        checkoutDialog.setCancelable(false);
        return checkoutDialog;
    }

    @Override
    protected View createDialogView() {
        View v = LayoutInflater.from(getContext())
                .inflate(R.layout.user_info_dialog, null);
        ButterKnife.bind(this, v);
        return v;
    }




 boolean validate_user_info(){
        return !isEditTextEmpty(app_id_input , app_id_input_layout)
                &&!isEditTextEmpty(api_key_input , api_key_input_layout);
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



    @BindView(R.id.io)
    RadioButton radioButton ;
    @OnClick(R.id.dialog_confirm)
    void confirm(){
        if (validate_user_info()){
            DUMMY.APP_ID = app_id_input.getText().toString();
            DUMMY.APP_API_KEY = api_key_input.getText().toString();
            Config.io = radioButton.isChecked() ;

             Config.API_URL =!Config.io ? "https://api.appgain.it/" : "https://api.appgain.io/"  ;
             Config.APPS_URL =Config.API_URL+"apps/" ;
             Config.APPGAIN_IO =!Config.io ? ".appgain.it/"  : ".appgain.io/";

            Timber.e(Config.io+" io ");
            Timber.e(Config.API_URL);
            this.dismiss();
        }
    }






}

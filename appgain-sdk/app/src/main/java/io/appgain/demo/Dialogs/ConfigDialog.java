package io.appgain.demo.Dialogs;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.appgain.demo.R;
import io.appgain.demo.app.AppController;

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


    @BindView(R.id.parse_app_id_key_input_layout)
    TextInputLayout parse_app_id_key_input_layout ;
    @BindView(R.id.parse_app_id_key_input)
    EditText parse_app_id_key_input ;


    @BindView(R.id.parse_server_name_input_layout)
    TextInputLayout parse_server_name_input_layout ;
    @BindView(R.id.parse_server_name_key_input)
    EditText parse_server_name_input ;


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
                .inflate(R.layout.config_dialog, null);
        ButterKnife.bind(this, v);
        if (AppController.getKeys()!=null){
            api_key_input.setText(AppController.getKeys().getApi_key()) ;
            app_id_input.setText(AppController.getKeys().getApp_id());
            parse_app_id_key_input.setText(AppController.getKeys().getParseAppId());
            parse_server_name_input.setText(AppController.getKeys().getParseServerName());
            radioButton.setChecked(AppController.getKeys().isIo());
            stgRadioButton.setChecked(!AppController.getKeys().isIo());
        }
        return v;
    }




 boolean validate_user_info(){
        return !isEditTextEmpty(app_id_input , app_id_input_layout)
                &&!isEditTextEmpty(api_key_input , api_key_input_layout)
                &&!isEditTextEmpty(app_id_input , app_id_input_layout)
                &&!isEditTextEmpty(parse_server_name_input , parse_server_name_input_layout);
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


    @BindView(R.id.stg)
    RadioButton stgRadioButton ;
    @BindView(R.id.io)
    RadioButton radioButton ;

    @OnClick(R.id.dialog_confirm)
    void confirm(){
        if (validate_user_info()){
           AppController.saveConfiguration(
                   api_key_input.getText().toString() ,
                   app_id_input.getText().toString()  ,
                   parse_app_id_key_input.getText().toString() ,
                   parse_server_name_input.getText().toString(),
                   radioButton.isChecked()
           );
            this.dismiss();
        }
    }


}

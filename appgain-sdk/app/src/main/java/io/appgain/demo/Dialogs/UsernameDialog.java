package io.appgain.demo.Dialogs;

import android.support.design.widget.TextInputLayout;
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
import io.appgain.sdk.Model.User;

/**
 * Created by developers@appgain.io on 7/4/2018.
 */
public class UsernameDialog extends BaseDialog {


    @BindView(R.id.username_input_layout)
    TextInputLayout username_input_layout ;
    @BindView(R.id.username_input)
    EditText username_input ;

    @BindView(R.id.user_email_input_layout)
    TextInputLayout user_email_input_layout ;
    @BindView(R.id.user_email_input)
    EditText user_email_input ;


    @BindView(R.id.user_password_input_layout)
    TextInputLayout user_password_input_layout ;
    @BindView(R.id.user_password_input)
    EditText user_password_input ;


    @BindView(R.id.dialog_action_layout)
    View dialog_action_layout ;


    UserinfoCallback userInfoDialogCallback ;

    public static UsernameDialog getInstance(UserinfoCallback userinfoCallback) {
        UsernameDialog usernameDialog =  new UsernameDialog();
        usernameDialog.userInfoDialogCallback = userinfoCallback ;
        usernameDialog.setCancelable(false);
        return usernameDialog;
    }

    @Override
    protected View createDialogView() {
        View v = LayoutInflater.from(getContext())
                .inflate(R.layout.user_info_dialog, null);
        ButterKnife.bind(this, v);
        if (AppController.getUser() !=null)
        {
            username_input.setText(AppController.getUser().getUsername());
            user_email_input.setText(AppController.getUser().getEmail());
            user_password_input.setText(AppController.getUser().getPassword());
        }
        return v;
    }




 boolean validate_user_info(){
        return !isEditTextEmpty(username_input , username_input_layout)
                &&!isEditTextEmpty(user_email_input , user_email_input_layout)
                &&!isEditTextEmpty(user_password_input , user_password_input_layout);
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
        if (validate_user_info()){
            AppController.saveUser(
                    new User(username_input.getText().toString() ,
                            user_email_input.getText().toString() ,
                            user_password_input.getText().toString()
                    )
            );
            userInfoDialogCallback.valueCallback(
                    username_input.getText().toString() ,
                    user_email_input.getText().toString() ,
                    user_password_input.getText().toString()
            );
            dismiss();
        }
    }



    public static interface UserinfoCallback {
        void valueCallback(String username, String email, String password);
    }






}

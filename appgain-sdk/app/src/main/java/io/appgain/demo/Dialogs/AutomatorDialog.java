package io.appgain.demo.Dialogs;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.appgain.demo.R;
import io.appgain.demo.app.AppController;

/**
 * Created by developers@appgain.io on 7/4/2018.
 */
public class AutomatorDialog extends BaseDialog {


    @BindView(R.id.trigger_point_name_input_layout)
    TextInputLayout trigger_point_name_input_layout ;
    @BindView(R.id.trigger_point_name_input)
    EditText trigger_point_name_input ;



    AutomatorDialogCallback automatorDialogCallback;

    public static AutomatorDialog getInstance(AutomatorDialogCallback automatorDialogCallback) {
        AutomatorDialog usernameDialog =  new AutomatorDialog();
        usernameDialog.automatorDialogCallback = automatorDialogCallback ;
        usernameDialog.setCancelable(false);
        return usernameDialog;
    }

    @Override
    protected View createDialogView() {
        View v = LayoutInflater.from(getContext())
                .inflate(R.layout.automator_dialog, null);
        ButterKnife.bind(this, v);
        if (AppController.DEBUG_MODE);
        {
            trigger_point_name_input.setText("dummy");
        }
        return v;
    }




 boolean validate_user_info(){
        return !isEditTextEmpty(
                trigger_point_name_input , trigger_point_name_input_layout
        );
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
            automatorDialogCallback.valueCallback(
                    trigger_point_name_input.getText().toString()
            );
            dismiss();
        }
    }



    public static interface AutomatorDialogCallback {
        void valueCallback(String trigger_point);
    }






}

package io.appgain.sdk.SessionUtills;

import android.support.annotation.Nullable;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.RevenueUtils.PurchaseTransactions;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.interfaces.ParseAuthCallBack;

/**
 * Created by developers@appgain.io on 7/31/2019.
 */
public class AppSessions {

    public static void logAppOpen(String userId ,  AppSessionsCallback callback ){
        pushNewSessionObject(userId , callback);
    }

    private static void pushNewSessionObject(final String userId , final AppSessionsCallback callBack) {
        ParseObject purchaseTransactionsObj = new ParseObject(Config.AppSession);
        purchaseTransactionsObj.put("userId", userId);
        purchaseTransactionsObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    if (callBack!=null)
                        callBack.onFail(Utils.getAppGainFailure(e));
                }else {
                    if (callBack !=null)
                        callBack.onSuccess();
                }
            }
        });
    }

    public interface AppSessionsCallback{
        void onSuccess();
        void onFail(@Nullable BaseResponse failure);
    }
}

package io.appgain.sdk.RevenueUtils;

import android.support.annotation.Nullable;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Utils.Utils;
import io.appgain.sdk.interfaces.ParseAuthCallBack;

/**
 * Created by developers@appgain.io on 7/31/2019.
 */
public class PurchaseTransactions {

    public  static  void logPurchase(String name , int amount , String currency , PurchaseTransactionsCallback callback  ){
        logPurchase(null , name , amount , currency , callback);
    }

    private static void logPurchase(final String userId, final String name, final int amount, final String currency , final PurchaseTransactionsCallback callBack) {
        Appgain.AppgainParseAuth(new ParseAuthCallBack() {
            @Override
            public void onSuccess(SDKKeys sdkKeys, String parseUserId) {
                pushNewPurchaceObject(userId , parseUserId , name , amount , currency , callBack);
            }

            @Override
            public void onFailure(BaseResponse failure) {
                if (callBack !=null)
                    callBack.onFail(failure);
            }
        });

    }

    private static void pushNewPurchaceObject(String userId, String parseUserId, String name, int amount, String currency, final PurchaseTransactionsCallback callBack) {
        ParseObject purchaseTransactionsObj = new ParseObject(Config.PurchaseTransactions);
        purchaseTransactionsObj.put("userId", userId ==null ? parseUserId : userId);
        purchaseTransactionsObj.put("name", name);
        purchaseTransactionsObj.put("amount", amount);
        purchaseTransactionsObj.put("currency", currency);
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

    public interface PurchaseTransactionsCallback{
        void onSuccess();
        void onFail(@Nullable BaseResponse failure);
    }

}

package io.appgain.sdk.RevenueUtils;

import android.support.annotation.Nullable;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.DeferredDeepLinking.DeferredDeepLinking;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.Model.SDKKeys;
import io.appgain.sdk.Controller.Utils;
import io.appgain.sdk.interfaces.ParseAuthCallBack;

/**
 * Created by developers@appgain.io on 7/31/2019.
 */
public class PurchaseTransactions {

    public  static  void logPurchase(String name , float amount , String currency , PurchaseTransactionsCallback callback  ){
        logPurchase(null , name , amount , currency , callback);
    }

    public static void logPurchase(final String userId, final String name,  float amount, final String currency , final PurchaseTransactionsCallback callBack) {
        pushNewPurchaseObject(userId  , name , amount , currency , callBack);

    }

    private static void pushNewPurchaseObject(String userId, String name, float amount, String currency, final PurchaseTransactionsCallback callBack) {
        ParseObject purchaseTransactionsObj = new ParseObject(Config.PurchaseTransactions);
        purchaseTransactionsObj.put(Config.USER_ID_KEY, userId ==null ? Appgain.getPreferencesManager().getUserId() : userId);
        purchaseTransactionsObj.put("name", name);
        purchaseTransactionsObj.put("amount", amount);
        purchaseTransactionsObj.put("currency", currency);
        purchaseTransactionsObj.put("platform" , "android");
        if (DeferredDeepLinking.getMatchedSDL()!=null)
            purchaseTransactionsObj.put("smartlink_id" ,DeferredDeepLinking.getMatchedSDL().getSmart_link_id());
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

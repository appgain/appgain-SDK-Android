package io.appgain.sdk.Controller;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import io.appgain.sdk.Controller.Appgain;
import io.appgain.sdk.Controller.Config;
import io.appgain.sdk.Model.BaseResponse;
import io.appgain.sdk.interfaces.UpdateUserIdCallBack;
import timber.log.Timber;

/**
 * Created by developers@appgain.io on 8/4/2019.
 */
public class UpdateUtils {

    // update parse installation , users , notification channel
     static void updateParseUserId(final String userId, final UpdateUserIdCallBack callBack) {
        final String curuntUserId = getCurrentUserId();
        // case Appgain sdk  initialized
        if (userId == null || (curuntUserId !=null&& curuntUserId.equals(userId)) ){
            return;
        }
        Timber.e("updateParseUserId" +" curuntUserId "+ curuntUserId+ "  newUserId " + userId );
        updateUserIdInInstallationObject(userId , new UpdateUserIdCallBack() {
            @Override
            public void onSuccess() {
                updateUserIdInParseUserObject( curuntUserId , userId, new UpdateUserIdCallBack() {
                    @Override
                    public void onSuccess() {
                        updateNotificationsChannel(curuntUserId , userId , new UpdateUserIdCallBack() {
                            @Override
                            public void onSuccess() {
                                updatePurchaseUserId(curuntUserId , userId , new UpdateUserIdCallBack(){
                                    @Override
                                    public void onSuccess() {
                                        updateAppSessionId(curuntUserId , userId , new UpdateUserIdCallBack() {
                                            @Override
                                            public void onSuccess() {
                                                //ParseInstallation succeed
                                                Timber.e("ParseInstallation update succeed");
                                                //update preference manger userId
                                                Appgain.getPreferencesManager().saveId(userId);
                                                callBack.onSuccess();
                                            }

                                            @Override
                                            public void onFailure(BaseResponse failure) {
                                                callBack.onFailure(failure);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(BaseResponse failure) {
                                        callBack.onFailure(failure);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(BaseResponse failure) {
                                callBack.onFailure(failure);
                            }
                        }) ;
                    }

                    @Override
                    public void onFailure(BaseResponse failure) {
                        callBack.onFailure(failure);
                    }
                });
            }

            @Override
            public void onFailure(BaseResponse failure) {
                callBack.onFailure(failure);
            }
        });

    }

    private static void updateAppSessionId(String currentUserId, String userId, UpdateUserIdCallBack callBack) {
       updateTable(Config.AppSession , currentUserId , userId , callBack);
    }

    private static void updatePurchaseUserId(String currentUserId, String userId, UpdateUserIdCallBack callBack) {
       updateTable(Config.PurchaseTransactions , currentUserId , userId , callBack);
    }

    private static String getCurrentUserId() {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        final String curuntUserId = (String) installation.get(Config.USER_ID_KEY);
        return curuntUserId ;
    }

    public static void updateNotificationsChannel(String currentUserId , String userId, UpdateUserIdCallBack callBack) {
       updateTable(Config.NotificationChannels , currentUserId , userId , callBack);
    }

    private static void updateUserIdInInstallationObject(final String userId , final UpdateUserIdCallBack callBack) {
        // update installation
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(Config.USER_ID_KEY , userId);
        ArrayList arrayList = new ArrayList() ;
        arrayList.add("AE");
        installation.put("channels" , arrayList);
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("Appgain" , "ParseInstallation update : " +e.toString()) ;
                    // remove sttatic user id
                    if (callBack !=null)
                        callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                }else {
                    callBack.onSuccess();
                }
            }
        });
    }

    private static void updateUserIdInParseUserObject(String currentUserId ,  String userId, final UpdateUserIdCallBack callBack) {
      updateTable(Config.USERS_TABLE , currentUserId , userId , callBack);
    }


    public static void updateTable(final String tableName , final String currentUserId, final String userId, final UpdateUserIdCallBack callBack) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(tableName) ;
        parseQuery.whereEqualTo(Config.USER_ID_KEY , currentUserId )
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        if (e == null){
                            Timber.e("parse objects size" + objects.size() );
                            if (!objects.isEmpty())
                            {
                                for (int index =0 ; index<objects.size() ; index++) {
                                    ParseObject parseObject= objects.get(index) ;
                                    parseObject.put(Config.USER_ID_KEY , userId);
                                    final int finalIndex = index;
                                    parseObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e!=null){
                                                Log.e("Appgain" , tableName +" update " + e.toString());
                                                if (callBack !=null && (objects.size()-1)!= finalIndex)
                                                    callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                                            }else {
                                                if (callBack !=null && (objects.size()-1)!= finalIndex)
                                                    callBack.onSuccess();
                                            }
                                        }
                                    });
                                }
                            }else {
                                callBack.onSuccess();
                            }
                        }else {
                            Log.e("Appgain" , tableName+" update " + e.toString());
                            if (callBack !=null)
                                callBack.onFailure(new BaseResponse(e.getCode()+"" , e.getMessage()));
                        }
                    }
                });
    }
}

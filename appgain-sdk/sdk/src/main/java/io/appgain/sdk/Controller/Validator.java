package io.appgain.sdk.Controller;

import android.text.TextUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.appgain.sdk.Config.Errors;

/**
 * Created by developers@appgain.io on 8/18/2018.
 */
public class Validator {




    public static boolean isNull(String string){
        if (string!=null && !TextUtils.isEmpty(string)){
            return false ;
        }else {
            return true ;
        }
    }

    public static boolean isNull(String string, String error) throws Exception {
        if (isNull(string)){
            throw  new Exception(new NullPointerException(error+Errors.NULL_ERROR)) ;
        } else {
            return false ;
        }
    }


    public static boolean isNull(Object object, String error) throws Exception {
        if (object ==null){
            throw  new Exception(new NullPointerException(error+Errors.NULL_ERROR)) ;
        } else {
            return false ;
        }
    }



    public static boolean isNull(List object, String error) throws Exception {
        if (object ==null || object.isEmpty()){
            throw  new Exception(new NullPointerException(error+Errors.NULL_ERROR)) ;
        } else {
            return false ;
        }
    }
    public static boolean isNull(ArrayList object, String error) throws Exception {
        if (object ==null || object.isEmpty()){
            throw  new Exception(new NullPointerException(error+Errors.NULL_ERROR)) ;
        } else {
            return false ;
        }
    }



    public static boolean isMatch(String string, String regx) {
        return string.matches(regx);
    }


    public static boolean isMatch(String string, String regx, String error) throws Exception {
        if (!isNull(string) && !Validator.isMatch(string , regx)){
         throw new Exception(new InvalidParameterException(error));
        }
        else return true;
    }

    public static boolean isContain(String string, String[] forbiddenInputs) {

        for (String forbiddenInput :forbiddenInputs ) {
            if (forbiddenInput.contains(string)){
                return true ;
            }
        }
        return false;
    }

    public static boolean isContain(String string, String[] forbiden, String error) throws Exception {
        if (!Validator.isNull(string) && Validator.isContain(string , forbiden))
            throw new Exception(new InvalidParameterException(error+Validator.getContainedValue(string,forbiden))) ;
        else
            return false ;
    }


    public static boolean isContain(String string, Map<String,String> forbiddenInputs) {
        for (Map.Entry<String, String> entry : forbiddenInputs.entrySet()) {
            if (string.contains(entry.getValue())){
                return true ;
            }
        }
        return false;
    }

    public static boolean isContain(String string, Map<String,String> forbiddenInputs , String error) throws Exception {
        if (!Validator.isNull(string) && Validator.isContain(string , forbiddenInputs))
            throw  new Exception(new InvalidParameterException(error+Validator.getContainedValue(string,forbiddenInputs)));
        else
            return false ;
    }


    public static String getContainedValue(String string, String[] forbiddenInputs) {

        for (String forbiddenInput :forbiddenInputs ) {
            if (forbiddenInput.contains(string)){
                return forbiddenInput ;
            }
        }
        return null;
    }

    public static String getContainedValue(String string, Map<String,String> forbiddenInputs) {
        for (Map.Entry<String, String> entry : forbiddenInputs.entrySet()) {
            if (string.contains(entry.getValue())){
                return entry.getKey() ;
            }
        }
        return null;
    }



}

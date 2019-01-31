package io.appgain.sdk.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developers@appgain.io on 8/18/2018.
 */
public class ForbiddenInputs {

    public  static String[] slug = {
            "app" ,
            "prod" ,
            "stg" ,
            "www" ,
            "blog" ,
            "notify" ,
            "parse" ,
            "appbackend" ,
            "appboost" ,
            "api " ,
            "demo" ,
            "marketing" ,
            "sales" ,
            "servers" ,
            "fw" ,
            "do" ,
            "srv" ,
            "kong" ,
            "dashboard" ,
            "reports " ,
            "appboost" ,
            "m" ,
            "hack"
    };


    public  static Map<String , String> getSlugSpecialChar(){
        Map<String,String> map = new HashMap<>() ;
        map.put("slash" ,"/") ;
        map.put("back slash" ,"\\") ;
        map.put("dot" ,".") ;
        map.put("under score" ,"_") ;
        map.put("dash" ,"-") ;
        return map ;
    }
}

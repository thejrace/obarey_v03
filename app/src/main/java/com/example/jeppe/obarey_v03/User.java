package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jeppe on 08.04.2016.
 */
public class User {
    public static int ID = 0;
    public static String NAME;

    // ilk acilista hafizaya aliyoruz user id yi
    public static void storeInfo( Context ctx ){
        if( ID == 0 ){
            SharedPreferences mPrefs = ctx.getSharedPreferences("user_session", Context.MODE_PRIVATE);
            if( mPrefs.contains("user_id")){
                ID = mPrefs.getInt("user_id", 0);
                NAME = mPrefs.getString("user_name", "");
            }
        }
    }

    // logout
    public static void clearCache(){
        ID = 0;
    }


}

package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jeppe on 08.04.2016.
 */
public class Session {
    private boolean mSessionIsset = false;
    public Session( Context context ){
        SharedPreferences mPrefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        if( mPrefs.contains("user_id")){
            mSessionIsset = true;
        }
    }

    public Boolean isLoggedin(){
        return mSessionIsset;
    }

}

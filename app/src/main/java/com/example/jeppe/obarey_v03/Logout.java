package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jeppe on 09.04.2016.
 */
public class Logout {
    private Context mContext;

    public Logout( Context ctx ){
        mContext = ctx;
    }

    public void action(){
        // clear db
        SQLiteDatabase db = DB.getInstance( mContext ).getWritableDatabase();
        db.delete(DB.DBT_FRIENDS, null, null);
        db.delete(DB.DBT_USER_FRIEND_REQUESTS, null, null);
        db.delete(DB.DBT_USER_ACTIVE_GAMES, null, null);
        db.delete(DB.DBT_USER_FINISHED_GAMES, null, null);
        db.delete(DB.DBT_USER_DATA, null, null);
        db.delete(DB.DBT_USER_GAME_REQUESTS_SENT, null, null);
        db.delete(DB.DBT_USER_GAME_REQUESTS, null, null);

        // delete session
        SharedPreferences prefs = mContext.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.clear().commit();

        User.clearCache();

        System.out.println("Logout, go to activitystart");

        // activity start
        Intent intent = new Intent( mContext, ActivityStart.class );
        // activity stack i temizle
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);

    }
}

package com.example.jeppe.obarey_v03;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeppe on 08.04.2016.
 */
public class Register implements AsyncResponse{
    private String mEmail;
    private String mPassword;
    private Context mContext;
    private SQLiteDatabase db;
    private TextView mErrorNotf;

    // LoginAttempt.java
    public Register( Context context, String email, String password, TextView errorNotf ) {
        mPassword = password;
        mEmail = email;
        mContext = context;
        mErrorNotf = errorNotf;
        db = DB.getInstance( context ).getWritableDatabase();
    }

    public void action(){
        PopupLoader.show( mContext );
        Map<String, String> params= new HashMap<>();
        params.put("email", mEmail);
        params.put("password", mPassword);
        params.put("type", "do_register_action");
        ServerRequest req = new ServerRequest(ServerRequest.SERVICE_REGISTER, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }


    // session, db
    public void processFinishJSON(JSONObject output){
        System.out.println(output);
        /*
        * output:
        *   user_id(int)
        *   user_name(string)
        *   status(bool)
        *   text(string)
        *   success(bool)
        *   type(string)
        */
        try{
            Boolean success = output.getBoolean("success");
            if( success ){
                Boolean error = output.getBoolean("error");
                if( !error ){
                    String userName = output.getString("user_name");
                    Integer userId = output.getInt("user_id");
                    // db
                    ContentValues vals = new ContentValues();
                    vals.put("name", userName);
                    vals.put("user_id", userId );
                    vals.put("email", mEmail);
                    vals.put("level", 0);
                    vals.put("points", 0);
                    vals.put("leaderboard", 0);
                    vals.put("wins", 0);
                    vals.put("loses", 0);
                    vals.put("avatar", "avatar");
                    db.insert(DB.DBT_USER_DATA, null, vals);

                    // session
                    SharedPreferences prefs = mContext.getSharedPreferences("user_session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = prefs.edit();
                    prefEditor.putInt("user_id", userId );
                    prefEditor.putString("user_name", userName);
                    prefEditor.apply();

                    // start activity
                    Intent intent = new Intent(mContext, ActivityMain.class);
                    mContext.startActivity( intent );

                } else {
                    String errorText = output.getString("text");
                    mErrorNotf.setText( errorText );
                }
            }
        }catch( JSONException e ){
            System.out.println("Register finish json error " + e.toString());
        }
        PopupLoader.hide();
    }

}

package com.example.jeppe.obarey_v03;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeppe on 09.04.2016.
 */
public class Login implements AsyncResponse {

    private int mUserId;
    private String mPassword;
    private Context mContext;
    private SQLiteDatabase db;
    private TextView mErrorNotf;

    // LoginAttempt.java
    public Login( Context context, Integer userId, String password, TextView errorNotf ) {
        mPassword = password;
        mUserId = userId;
        mContext = context;
        mErrorNotf = errorNotf;
        db = DB.getInstance( context ).getWritableDatabase();
    }

    // user_id, pass gonderiyoruz
    public void action(){
        PopupLoader.show( mContext );
        Map<String, String> params= new HashMap<>();
        params.put("user_id", String.format( "%s", mUserId ));
        params.put("password", mPassword);
        params.put("type", "do_login_action");
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
        *   bundle(jsonobject)
        *       user_id(int)
        *       user_name(string) .. full data
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
                    JSONObject bundle = output.getJSONObject("user_bundle");

                    String userName = bundle.getString("name");
                    Integer userId = bundle.getInt("id");

                    // full bundle insert
                    GameDataBundle bundleAction = new GameDataBundle( mContext );
                    bundleAction.insert( bundle );

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
            System.out.println("Login finish json error " + e.toString());
        }
        PopupLoader.hide();
    }


}

package com.example.jeppe.obarey_v03;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.Map;

/**
 * Created by Jeppe on 05.03.2016.
 */
public class ServerRequest extends AsyncTask<String, String, JSONObject> {
    public AsyncResponse mDelegate = null;
    private Map<String, String> mPostParams;
    private String mAction, mReqType = "POST",
            mBaseUrl = "http://ahsaphobby.net/obarey_webservice/";
    public static String SERVICE_USER_DATA = "user_data.php",
                         SERVICE_FRIEND_REQUEST = "friend_request.php",
                         SERVICE_GAME_REQUEST = "game_request.php",
                         SERVICE_REGISTER = "register.php",
                         SERVICE_FRIEND_SEARCH = "friend_search.php",
                         SERVICE_GAME_ACTION = "game_action.php";
    public ServerRequest( String type, String request_type ){
        mReqType = request_type;
        mAction = type;
    }

    public void send(Map<String, String> params){
        mPostParams = params;
    }

    @Override
    public JSONObject doInBackground(String... params){
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;
        try {
            URL url = new URL(mBaseUrl+mAction);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(mReqType);
            Uri.Builder builder = new Uri.Builder();
            // oto gonderme, eger manuel gonderirsem diye looptan once ata
            builder.appendQueryParameter("user_id", String.format("%s", User.ID));
            for( Map.Entry<String, String> param : mPostParams.entrySet() ){
                builder.appendQueryParameter(param.getKey(), param.getValue());
            }
            String query = builder.build().getEncodedQuery();
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            int status = connection.getResponseCode();
            switch( status ){
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null){
                        sb.append(line + "/n");
                    }
                    br.close();
                    result = sb.toString();
                break;
            }
        } catch(Exception e){
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        try{
            jArray = new JSONObject(result);
        } catch(JSONException e){
            Log.e("log_tag", "JSON parse patladi " + e.toString());
        }
        return jArray;
    }

    @Override
    public void onPostExecute(JSONObject result){
        mDelegate.processFinishJSON(result);
    }

}

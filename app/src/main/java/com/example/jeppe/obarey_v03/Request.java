package com.example.jeppe.obarey_v03;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeppe on 13.04.2016.
 */
@Deprecated
public class Request implements AsyncResponse {

    public static final int GAME_REQUEST = 1, FRIEND_REQUEST = 2;
    private String mReqService;
    private int mFriendId;
    private Context mContext;
    private int mAdapterPos;
    private AdapterFriendSearch mFriendSearchAdapter;

    public Request( Context ctx, int type, int friend_id ){
        mFriendId = friend_id;
        if( type == GAME_REQUEST ){
            mReqService = ServerRequest.SERVICE_GAME_REQUEST;
        } else if( type == FRIEND_REQUEST ) {
            mReqService = ServerRequest.SERVICE_FRIEND_REQUEST;
        }
    }

    // FriendRequest constructor
    public Request( Context ctx, AdapterFriendSearch adapter, int adapter_pos, int friend_id ){
        mFriendId = friend_id;
        mReqService = ServerRequest.SERVICE_FRIEND_REQUEST;
        mAdapterPos = adapter_pos;
        mContext = ctx;
        mFriendSearchAdapter = adapter;
    }

    public void send(){
        PopupLoader.show(mContext);
        Map<String, String> params= new HashMap<>();
        params.put("type", "do_send_request");
        params.put("friend_id", String.format("%s", mFriendId ));
        ServerRequest req = new ServerRequest(mReqService, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }

    public void cancel(){
        PopupLoader.show(mContext);
        Map<String, String> params= new HashMap<>();
        params.put("type", "do_cancel_request");
        params.put("friend_id", String.format("%s", mFriendId ));
        ServerRequest req = new ServerRequest(mReqService, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }

    public void accept(){

    }

    public void ignore(){

    }

    public void processFinishJSON( JSONObject output ){
        System.out.println(output);

        try{
            String type = output.getString("type");
            // gelen istegi kabul ve reddette adapter refreshlicez o yuzden position da gelecek argument olarak
            switch( type ){
                case "do_accept_request":
                break;

                case "do_ignore_request":
                break;

                case "do_cancel_request":
                    mFriendSearchAdapter.updateRequestSentFlag( mAdapterPos );
                break;

                case "do_send_request":
                    mFriendSearchAdapter.updateRequestSentFlag( mAdapterPos );
                break;
            }

        } catch( JSONException e ){
            System.out.println("Request json " + e.toString() );
        }
        PopupLoader.hide();
    }
}

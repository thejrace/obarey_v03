package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeppe on 14.04.2016.
 */
public class FriendAction implements AsyncResponse {

    private Context mContext;
    private int mId;
    private GameDataBundle mBundle;

    private AdapterStartGame mStartGameAdapter;
    private AdapterGame mGameAdapter;
    private AdapterRequest mRequestAdapter;
    private AdapterFriendSearch mFriendSearchAdapter;
    private int mAdapterPos;

    // arkadas silme, istek kabul etme vs..
    public FriendAction( Context ctx, int friend_id ){
        mContext = ctx;
        mId = friend_id;
        mBundle = new GameDataBundle( ctx );
    }

    private void serverRequest( String type ){
        PopupLoader.show(mContext);
        Map<String, String> params= new HashMap<>();
        params.put("type", type);
        params.put("friend_id", String.format("%s", mId ));
        ServerRequest req = new ServerRequest(ServerRequest.SERVICE_FRIEND_REQUEST, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }

    public void setAdapterData( AdapterStartGame adapter, int pos ){
        mStartGameAdapter = adapter;
        mAdapterPos = pos;
    }

    public void setAdapterData( AdapterGame adapter, int pos ){
        mGameAdapter = adapter;
        mAdapterPos = pos;
    }

    public void setAdapterData( AdapterRequest adapter, int pos ){
        mRequestAdapter = adapter;
        mAdapterPos = pos;
    }

    public void setAdapterData( AdapterFriendSearch adapter, int pos ){
        mFriendSearchAdapter = adapter;
        mAdapterPos = pos;
    }

    public void delete(){ serverRequest("do_delete_friend");}
    public void sendRequest(){ serverRequest("do_send_request");}
    public void cancelRequest(){ serverRequest("do_cancel_request");}
    public void acceptRequest(){ serverRequest("do_accept_request");}
    public void ignoreRequest(){ serverRequest("do_ignore_request" );}

    public void processFinishJSON( JSONObject output ){
        System.out.println(output);

        try {
            String type = output.getString("type");
            JSONObject friendData;
            switch( type ){
                case "do_delete_friend":
                    mBundle.deleteFriend(mId);
                    mBundle.deleteGame(mId);
                    // start game adapter data
                    //List<FriendData> startGameAdapterData =;
                    mStartGameAdapter.setNewData( mBundle.getFriendsToStartGame());
                    mStartGameAdapter.notifyDataSetChanged();
                break;

                case "do_accept_request":
                    friendData = output.getJSONObject("friend_data");
                    // db ye ekle arkadasi
                    mBundle.addFriend( new FriendData( mId, friendData.getString("friend_name"), friendData.getString("friend_avatar") ) );
                    // requesti sil
                    mBundle.deleteFriendRequest( mId );
                    // adapter refresh
                    mRequestAdapter.setNewData(mBundle.getFriendRequests() );
                    mRequestAdapter.notifyDataSetChanged();
                break;

                case "do_ignore_request":
                    mBundle.deleteFriendRequest( mId );
                    mRequestAdapter.setNewData(mBundle.getFriendRequests());
                    mRequestAdapter.notifyDataSetChanged();
                break;

                // gonderileni iptal etme
                case "do_cancel_request":
                    mFriendSearchAdapter.updateRequestSentFlag( mAdapterPos );
                    break;

                case "do_send_request":
                    mFriendSearchAdapter.updateRequestSentFlag( mAdapterPos );
                break;

            }

        } catch( JSONException e ){
            System.out.println( "FriendAction json " + e.toString());
        }
        PopupLoader.hide();
    }

}

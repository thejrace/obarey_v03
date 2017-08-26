package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeppe on 12.04.2016.
 */
public class FriendSearch implements AsyncResponse {

    private Context mContext;
    private AdapterFriendSearch mAdapter;
    private TextView mNotf;

    public FriendSearch( Context ctx  ){
        mContext = ctx;
    }

    public void make( String query, AdapterFriendSearch adapter, TextView notf ){
        PopupLoader.show( mContext );
        mAdapter = adapter;
        mNotf = notf;
        Map<String, String> params= new HashMap<>();
        params.put("query", query);
        params.put("type", "do_search");
        ServerRequest req = new ServerRequest(ServerRequest.SERVICE_FRIEND_SEARCH, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }

    @Override
    public void processFinishJSON( JSONObject output ){
        System.out.println(output);
        try {
            JSONArray results = output.getJSONArray("results");
            int length = results.length();
            List<RequestData> data = new ArrayList<>();
            if( length > 0 ){
                mNotf.setText( String.format( "%s sonuç bulundu.", length ) );
                for( int i = 0; i < length; i++ ){
                    JSONObject curData = results.getJSONObject(i);
                    data.add( new RequestData( new FriendData ( curData.getInt("friend_id"), curData.getString("friend_name"), curData.getString("friend_avatar") ), curData.getBoolean("request_sent") ) );
                }
            } else {
                mAdapter.clearList();
                mNotf.setText( R.string.no_search_result );
            }
            // refresh listview bossa
            mAdapter.setNewData( data );
            mAdapter.notifyDataSetChanged();
        } catch( JSONException e ){
            System.out.println("FriendSearch json " + e.toString());
        }
        PopupLoader.hide();
    }


}

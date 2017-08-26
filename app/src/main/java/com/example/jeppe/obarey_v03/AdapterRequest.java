package com.example.jeppe.obarey_v03;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 14.04.2016.
 */
public class AdapterRequest extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<RequestData> mRequests;
    private int mTotal;
    private Activity mActivity;
    private int mReqType;
    public static int FRIEND_REQUEST = 1, GAME_REQUEST = 2;
    public AdapterRequest( int req_type, Activity context, List<RequestData> requests ){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRequests = requests;
        mTotal = requests.size();
        mActivity = context;
        mReqType = req_type;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ){
        if( convertView == null ){
            convertView = mInflater.inflate(R.layout.lv_adapter_request, parent ,false );
        }

        RequestData currentData = mRequests.get(position);
        final FriendData currentFriend = currentData.getFriendData();

        TextView friendName = (TextView)convertView.findViewById(R.id.friends_name);
        friendName.setText(currentFriend.getName());

        ImageButton acceptRequest = (ImageButton)convertView.findViewById(R.id.action_accept);
        ImageButton ignoreRequest = (ImageButton)convertView.findViewById(R.id.action_ignore);

        final FriendAction friendAction = new FriendAction( mActivity, currentFriend.getId() );
        friendAction.setAdapterData(this, position);

        final GameAction gameAction = new GameAction( mActivity, currentFriend.getId() );
        gameAction.setAdapterData(this, position);

        // game requestlerde var type mantigi yapmak lazim
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mReqType == FRIEND_REQUEST ){
                    friendAction.acceptRequest();
                } else if( mReqType == GAME_REQUEST ){
                    gameAction.acceptRequest();
                }
            }
        });

        ignoreRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mReqType == FRIEND_REQUEST ){
                    friendAction.ignoreRequest();
                } else if( mReqType == GAME_REQUEST ){
                    gameAction.ignoreRequest();
                }
            }
        });


        return convertView;
    }


    public void setNewData( List<RequestData> newData ){
        mTotal = newData.size();
        mRequests = newData;
    }

    @Override
    public int getCount(){
        return mTotal;
    }

    @Override
    public RequestData getItem( int position ){
        return mRequests.get(position);
    }

    @Override
    public long getItemId( int position ){
        return position;
    }
}

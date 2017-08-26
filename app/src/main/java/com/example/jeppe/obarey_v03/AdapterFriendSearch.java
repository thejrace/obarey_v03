package com.example.jeppe.obarey_v03;

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
 * Created by Jeppe on 12.04.2016.
 */
public class AdapterFriendSearch extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<RequestData> mFriends;
    private int mTotal;
    private Context mContext;
    public AdapterFriendSearch( Context context, List<RequestData> friends ){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFriends = friends;
        mTotal = friends.size();
        mContext = context;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ){
        if( convertView == null ){
            convertView = mInflater.inflate(R.layout.lv_adapter_friend_search, parent ,false );
        }

        RequestData currentData = mFriends.get(position);
        final FriendData currentFriend = currentData.getFriendData();
        final boolean isRequestSent = currentData.getRequestSent();

        TextView friendName = (TextView)convertView.findViewById(R.id.friends_name);
        friendName.setText(currentFriend.getName());

        ImageButton sendRequest = (ImageButton)convertView.findViewById(R.id.action_add);
        ImageButton cancelRequest = (ImageButton)convertView.findViewById(R.id.action_cancel);

        // friend request
        //final Request request = new Request( mContext, this, position, currentFriend.getId() );
        final FriendAction friendAction = new FriendAction( mContext, currentFriend.getId() );
        friendAction.setAdapterData(this, position);

        // request gonderilmisse iptal butonu gelecek
        if( isRequestSent ){
            sendRequest.setVisibility(View.GONE);
            cancelRequest.setVisibility(View.VISIBLE);
            cancelRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println( "Cancel request friend : " + currentFriend.getName() );
                    friendAction.cancelRequest();
                }
            });
        } else {
            cancelRequest.setVisibility(View.GONE);
            sendRequest.setVisibility(View.VISIBLE);
            sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println( "Send request friend : " + currentFriend.getName() );
                    friendAction.sendRequest();
                }
            });
        }

        return convertView;
    }

    public int getClickedFriend( int position ){
        return mFriends.get(position).getFriendData().getId();
    }

    // send, accept sonrasi buton guncelleme
    public void updateRequestSentFlag( int position ){
        //System.out.println( "SENT FLAG : " + mFriends.get(position).getRequestSent() );
        mFriends.get(position).setRequestSent( !mFriends.get(position).getRequestSent() );
        notifyDataSetChanged();
    }

    public void clearList(){
        mFriends = new ArrayList<>();
        mTotal = 0;
        notifyDataSetChanged();
    }

    public void setNewData( List<RequestData> newData ){
        mTotal = newData.size();
        mFriends = newData;
    }

    @Override
    public int getCount(){
        return mTotal;
    }

    @Override
    public RequestData getItem( int position ){
        return mFriends.get(position);
    }

    @Override
    public long getItemId( int position ){
        return position;
    }
}

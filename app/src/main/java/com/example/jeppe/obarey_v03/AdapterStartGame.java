package com.example.jeppe.obarey_v03;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jeppe on 12.04.2016.
 */
public class AdapterStartGame extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<FriendData> mFriends;
    private Activity mActivity;
    private int mTotal;
    public AdapterStartGame( Activity context, List<FriendData> friends ){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFriends = friends;
        mTotal = friends.size();
        mActivity = context;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ){
        if( convertView == null ){
            convertView = mInflater.inflate(R.layout.lv_adapter_start_game, parent ,false );
        }

        final FriendData currentFriend = mFriends.get(position);
        final boolean isRequestSent = currentFriend.getGameRequestSent();

        TextView friendName = (TextView)convertView.findViewById(R.id.friends_name);
        friendName.setText( currentFriend.getName() );

        ImageButton sendRequest = (ImageButton)convertView.findViewById(R.id.action_send_game_request);
        ImageButton cancelRequest = (ImageButton)convertView.findViewById(R.id.action_cancel_game_request);
        ImageButton checkHistory = (ImageButton)convertView.findViewById(R.id.action_history);
        ImageButton chat = (ImageButton)convertView.findViewById(R.id.action_chat);
        ImageButton delete = (ImageButton)convertView.findViewById(R.id.action_delete);

        final FriendAction friendAction = new FriendAction( mActivity, currentFriend.getId() );
        friendAction.setAdapterData( this, position );

        final GameAction gameAction = new GameAction( mActivity, currentFriend.getId() );
        gameAction.setAdapterData( this, position );

        // request gonderilmisse iptal butonu gelecek
        if( isRequestSent ){
            sendRequest.setVisibility(View.GONE);
            cancelRequest.setVisibility(View.VISIBLE);
            cancelRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println( "Cancel request friend : " + currentFriend.getName() );
                    gameAction.cancelRequest();
                }
            });
        } else {
            cancelRequest.setVisibility(View.GONE);
            sendRequest.setVisibility(View.VISIBLE);
            sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println( "Send request friend : " + currentFriend.getName() );
                    gameAction.sendRequest();
                }
            });
        }

        checkHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( "Check history friend : " + currentFriend.getName() );
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( "Chat with friend : " + currentFriend.getName() );
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( "Delete friend friend : " + currentFriend.getName() );
                friendAction.delete();
            }
        });

        return convertView;
    }

    // send, accept sonrasi buton guncelleme
    public void updateRequestSentFlag( int position ){
        //System.out.println( "SENT FLAG : " + mFriends.get(position).getRequestSent() );
        mFriends.get(position).setGameRequestSent(!mFriends.get(position).getGameRequestSent());
        notifyDataSetChanged();
    }

    public void removeItem( int position ){
        mFriends.remove( position );
        mTotal = mFriends.size();
    }

    public void setNewData( List<FriendData> newData ){
        mTotal = newData.size();
        mFriends = newData;
    }

    @Override
    public int getCount(){
        return mTotal;
    }

    @Override
    public FriendData getItem( int position ){
        return mFriends.get(position);
    }

    @Override
    public long getItemId( int position ){
        return position;
    }

}

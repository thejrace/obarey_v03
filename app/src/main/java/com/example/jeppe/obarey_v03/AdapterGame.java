package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jeppe on 12.04.2016.
 */
public class AdapterGame extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<GameAdapterData> mGames;
    private int mTotal;
    public AdapterGame( Context context, List<GameAdapterData> games ){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGames = games;
        mTotal = games.size();
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ){
        if( convertView == null ){
            convertView = mInflater.inflate(R.layout.lv_adapter_active_game, parent ,false );
        }

        if( mTotal > 0 ) {
            final GameData game = mGames.get(position).getGameData();
            final FriendData friend = mGames.get(position).getFriendData();

            LinearLayout itemBody = (LinearLayout) convertView.findViewById(R.id.game_adapter_body);
            // sira kullanici da degilse hafif transparan yap
            if (game.getPlayTurn() != User.ID){
                itemBody.setAlpha((float) 0.8);
            } else {
                itemBody.setAlpha((float) 1);
            }

            TextView friendName = (TextView) convertView.findViewById(R.id.friends_name);
            TextView roundCount = (TextView) convertView.findViewById(R.id.round_count);
            TextView friendsPoints = (TextView) convertView.findViewById(R.id.friends_points);
            TextView usersPoints = (TextView) convertView.findViewById(R.id.users_points);

            ImageButton chatButton = (ImageButton) convertView.findViewById(R.id.action_chat);
            ImageButton withDrawButon = (ImageButton) convertView.findViewById(R.id.action_withdraw);
            // onitemclick listener icin @ http://stackoverflow.com/questions/1518338/setonitemclicklistener-not-working-on-custom-listview-android
            chatButton.setFocusable(false);
            withDrawButon.setFocusable(false);

            friendName.setText(friend.getName());
            roundCount.setText(String.format("%s. Tur", game.getRound()));
            friendsPoints.setText(String.format("%s", game.getFriendsPoints()));
            usersPoints.setText(String.format("%s", game.getUsersPoints()));

            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Chat friend_id : " + game.getFriendId());
                }
            });

            withDrawButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Withdraw friend_id : " + game.getFriendId());
                }
            });
        }
        return convertView;
    }

    public GameData getClickedGame( int position ){
        return mGames.get(position).getGameData();
    }
    public boolean isPlayable( int position ){
        return mGames.get(position).getGameData().getPlayTurn() == User.ID;
    }

    public void setNewData( List<GameAdapterData> newData ){
        mTotal = newData.size();
        mGames = newData;
    }

    public void removeItem( int position ){
        mGames.remove( position );
        mTotal = mGames.size();
    }

    @Override
    public int getCount(){
        return mTotal;
    }

    @Override
    public GameAdapterData getItem( int position ){
        return mGames.get(position);
    }

    @Override
    public long getItemId( int position ){
        return position;
    }
}

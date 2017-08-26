package com.example.jeppe.obarey_v03;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by Jeppe on 10.04.2016.
 */
public class FragmentTabStartGame extends Fragment  {

    public FragmentTabStartGame() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_start_game, container, false);

        final Activity mActivity = getActivity();
        final GameDataBundle gameDataBundle = new GameDataBundle( mActivity );

        // listview
        ListView startGameListView = (ListView)rootView.findViewById(R.id.start_game_listview);
        final AdapterStartGame adapter = new AdapterStartGame( mActivity, gameDataBundle.getFriendsToStartGame() );
        startGameListView.setAdapter(adapter);

        // add friend button
        ImageButton goAddFriendActivity = (ImageButton)rootView.findViewById(R.id.go_add_friend_activity);
        goAddFriendActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to AddFriend
                Intent intent = new Intent( mActivity, ActivityAddFriend.class );
                startActivity( intent );
            }
        });

        return rootView;
    }
}
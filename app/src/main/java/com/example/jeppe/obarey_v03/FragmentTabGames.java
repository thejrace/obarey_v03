package com.example.jeppe.obarey_v03;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 10.04.2016.
 */
public class FragmentTabGames extends Fragment{

    private int mAutoRefreshInterval = 5000;
    private Handler mHandler;
    private GameDataBundle mGameDataBundle;
    private AdapterGame mAdapter;

    public FragmentTabGames(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // ActivityMain den listview guncelleme
    // startGame tabdan arkadasi sildigimde aktif oyun varsa listeyi guncelleme ( boyle bisi yok amk ama dursun )
    public void refreshFromActivity(){
        // timer koymak lazim her seferinde olmaz
        //PopupLoader.show( mActivity );
        //mGameDataBundle.requestGamesWithAdapterRefresh(mAdapter);
    }

    Runnable mAutoRefresher = new Runnable(){
        @Override
        public void run(){
            try {
                //System.out.println("REFRESH GAMES AUTO");
                mGameDataBundle.requestGamesWithAdapterRefresh(mAdapter);
            } finally{
                mHandler.postDelayed(mAutoRefresher, mAutoRefreshInterval);
            }
        }
    };

    private void initAutoRefresh(){
        mHandler = new Handler();
        autoRefreshStart();
    }

    private void autoRefreshStart(){
        mAutoRefresher.run();
    }
    private void stopAutoRefresh(){
        mHandler.removeCallbacks(mAutoRefresher);
    }

    @Override
    public void onPause(){
        super.onPause();
        stopAutoRefresh();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_games, container, false);
        // GAMES count 0 sa hata veriyor

        final Activity mActivity = getActivity();
        mGameDataBundle = new GameDataBundle( mActivity );
        //mActivity = (ActivityMain)getActivity();
        // mGameDataBundle = mActivity.getGameDataBundle();

        List<GameAdapterData> mGameAdapterData = new ArrayList<>();
        List<GameData> games = mGameDataBundle.getGames();
        for( int i = 0; i < games.size(); i++ ){
            mGameAdapterData.add( new GameAdapterData( mGameDataBundle, games.get(i) ) );
        }

        // listview
        ListView gameListView = (ListView)rootView.findViewById(R.id.games_listview);
        mAdapter = new AdapterGame(mActivity, mGameAdapterData );
        gameListView.setAdapter(mAdapter);
        // item onclick
        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if( mAdapter.isPlayable(position) ){
                    PlayAttempt playAttempt = new PlayAttempt( mActivity, mAdapter.getClickedGame(position));
                    playAttempt.requestNewQuestion( false );
                    //System.out.println(" Game friend_id : " + mAdapter.getClickedGame(position));

                }
            }
        });
        // refresh
        ImageButton refresh = (ImageButton)rootView.findViewById(R.id.refresh_games);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupLoader.show(mActivity);
                mGameDataBundle.requestGamesWithAdapterRefresh(mAdapter);
            }
        });

        // todo auto refresh ( bunu genel ayar yapicaz her seferinde kullanici tercihine gore acik veya kapali olacak
        final ImageButton autoRefreshOn = (ImageButton)rootView.findViewById(R.id.auto_refresh_on);
        final ImageButton autoRefreshOff = (ImageButton)rootView.findViewById(R.id.auto_refresh_off);
        autoRefreshOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRefreshOn.setVisibility(View.GONE);
                autoRefreshOff.setVisibility(View.VISIBLE);
                stopAutoRefresh();
            }
        });
        autoRefreshOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRefreshOff.setVisibility(View.GONE);
                autoRefreshOn.setVisibility(View.VISIBLE);
                initAutoRefresh();
            }
        });

        // auto refresh
        initAutoRefresh();
        return rootView;
    }
}

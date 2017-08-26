package com.example.jeppe.obarey_v03;

import java.util.List;

/**
 * Created by Jeppe on 11.04.2016.
 * ActivityMain
 * Direk oyun adapter e gidecek data ( List<GameDataAdapter>
 */
public class GameAdapterData {

    private GameData mGameData;
    private FriendData mFriendData;

    // activityMain den bundle aliyorum friend datayi tamamiyla alabilmek icin
    public GameAdapterData( GameDataBundle game_data_bundle, GameData game_data ){
        mGameData = game_data;
        mFriendData = game_data_bundle.getFriend(mGameData.getFriendId());
    }

    public GameData getGameData(){
        return mGameData;
    }
    public FriendData getFriendData(){
        return mFriendData;
    }

}

package com.example.jeppe.obarey_v03;

import java.io.Serializable;

/**
 * Created by Jeppe on 11.04.2016.
 */
public class GameData implements Serializable {
    // todo GAMEID IMPLEMENTATION -- ID ZATEN REQUESTTE GELIYOR DB YI GUNCELLE
    private Integer mFriendId, mRound, mPlayTurn, mFriendsPoints, mUsersPoints, mGameId;
    public GameData( Integer game_id, Integer friend_id, Integer round, Integer play_turn, Integer users_points, Integer friends_points ){
        mFriendId = friend_id;
        mRound = round;
        mPlayTurn = play_turn;
        mFriendsPoints = friends_points;
        mUsersPoints = users_points;
        mGameId = game_id;
    }

    public int getGameId(){ return mGameId; }
    public int getFriendId(){
        return mFriendId;
    }
    public int getRound(){
        return mRound;
    }
    public int getPlayTurn(){
        return mPlayTurn;
    }
    public int getFriendsPoints(){
        return mFriendsPoints;
    }
    public int getUsersPoints(){
        return mUsersPoints;
    }

}

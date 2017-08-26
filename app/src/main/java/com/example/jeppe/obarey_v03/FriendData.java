package com.example.jeppe.obarey_v03;

/**
 * Created by Jeppe on 11.04.2016.
 */
public class FriendData {

    private int mId;
    private String mName, mAvatar;
    private boolean mGameRequestSent;
    public FriendData( int id, String name, String avatar ){
        mId = id;
        mName = name;
        mAvatar = avatar;
    }

    // StartGameAdapter icin constructor
    public FriendData ( int id, String name, String avatar, boolean game_request_sent ){
        mId = id;
        mName = name;
        mAvatar = avatar;
        mGameRequestSent = game_request_sent;
    }

    public boolean getGameRequestSent(){
        return mGameRequestSent;
    }
    public void setGameRequestSent( boolean flag ){
        mGameRequestSent = flag;
    }

    public int getId(){
        return mId;
    }

    public String getName(){
        return mName;
    }

    // avatar data class i yap
    public String getAvatar(){
        return mAvatar;
    }

}

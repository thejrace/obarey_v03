package com.example.jeppe.obarey_v03;

/**
 * Created by Jeppe on 12.04.2016.
 */
public class RequestData {

    private FriendData mFriendData;
    private boolean mRequestSent;
    private int mSeen;

    // kullanicinin gonderdikleri
    public RequestData( FriendData friend_data, boolean request_sent ){
        mFriendData = friend_data;
        mRequestSent = request_sent;
    }

    // kullaniciya gelen
    public RequestData( FriendData friend_data, int seen ){
        mFriendData = friend_data;
        mSeen = seen;
    }

    public boolean getRequestSeen(){
        if( mSeen == 1 ){
            return true;
        }
        return false;
    }
    public void setRequestSeen( int flag ){
        mSeen = flag;
    }

    public FriendData getFriendData(){
        return mFriendData;
    }
    public boolean getRequestSent(){
        return mRequestSent;
    }

    public void setRequestSent( boolean flag ){
        mRequestSent = flag;
    }

}

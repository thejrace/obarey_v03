package com.example.jeppe.obarey_v03;

/**
 * Created by Jeppe on 12.04.2016.
 */
public class UserStatsData {

    private int mLevel, mPoints, mLeaderboard, mWins, mLoses;
    public UserStatsData( int level, int points, int leaderboard, int wins, int loses){
        mLevel = level;
        mPoints = points;
        mLeaderboard = leaderboard;
        mWins = wins;
        mLoses = loses;
    }
    public int getLevel(){
        return mLevel;
    }
    public int getPoints(){
        return mPoints;
    }
    public int getLeaderboard(){
        return mLeaderboard;
    }
    public int getWins(){
        return mWins;
    }
    public int getLoses(){
        return mLoses;
    }
}

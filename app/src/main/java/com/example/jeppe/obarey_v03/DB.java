package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jeppe on 12.03.2016.
 */
public class DB extends SQLiteOpenHelper {
    private static DB sInstance;
    private static final Integer DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "obarey_v0",
    DBT_FRIENDS = "user_friends",
    DBT_USER_DATA = "user_data",
    DBT_USER_FINISHED_GAMES = "user_finished_games",
    DBT_USER_ACTIVE_GAMES = "user_active_games",
    DBT_USER_FRIEND_REQUESTS = "user_friend_requests",
    DBT_USER_GAME_REQUESTS = "user_game_requests",
    DBT_USER_GAME_REQUESTS_SENT = "user_game_requests_sent",
    DBT_USER_TEMP_QUESTION = "temp_question";

    public static synchronized DB getInstance( Context context ){
        if( sInstance == null ){
            sInstance = new DB( context.getApplicationContext() );
        }
        return sInstance;
    }

    private DB( Context context ){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user_data( id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, name TEXT, email TEXT, level INTEGER, points INTEGER, leaderboard INTEGER, wins INTEGER, loses INTEGER, avatar TEXT )");
        db.execSQL("CREATE TABLE IF NOT EXISTS user_friends( id INTEGER PRIMARY KEY AUTOINCREMENT, friend_id INTEGER, friend_name  TEXT, friend_avatar  TEXT )");
        db.execSQL("CREATE TABLE IF NOT EXISTS user_friend_requests( id INTEGER PRIMARY KEY AUTOINCREMENT, friend_id INTEGER, friend_name TEXT, friend_avatar TEXT, request_seen  INTEGER )");
        db.execSQL("CREATE TABLE IF NOT EXISTS user_finished_games( id INTEGER PRIMARY KEY AUTOINCREMENT, game_id INTEGER, friend_id INTEGER, users_points  INTEGER, friends_points INTEGER )");
        db.execSQL("CREATE TABLE IF NOT EXISTS user_active_games( id INTEGER PRIMARY KEY AUTOINCREMENT, game_id INTEGER, friend_id INTEGER, round INTEGER, play_turn INTEGER ,users_points  INTEGER, friends_points INTEGER )");
        db.execSQL("CREATE TABLE IF NOT EXISTS user_game_requests( id INTEGER PRIMARY KEY AUTOINCREMENT, friend_id INTEGER, friend_name TEXT, friend_avatar TEXT, request_seen  INTEGER )");
        db.execSQL("CREATE TABLE IF NOT EXISTS user_game_requests_sent ( id INTEGER PRIMARY KEY AUTOINCREMENT, friend_id INTEGER )");
        //db.execSQL("CREATE TABLE IF NOT EXISTS temp_question ( id INTEGER PRIMARY KEY AUTOINCREMENT, friend_id INTEGER, question TEXT, option_0 TEXT, option_1 TEXT, option_2 TEXT, option_3 TEXT, answer INTEGER )" );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion) {
        if( newVersion > oldVersion )
            db.execSQL("DROP TABLE IF EXISTS user_data");
            db.execSQL("DROP TABLE IF EXISTS user_friends");
            db.execSQL("DROP TABLE IF EXISTS user_friend_requests");
            db.execSQL("DROP TABLE IF EXISTS user_finished_games");
            db.execSQL("DROP TABLE IF EXISTS user_active_games");
            db.execSQL("DROP TABLE IF EXISTS user_game_requests");
            onCreate(db);
    }
}

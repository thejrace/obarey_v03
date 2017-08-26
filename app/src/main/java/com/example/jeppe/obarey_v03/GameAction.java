package com.example.jeppe.obarey_v03;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeppe on 15.04.2016.
 */
public class GameAction implements AsyncResponse {

    private Activity mActivity;
    private int mId, mGameId;
    private GameDataBundle mBundle;
    private GameData mGameData;

    private AdapterStartGame mStartGameAdapter;
    private AdapterGame mGameAdapter;
    private AdapterRequest mRequestAdapter;
    private int mAdapterPos;

    // Requests
    public GameAction( Activity ctx, int friend_id ){
        mActivity = ctx;
        mId = friend_id;
        mBundle = new GameDataBundle( ctx );
    }

    // ActivityGame
    public GameAction( Activity ctx, GameData game_data ){
        mActivity = ctx;
        mBundle = new GameDataBundle( ctx );
        mGameData = game_data;
        mId = mGameData.getFriendId();
    }

    public void setAdapterData( AdapterStartGame adapter, int pos ){
        mStartGameAdapter = adapter;
        mAdapterPos = pos;
    }

    public void setAdapterData( AdapterRequest adapter, int pos ){
        mRequestAdapter = adapter;
        mAdapterPos = pos;
    }

    // todo requestleri game action dan ayir
    private void serverRequest( String type ){
        PopupLoader.show(mActivity);
        Map<String, String> params= new HashMap<>();
        params.put("type", type);
        params.put("friend_id", String.format("%s", mId ));
        ServerRequest req = new ServerRequest(ServerRequest.SERVICE_GAME_REQUEST, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }

    // ActivityGame
    // @actiontype ( pas, answer vs. )
    // @iscorrect ( dogru mu yanlis mi ) ( hack check )
    public void sendAnswer( int action_type, int answer_pos, boolean is_correct ){
        Map<String, String> params= new HashMap<>();
        params.put("type", "do_update_game");
        ///params.put("friend_id", String.format("%s", mId ));
        params.put("game_id", String.format( "%s", mGameData.getGameId()) );
        params.put("action_type", String.format( "%s", action_type) );
        params.put("is_correct", String.format( "%s", is_correct) );
        params.put("answer_pos", String.format( "%s", answer_pos) );
        params.put("round", String.format( "%s", mGameData.getRound()) );
        ServerRequest req = new ServerRequest(ServerRequest.SERVICE_GAME_ACTION, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }

    public void cancelRequest(){ serverRequest("do_cancel_request");}
    public void sendRequest(){ serverRequest("do_send_request");}
    public void acceptRequest(){ serverRequest( "do_accept_request" );}
    public void ignoreRequest(){ serverRequest( "do_ignore_request" );}
    public void withDraw(){

    }


    @Override
    public void processFinishJSON( JSONObject output ){
        System.out.println(output);

        try {
            String type = output.getString("type");
            JSONObject gameData;
            switch( type ){
                case "do_withdraw_game":
                    mBundle.deleteGame(mId);
                    // start game adapter data
                    //mGameAdapter.setNewData( mBundle.getGames() );
                    //mGameAdapter.notifyDataSetChanged();
                break;

                case "do_accept_request":
                    // db ye ekle oyunu ( yeni oyun oldugu icin server dan sadece game_id yi aliyorum )
                    mBundle.addGame( new GameData( output.getInt("game_id"), mId, 1, User.ID, 0, 0) );
                    // requesti sil
                    mBundle.deleteGameRequest( mId);
                    // adapter refresh
                    mRequestAdapter.setNewData(mBundle.getGameRequests() );
                    mRequestAdapter.notifyDataSetChanged();
                break;

                case "do_ignore_request":
                    mBundle.deleteGameRequest(mId);
                    mRequestAdapter.setNewData(mBundle.getGameRequests());
                    mRequestAdapter.notifyDataSetChanged();
                break;

                case "do_cancel_request":
                    mBundle.deleteGameRequestSent( mId );
                    mStartGameAdapter.updateRequestSentFlag( mAdapterPos );
                break;

                case "do_send_request":
                    mBundle.addGameRequestSent( mId );
                    mStartGameAdapter.updateRequestSentFlag( mAdapterPos );
                break;

                case "do_update_game":
                    gameData = output.getJSONObject("game_data");
                    GameData updatedGameData = new GameData( gameData.getInt("id"), mId, gameData.getInt("round"), gameData.getInt("play_turn"), gameData.getInt("user_wins"), gameData.getInt("opponents_wins") );
                    mBundle.updateDBGame( updatedGameData );
                    boolean answerValid = output.getBoolean("answer_valid");
                    boolean gameFinishedFlag = output.getBoolean("game_finished_flag");

                    if( gameFinishedFlag ){
                        // oyunu bitir, db guncellemeleri yap ( sil active den falan )
                        // activity bitir
                    }

                    if( answerValid ){
                        // cevap dogruysa oyuna devam, yeni soru
                        PlayAttempt playAttempt = new PlayAttempt( mActivity, updatedGameData );
                        playAttempt.requestNewQuestion(true);
                    } else{
                        // yanlissa ana activity ye don
                        mActivity.finish();
                    }
                break;
            }
        } catch( JSONException e ){
            System.out.println( "GameAction json " + e.toString());
        }
        PopupLoader.hide();
    }
}

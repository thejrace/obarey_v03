package com.example.jeppe.obarey_v03;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeppe on 16.04.2016.
 */
public class PlayAttempt implements AsyncResponse {

    private Activity mActivity;
    private GameData mGameData;
    private boolean mActivityFinish;
    // FragmentTabGames
    public PlayAttempt( Activity ctx, GameData game_data ){
        mActivity = ctx;
        mGameData = game_data;
    }

    public void requestNewQuestion( boolean activity_finish ){
        mActivityFinish = activity_finish;
        Map<String, String> params= new HashMap<>();
        params.put("type", "do_get_question");
        params.put("game_id", String.format("%s", mGameData.getGameId()));
        ServerRequest req = new ServerRequest(ServerRequest.SERVICE_GAME_ACTION, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }


    @Override
    public void processFinishJSON( JSONObject output ){
        System.out.println( output );
        try {
            Intent gameIntent = new Intent(mActivity, ActivityGame.class);
            // oyuna girip geri cikmissa server side timer kontrolu
            if( output.getBoolean("timeout_flag") ){
                gameIntent.putExtra("timeout_flag", true );
            } else {
                JSONObject questionData = output.getJSONObject("question_data");
                JSONArray options = questionData.getJSONArray("options");
                List<String> optionsList = new ArrayList<>();
                for (int i = 0; i < options.length(); i++) optionsList.add(options.getString(i));
                int answer = questionData.getInt("answer");
                // soru
                QuestionData question = new QuestionData( mGameData.getGameId(), questionData.getString("question"), optionsList, answer);
                // game activity baslat
                gameIntent.putExtra("game_data", mGameData);
                gameIntent.putExtra("question_data", question);
                // onceki sorunun varsa activitysini kapat
                if (mActivityFinish) mActivity.finish();
            }
            mActivity.startActivityForResult(gameIntent, 5);
        } catch( JSONException e ){
            System.out.println("PlayAttempt json " + e.toString());
        }

        //mBundle.addTempQuestion()

    }
}

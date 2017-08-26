package com.example.jeppe.obarey_v03;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jeppe on 16.04.2016.
 */
public class QuestionData implements Serializable {

    private String mQuestion;
    private List<String> mOptions;
    private int mAnswer;
    private int mGameId;
    public QuestionData( int game_id, String question, List<String> options, int answer ){
        mQuestion = question;
        mOptions = options;
        mAnswer = answer;
        mGameId = game_id;
    }
    public int getGameId(){
        return mGameId;
    }

    public String getQuestion(){
        return mQuestion;
    }
    public List<String> getOptions(){
        return mOptions;
    }
    public int getAnswer(){
        return mAnswer;
    }

}

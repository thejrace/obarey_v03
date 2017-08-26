package com.example.jeppe.obarey_v03;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 16.04.2016.
 */
public class ActivityGame extends AppCompatActivity{

    private int mCorrectAnswer;
    private boolean mIsUserAnswerCorrect;
    private List<LinearLayout> mOptionButtons = new ArrayList<>();
    private List<Button> mActionButtons = new ArrayList<>();
    private GameAction mGameAction;
    private GameData mGameData;
    private Handler mHandler;
    private int mTimeLimit = 30;
    private TextView mTimerText;

    private RelativeLayout mPopupOverlay;

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        appNameBarHide();
        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_game);
        initLoader();

        Intent gameIntent = getIntent();
        // timeout olduysak bitir activityi
        boolean timeoutFlag = (Boolean)gameIntent.getBooleanExtra( "timeout_flag", false );
        if( timeoutFlag ){
            // game activity finish
            finish();
            return;
        }
        GameData gameData = (GameData)gameIntent.getSerializableExtra("game_data");
        QuestionData questionData = (QuestionData)gameIntent.getSerializableExtra("question_data");

        GameDataBundle gameDataBundle = new GameDataBundle( this );
        FriendData friendData = gameDataBundle.getFriend(gameData.getFriendId());
        mCorrectAnswer = questionData.getAnswer();
        mGameData = gameData;

        mGameAction = new GameAction( this, mGameData );

        // header info
        TextView userName = (TextView)findViewById(R.id.game_header_user_name);
        TextView roundCount = (TextView)findViewById(R.id.game_header_round_count);
        mTimerText = (TextView)findViewById(R.id.game_header_timer);

        // soru - cevaplar
        TextView questionWrapper = (TextView)findViewById(R.id.question);
        TextView option0Content = (TextView)findViewById(R.id.option_a_content);
        TextView option1Content = (TextView)findViewById(R.id.option_b_content);
        TextView option2Content = (TextView)findViewById(R.id.option_c_content);
        TextView option3Content = (TextView)findViewById(R.id.option_d_content);

        // cevap buttonlari
        LinearLayout option0 = (LinearLayout)findViewById(R.id.option_a);
        LinearLayout option1 = (LinearLayout)findViewById(R.id.option_b);
        LinearLayout option2 = (LinearLayout)findViewById(R.id.option_c);
        LinearLayout option3 = (LinearLayout)findViewById(R.id.option_d);
        mOptionButtons.add( option0 );
        mOptionButtons.add( option1 );
        mOptionButtons.add( option2 );
        mOptionButtons.add( option3 );

        // popups
        mPopupOverlay = (RelativeLayout)findViewById(R.id.game_popup_overlay);

        // extra buttonlar
        Button actionPas = (Button)findViewById(R.id.game_action_pas);
        Button actionFiftyFifty = (Button)findViewById(R.id.game_action_fifty_fifty);
        Button actionDoubleAnswer = (Button)findViewById(R.id.game_action_double_answer);
        mActionButtons.add(actionPas);
        mActionButtons.add(actionFiftyFifty);
        mActionButtons.add(actionDoubleAnswer);

        // header ui set
        userName.setText( friendData.getName() );
        roundCount.setText( String.format("%s. Tur", gameData.getRound()) );

        // question - answer ui set
        questionWrapper.setText( questionData.getQuestion() );
        option0Content.setText(questionData.getOptions().get(0));
        option1Content.setText(questionData.getOptions().get(1));
        option2Content.setText(questionData.getOptions().get(2));
        option3Content.setText(questionData.getOptions().get(3));

        // option onclick
        for( int i = 0; i < 4; i++ ){
            final int iRef = i;
            mOptionButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer( iRef );
                }
            });
        }
        // action buttons onclick
        for( int i = 0; i < 3; i++ ){
            final int iRef = i;
            mActionButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // action
                    actionButtonHandler(iRef);
                }
            });
        }
        // timer baslat view olusturul olusturulmaz
        startTimer();
    }

    private void actionButtonHandler( int type ){
        switch( type ){
            // pas
            case 0:
                showPopup(2, getResources().getString(R.string.game_popup_pass));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGameAction.sendAnswer(2, 4, false);
                    }
                }, 1000);
            break;
            // fifty fifty
            case 1:
                // puan kontrolu
            break;
            // cift cevap
            case 2:
                // puan kontrolu
            break;
        }
    }

    private void checkAnswer( int pos ){
        stopTimer();
        LinearLayout button = mOptionButtons.get(pos);
        disableClicks();
        if( mCorrectAnswer == pos ){
            mIsUserAnswerCorrect = true;
            button.setBackgroundColor(getResources().getColor(R.color.option_correct_background));
            showPopup(1, getResources().getString(R.string.game_popup_correct));
        } else {
            mIsUserAnswerCorrect = false;
            button.setBackgroundColor(getResources().getColor(R.color.option_wrong_background));
            showPopup(0, getResources().getString(R.string.game_popup_wrong));
        }
        /*
        * action_type
        *   1 - cevap
        *   2 - pas
        *   3 - sure bitti
        * */
        final int posRef = pos;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mGameAction.sendAnswer(1, posRef, mIsUserAnswerCorrect);
            }
        }, 1000);

    }

    // timer init
    Runnable mGameTimer = new Runnable(){
        @Override
        public void run(){
            try {
                mTimeLimit--;
                mTimerText.setText(String.format("%s", mTimeLimit));
                if( mTimeLimit == 0 ){
                    showPopup(0, getResources().getString(R.string.game_popup_timeup));
                    // delay
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mGameAction.sendAnswer(3, 4, false);
                        }
                    }, 1000);
                }
            } finally{
                mHandler.postDelayed(mGameTimer, 1000);
            }
        }
    };

    private void startTimer(){
        mHandler = new Handler();
        initTimerFunction();
    }
    private void initTimerFunction(){
        mGameTimer.run();
    }
    private void stopTimer(){
        mHandler.removeCallbacks(mGameTimer);
    }

    private void disableClicks(){
        for( LinearLayout button : mOptionButtons  ){
            button.setClickable(false);
        }
        for( Button button : mActionButtons  ){
            button.setClickable(false);
        }
    }

    private void showPopupOverlay(){
        mPopupOverlay.setVisibility(View.VISIBLE);
    }

    private void hidePopupOverlay(){
        mPopupOverlay.setVisibility(View.GONE);
    }

    private void showPopup( int type, String text ){
        showPopupOverlay();
        LinearLayout popupContent = (LinearLayout)findViewById(R.id.game_popup_content);
        TextView popupText = (TextView)findViewById(R.id.popup_text);
        int color;
        if( type == 0 ){
            color = getResources().getColor(R.color.option_wrong_background);
        } else if( type == 1 ){
            color = getResources().getColor(R.color.option_correct_background);
        } else if ( type == 2 ){
            color = getResources().getColor(R.color.option_pass_background);
        } else {
            color = getResources().getColor(R.color.main_background);
        }
        popupContent.setBackgroundColor( color );
        popupContent.setVisibility(View.VISIBLE);
        popupText.setText(text);
    }


    private void initLoader(){
        // loader init
        ViewGroup layoutBody = (ViewGroup) findViewById(R.id.layout_body);
        PopupLoader.setBody(layoutBody);
    }

    public void appNameBarHide(){
        // app name bari gizle
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
    }
}

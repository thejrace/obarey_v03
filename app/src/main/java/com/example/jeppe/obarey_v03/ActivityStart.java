package com.example.jeppe.obarey_v03;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jeppe on 08.04.2016.
 */
public class ActivityStart extends Activity {

    @Override
    protected void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_start);
        Session session = new Session( this );
        final Context thisRef = this;

        ViewGroup layoutBody = (ViewGroup) findViewById(R.id.layout_body);
        PopupLoader.setBody( layoutBody );

        if( session.isLoggedin()){
            // show loader
            // static user id, name
            User.storeInfo(this);
            PopupLoader.show(this);
            //Logout logout = new Logout( this );
            //logout.action();
            GameDataBundle bundle = new GameDataBundle( this );
            // request tamamlaninca go to ActivityMain
            bundle.requestAll();

        } else {
            // form
            LinearLayout layoutFirstStep = (LinearLayout) findViewById(R.id.start_first_step);
            LinearLayout layoutSecondStepLogin = (LinearLayout) findViewById(R.id.start_second_step_login);
            LinearLayout layoutSecondStepRegister = (LinearLayout) findViewById(R.id.start_second_step_register);
            LinearLayout layoutSpinner = (LinearLayout) findViewById(R.id.loading_spinner);

            // first step show
            layoutSpinner.setVisibility(View.GONE);
            layoutFirstStep.setVisibility(View.VISIBLE);

            // layout chain
            final ArrayList<LinearLayout> layouts = new ArrayList<>();
            layouts.add(layoutFirstStep);
            layouts.add(layoutSecondStepLogin);
            layouts.add(layoutSecondStepRegister);

            Button firstStepSubmit = (Button)findViewById(R.id.forward);
            final TextView errorNotfFirstStep = (TextView)findViewById(R.id.step_one_error_notf);
            final EditText inputEmail = (EditText)findViewById(R.id.email);
            firstStepSubmit.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick( View v ){
                    String emailVal = inputEmail.getText().toString();
                    if( !emailVal.isEmpty() ) {
                        LoginAttempt loginAttempt = new LoginAttempt( thisRef, emailVal, layouts );
                        loginAttempt.start();
                    } else {
                        errorNotfFirstStep.setText( R.string.form_error_email_empty );
                    }
                }
            });

        }

    }

}

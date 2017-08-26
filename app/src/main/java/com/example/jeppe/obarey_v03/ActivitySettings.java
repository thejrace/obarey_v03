package com.example.jeppe.obarey_v03;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Jeppe on 15.04.2016.
 */
public class ActivitySettings extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState){
        appNameBarHide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initLoader();

        final Activity thisRef = this;
        // gecici
        Button logoutBtn = (Button)findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout logout = new Logout( thisRef );
                logout.action();

            }
        });

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

package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 08.04.2016.
 */
public class ActivityMain extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedStateInstance) {
        appNameBarHide();
        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_main);
        // login, registerden geldiysek
        User.storeInfo(this);
        System.out.println(User.ID);
        System.out.println(User.NAME);
        loaderInit();
        // tab init
        initTabs();

        // header fragment init
        FragmentHeader fragment = new FragmentHeader();
        getFragmentManager().beginTransaction().add(R.id.header_fragment, fragment).commit();
    }



    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ){
        super.onActivityResult( requestCode, resultCode, data );
        final ActivityMain thisRef = this;
        // http://stackoverflow.com/questions/10844112/runtimeexception-performing-pause-of-activity-that-is-not-resumed
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                thisRef.recreate();
            }
        }, 1);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loaderInit();
        // bundle get all
        // request kabul edildiginde orada requestData yap burada yalnizca db den al
    }

    private void loaderInit(){
        // loader init
        ViewGroup layoutBody = (ViewGroup) findViewById(R.id.layout_body);
        PopupLoader.setBody(layoutBody);
    }

    private void initTabs(){
        TabLayout tabLayout;
        ViewPager viewPager;

        // tab init
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter( getSupportFragmentManager() );
        vpAdapter.addFragment( new FragmentTabGames(), "");
        vpAdapter.addFragment( new FragmentTabStartGame(), "");
        vpAdapter.addFragment(new FragmentTabChat(), "");
        viewPager.setAdapter(vpAdapter);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        // icon
        TextView gamesTab = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_main_acitvity_custom, null);
        gamesTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ico_tab_games, 0, 0);
        tabLayout.getTabAt(0).setCustomView(gamesTab);

        TextView friendsTab = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_main_acitvity_custom, null);
        friendsTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ico_tab_start_game, 0, 0);
        tabLayout.getTabAt(1).setCustomView(friendsTab);
        TextView chatTab = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_main_acitvity_custom, null);
        chatTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ico_tab_message, 0, 0);
        tabLayout.getTabAt(2).setCustomView(chatTab);

        // selection event
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int tabPos = tab.getPosition();
                if (tabPos == 0) {
                    // timer la yaparsak bunu guzel olur
                    //System.out.println("GAMEEEEZ SELECTEEED");
                    //mFragmentTabGames.refreshFromActivity();
                }
            }
        });
    }

    public void appNameBarHide(){
        // app name bari gizle
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
    }

}

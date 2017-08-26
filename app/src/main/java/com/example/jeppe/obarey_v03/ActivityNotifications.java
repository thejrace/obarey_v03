package com.example.jeppe.obarey_v03;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jeppe on 14.04.2016.
 */
public class ActivityNotifications extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState){
        appNameBarHide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initLoader();
        initTabs();

        // todo ilk giriste tum seen 0 lari 1 yap

    }
    private void initLoader(){
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
        vpAdapter.addFragment(new FragmentTabGameRequests(), "");
        vpAdapter.addFragment(new FragmentTabFriendRequests(), "");
        viewPager.setAdapter(vpAdapter);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        // icon
        TextView gameReqsTab = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_main_acitvity_custom, null);
        gameReqsTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ico_tab_cd, 0, 0);
        tabLayout.getTabAt(0).setCustomView(gameReqsTab);

        TextView friendReqsTab = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_main_acitvity_custom, null);
        friendReqsTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ico_tab_friend_requests, 0, 0);
        tabLayout.getTabAt(1).setCustomView(friendReqsTab);

        // selection event
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                //int tabPos = tab.getPosition();
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

package com.example.jeppe.obarey_v03;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Jeppe on 11.04.2016.
 */
public class FragmentHeader extends Fragment {

    public FragmentHeader() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_header, container, false);

        // main activity data al
        final Activity mainActivity = getActivity();
        GameDataBundle mGameDataBundle = new GameDataBundle( mainActivity );

        UserStatsData data = mGameDataBundle.getUserStats();

        TextView userName = (TextView) rootView.findViewById(R.id.header_user_name);
        TextView userPoints = (TextView) rootView.findViewById(R.id.header_user_points);
        TextView userLevel = (TextView) rootView.findViewById(R.id.header_user_level);

        userName.setText( User.NAME );
        userPoints.setText( String.format( "%s", data.getPoints() ));
        userLevel.setText( String.format("%s", data.getLevel()));

        // gecici
        ImageButton settingsBtn = (ImageButton)rootView.findViewById(R.id.settings_notf_btn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mainActivity, ActivitySettings.class );
                mainActivity.startActivity(intent);
            }
        });

        ImageButton notfBtn = (ImageButton)rootView.findViewById(R.id.requests_notf_btn);
        notfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mainActivity, ActivityNotifications.class );
                mainActivity.startActivityForResult(intent, 5);
            }
        });


        return rootView;
    }
}

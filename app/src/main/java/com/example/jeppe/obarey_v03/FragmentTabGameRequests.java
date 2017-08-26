package com.example.jeppe.obarey_v03;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by Jeppe on 14.04.2016.
 */
public class FragmentTabGameRequests extends Fragment {

    public FragmentTabGameRequests() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_requests, container, false);
        // GAMES count 0 sa hata veriyor

        final Activity mainActivity = getActivity();
        final GameDataBundle gameDataBundle = new GameDataBundle( mainActivity );

        // listview
        ListView requestsListView = (ListView)rootView.findViewById(R.id.game_requests_listview);
        final AdapterRequest adapter = new AdapterRequest( AdapterRequest.GAME_REQUEST, mainActivity, gameDataBundle.getGameRequests() );
        requestsListView.setAdapter(adapter);

        // refresh
        ImageButton refresh = (ImageButton)rootView.findViewById(R.id.refresh_game_requests);
        refresh.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View v ){
                PopupLoader.show( mainActivity );
                gameDataBundle.requestGameRequestsWithAdapterRefresh( adapter );
            }
        });

        return rootView;
    }
}

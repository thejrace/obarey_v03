package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 12.04.2016.
 */
public class ActivityAddFriend extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        // loader init
        ViewGroup layoutBody = (ViewGroup) findViewById(R.id.layout_body);
        PopupLoader.setBody(layoutBody);

        // views
        ImageButton searchButton = (ImageButton)findViewById(R.id.search_friend_button);
        final EditText input = (EditText)findViewById(R.id.friend_search_input);
        final TextView searchNotf = (TextView)findViewById(R.id.friend_search_notf);
        ListView resultList = (ListView)findViewById(R.id.friend_search_list_view);

        // listview init ( baslangicta bos )
        final List<RequestData> emptyList = new ArrayList<>();
        final AdapterFriendSearch searchAdapter = new AdapterFriendSearch( this, emptyList );
        resultList.setAdapter(searchAdapter);

        // search class init
        final FriendSearch friendSearch = new FriendSearch( this );

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = input.getText().toString();
                if( !query.isEmpty() ){
                    friendSearch.make( query, searchAdapter, searchNotf );
                } else {
                    // arama yaptiktan sonra silip aratirsa temizle listeyi
                    searchAdapter.clearList();
                    searchNotf.setText("");
                }
            }
        });

    }

}

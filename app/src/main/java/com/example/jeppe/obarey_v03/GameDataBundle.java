package com.example.jeppe.obarey_v03;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeppe on 08.04.2016.
 */
public class GameDataBundle implements AsyncResponse {

    private Context mContext;
    private SQLiteDatabase db;
    // request sonrasi listview update icin
    private AdapterGame mGameAdapter;
    private AdapterRequest mRequestAdapter;
    private boolean mAdapterUpdateFlag = false;

    public GameDataBundle( Context context ){
        mContext = context;
        db = DB.getInstance(mContext).getWritableDatabase();
    }
    private void request( String type, String url ){
        Map<String, String> params= new HashMap<>();
        params.put("type", type);
        ServerRequest req = new ServerRequest(url, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }
    // ActivityStart ilk acilista guncelleme
    public void requestAll(){
        request("do_get_user_data", ServerRequest.SERVICE_USER_DATA);
    }
     // ayri request
    public void requestGameRequests(){
        request( "do_get_game_requests", ServerRequest.SERVICE_USER_DATA );
    }
    public void requestFriendRequests(){
        request( "do_get_friend_requests", ServerRequest.SERVICE_USER_DATA );
    }
    public void requestGames(){
        request("do_get_user_active_games", ServerRequest.SERVICE_USER_DATA);
    }

    // listview refresh
    public void requestGamesWithAdapterRefresh( AdapterGame adapter ){
        mAdapterUpdateFlag = true;
        mGameAdapter = adapter;
        requestGames();
    }
    // FragmentTabGameRequests
    public void requestGameRequestsWithAdapterRefresh( AdapterRequest adapter ){
        mAdapterUpdateFlag = true;
        mRequestAdapter = adapter;
        requestGameRequests();
    }
    // FragmentTabFriendRequests
    public void requestFriendRequestsWithAdapterRefresh( AdapterRequest adapter ){
        mAdapterUpdateFlag = true;
        mRequestAdapter = adapter;
        requestFriendRequests();
    }

    public List<GameData> getGames(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB.DBT_USER_ACTIVE_GAMES, null);
        List<GameData> output = new ArrayList<>();
        while( cursor.moveToNext() ){
            output.add( new GameData( cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6) ) );
        }
        cursor.close();
        return output;
    }
    public FriendData getFriend( int id ){
        Cursor cursor = db.query(DB.DBT_FRIENDS, new String[]{"friend_id, friend_name, friend_avatar"}, "friend_id = ?", new String[]{String.format("%s", id)}, null, null, null);
        FriendData output = null;
        if( cursor.getCount() == 1 ){
            while( cursor.moveToNext() ){
                output = new FriendData(cursor.getInt(0), cursor.getString(1), cursor.getString(2) );
            }
            cursor.close();
        }
        return output;
    }
    public List<FriendData> getFriends(){
        List<FriendData> output = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT friend_id, friend_name, friend_avatar FROM " + DB.DBT_FRIENDS, null );
        if( cursor.getCount() > 0 ){
            while( cursor.moveToNext() ){
                output.add( new FriendData( cursor.getInt(0), cursor.getString(1), cursor.getString(2) ) );
            }
        }
        return output;
    }
    public List<FriendData> getFriendsToStartGame(){
        List<GameData> activeGames = getGames();
        List<FriendData> output = new ArrayList<>();
        List<Integer> requestsSent = new ArrayList<>();
        List<Integer> requestsReceived = new ArrayList<>();
        int i = 0, length = activeGames.size();

        Cursor cursor = db.rawQuery("SELECT friend_id, friend_name, friend_avatar FROM " + DB.DBT_FRIENDS, null);
        if( cursor.getCount() > 0 ){
            boolean matchFound;
            boolean requestSent;
            while( cursor.moveToNext() ){
                matchFound = false;
                requestSent = false;
                for( i = 0; i < length; i++ ) {
                    // aktif oyun varsa ekleme listeye
                    if (activeGames.get(i).getFriendId() == cursor.getInt(0)){
                        matchFound = true;
                        break;
                    }
                }
                if( !matchFound ) {
                    // zaten istek gonderildiginde ve arkadas istek gonderdiginde bayrak true oluyor
                    requestsSent = getGameRequestsSent();
                    List<RequestData> requestsAll = getGameRequests();
                    // friend_id leri listele
                    for( RequestData data : requestsAll ) requestsReceived.add( data.getFriendData().getId() );
                    if( requestsSent.contains( cursor.getInt(0) ) || requestsReceived.contains( cursor.getInt(0) ) ) requestSent = true;
                    output.add( new FriendData( cursor.getInt(0), cursor.getString(1), cursor.getString(2), requestSent ) );
                }
            }
            cursor.close();
        }
        return output;
    }
    public UserStatsData getUserStats(){
        Cursor cursor = db.rawQuery("SELECT level, points, leaderboard, wins, loses FROM " + DB.DBT_USER_DATA, null);
        UserStatsData output = null;
        while( cursor.moveToNext() ){
            output = new UserStatsData( cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4) );
        }
        cursor.close();
        return output;
    }
    public List<RequestData> getFriendRequests(){
        Cursor cursor = db.rawQuery("SELECT friend_id, friend_name, friend_avatar, request_seen FROM " + DB.DBT_USER_FRIEND_REQUESTS, null);
        List<RequestData> output = new ArrayList<>();
        while( cursor.moveToNext() ){
            output.add( new RequestData( new FriendData( cursor.getInt(0), cursor.getString(1), cursor.getString(2)), cursor.getInt(3) ) );
        }
        cursor.close();
        return output;
    }
    public List<RequestData> getGameRequests(){
        Cursor cursor = db.rawQuery("SELECT friend_id, friend_name, friend_avatar, request_seen FROM " + DB.DBT_USER_GAME_REQUESTS, null);
        List<RequestData> output = new ArrayList<>();
        while( cursor.moveToNext() ){
            output.add( new RequestData( new FriendData( cursor.getInt(0), cursor.getString(1), cursor.getString(2)), cursor.getInt(3) ) );
        }
        cursor.close();
        return output;
    }
    public List<Integer> getGameRequestsSent(){
        Cursor cursor = db.rawQuery("SELECT friend_id FROM " + DB.DBT_USER_GAME_REQUESTS_SENT, null);
        List<Integer> output = new ArrayList<>();
        while( cursor.moveToNext() ){
            output.add(cursor.getInt(0));
        }
        cursor.close();
        return output;
    }

    /*public void addTempQuestion( QuestionData question ){
        ContentValues vals = new ContentValues();
        int i = 0;
        vals.put("question", question.getQuestion());
        for( String option : question.getOptions() )vals.put("option_"+i, option );
        vals.put("answer", question.getAnswer());
        vals.put("friend_id", question.getFriendId() );
        db.insert(DB.DBT_USER_TEMP_QUESTION, null, vals);
        vals.clear();
    }*/

    /*public QuestionData getTempQuestion( int friend_id ){
        Cursor cursor = db.query(DB.DBT_USER_TEMP_QUESTION, new String[]{"friend_id, question, option_0, option_1, option_2, option_3, answer"}, "friend_id = ?", new String[]{String.format("%s", friend_id)}, null, null, null);
        QuestionData output = null;
        if( cursor.getCount() == 1 ){
            while( cursor.moveToNext() ){
                output = new QuestionData(cursor.getInt(0), cursor.getString(1), cursor.getString(2) );
            }
            cursor.close();
        }
        return output;
    }*/

    public void addGameRequestSent( int friend_id ){
        ContentValues vals = new ContentValues();
        vals.put("friend_id", friend_id );
        db.insert(DB.DBT_USER_GAME_REQUESTS_SENT, null, vals);
        vals.clear();
    }
    public void deleteGameRequestSent( int friend_id ){
        db.delete(DB.DBT_USER_GAME_REQUESTS_SENT, "friend_id = ?", new String[]{String.format("%s", friend_id)});
    }

    public void deleteFriendRequest( int friend_id ){
        db.delete(DB.DBT_USER_FRIEND_REQUESTS, "friend_id = ?", new String[]{String.format("%s", friend_id)});
    }
    public void deleteGameRequest( int friend_id ){
        db.delete(DB.DBT_USER_GAME_REQUESTS, "friend_id = ?", new String[]{String.format("%s", friend_id)});
    }

    public void addFriend( FriendData data ){
        ContentValues vals = new ContentValues();
        vals.put("friend_id", data.getId() );
        vals.put("friend_name", data.getName() );
        vals.put("friend_avatar", data.getAvatar() );
        db.insert(DB.DBT_FRIENDS, null, vals);
        vals.clear();
    }
    public void deleteFriend( int friend_id ){
        db.delete(DB.DBT_FRIENDS, "friend_id = ?", new String[]{String.format("%s", friend_id)});
    }

    public void addGame( GameData data ){
        ContentValues vals = new ContentValues();
        vals.put("game_id", data.getGameId() );
        vals.put("friend_id", data.getFriendId() );
        vals.put("friends_points", data.getFriendsPoints() );
        vals.put("users_points",data.getUsersPoints() );
        vals.put("round", data.getRound() );
        vals.put("play_turn", data.getPlayTurn() );
        db.insert(DB.DBT_USER_ACTIVE_GAMES, null, vals);
        vals.clear();
    }
    public void deleteGame( int friend_id ){
        db.delete(DB.DBT_USER_ACTIVE_GAMES, "friend_id = ?", new String[]{String.format("%s", friend_id)});
    }

    public void updateDBGame( GameData game ){
        ContentValues vals = new ContentValues();
        vals.put("friend_id", game.getFriendId());
        vals.put("round", game.getRound());
        vals.put("play_turn", game.getPlayTurn());
        vals.put("friends_points", game.getFriendsPoints() );
        vals.put("users_points", game.getUsersPoints());
        db.update(DB.DBT_USER_ACTIVE_GAMES, vals, " game_id = ?", new String[]{String.format("%s", game.getGameId())});
        vals.clear();
    }

    private void updateDBGames( JSONArray games ){
        if( games.length() > 0 ){
            try{
                for( int i = 0; i < games.length(); i++ ){
                    ContentValues vals = new ContentValues();
                    JSONObject game = games.getJSONObject(i);
                    Cursor cursor = db.rawQuery("SELECT * FROM " + DB.DBT_USER_ACTIVE_GAMES + " WHERE game_id = ?", new String[]{ String.format("%s", game.getInt("id"))});
                    vals.put("game_id", game.getInt("id"));
                    vals.put("friend_id", game.getInt("opponents_id"));
                    vals.put("round", game.getInt("round"));
                    vals.put("play_turn", game.getInt("play_turn"));
                    vals.put("friends_points", game.getInt("opponents_wins"));
                    vals.put("users_points", game.getInt("user_wins"));
                    if( cursor.getCount() == 1 ){
                        // bunu yapmamin sebebi server dan sira kullanicida olan oyunlarin basta gelmesi
                        // todo burada update yap, datalari play_turn e gore yeniden sirala
                        db.delete( DB.DBT_USER_ACTIVE_GAMES, "game_id = ?", new String[]{ String.format("%s", game.getInt("id"))});

                        // yeni oyunsa ekle db ye
                        //db.insert(DB.DBT_USER_ACTIVE_GAMES, null, vals);
                    }/* else {
                        // zaten ekliyse update
                        db.update(DB.DBT_USER_ACTIVE_GAMES, vals, " friend_id = ?", new String[]{ String.format("%s", game.getInt("opponents_id"))});
                    }*/
                    db.insert(DB.DBT_USER_ACTIVE_GAMES, null, vals);
                    vals.clear();
                    cursor.close();
                }
            } catch( JSONException e ){
                System.out.println("UpdateDBGames json " + e.toString() );
            }
        }
    }
    private void updateDBFriendRequests( JSONArray requests ){
        if( requests.length() > 0 ) {
            try {
                for (int i = 0; i < requests.length(); i++) {
                    ContentValues vals = new ContentValues();
                    Cursor cursor = db.rawQuery("SELECT * FROM " + DB.DBT_USER_FRIEND_REQUESTS + " WHERE friend_id = ?", new String[]{String.format("%s", requests.getJSONObject(i).getInt("friend_id"))});
                    if (cursor.getCount() == 0) {
                        vals.put("friend_id", requests.getJSONObject(i).getInt("friend_id"));
                        vals.put("friend_name", requests.getJSONObject(i).getString("friend_name"));
                        vals.put("friend_avatar", "default");
                        vals.put("request_seen", 0 );
                        db.insert(DB.DBT_USER_FRIEND_REQUESTS, null, vals);
                        vals.clear();
                        cursor.close();
                    }
                }
            } catch (JSONException e) {
                System.out.println("UpdateDBGameRequests json " + e.toString());
            }
        }
    }
    private void updateDBGameRequests( JSONArray requests ){
        if( requests.length() > 0 ) {
            try {
                for (int i = 0; i < requests.length(); i++) {
                    ContentValues vals = new ContentValues();
                    Cursor cursor = db.rawQuery("SELECT * FROM " + DB.DBT_USER_GAME_REQUESTS + " WHERE friend_id = ?", new String[]{String.format("%s", requests.getJSONObject(i).getInt("friend_id"))});
                    if (cursor.getCount() == 0) {
                        vals.put("friend_id", requests.getJSONObject(i).getInt("friend_id"));
                        vals.put("friend_name", requests.getJSONObject(i).getString("friend_name"));
                        vals.put("friend_avatar", "default");
                        vals.put("request_seen", 0 );
                        db.insert(DB.DBT_USER_GAME_REQUESTS, null, vals);
                        vals.clear();
                        cursor.close();
                    }
                }
            } catch (JSONException e) {
                System.out.println("UpdateDBFriendRequests json " + e.toString());
            }
        }
    }
    private void updateDBUserData( int points, int level, int leaderboard, int wins, int loses ){
        ContentValues summaryParams = new ContentValues();
        summaryParams.put("level", level);
        summaryParams.put("points", points);
        summaryParams.put("wins", wins);
        summaryParams.put("loses", loses);
        summaryParams.put("leaderboard", leaderboard );
        /* kullanici istatistiklerini guncelle */
        db.update(DB.DBT_USER_DATA, summaryParams, " user_id = ?", new String[]{String.format("%s", User.ID)});
    }
    private void updateDBGameRequestsSent( JSONArray requests ){
        ContentValues vals = new ContentValues();
        if( requests.length() > 0 ) {
            try {
                for (int i = 0; i < requests.length(); i++) {
                    vals.put("friend_id", requests.getJSONObject(i).getInt("friend_id"));
                    db.insert(DB.DBT_USER_GAME_REQUESTS_SENT, null, vals);
                    vals.clear();
                }
            } catch (JSONException e) {
                System.out.println("updateDBGameRequestsSent json " + e.toString());
            }
        }
    }
    // eger arkadasi kullaniciyi silmisse
    private void updateDBFriends( JSONArray friends ){
        if( friends.length() > 0 ) {
            List<Integer> output_ids = new ArrayList<>();
            List<FriendData> dbFriends = getFriends();
            int friendsLength = dbFriends.size();
            try {
                // server dan gelen guncel arkadaslarin id lerini topla
                for (int i = 0; i < friends.length(); i++) {
                   output_ids.add( friends.getJSONObject(i).getInt("friend_id") );
                }
                // tum arkadaslar icin kullanici arkadasliktan cikarilmis mi diye kontrol et
                for( int i = 0; i < friendsLength; i++ ){
                    // cikarmissa kullanicinin db den sil arkadasi
                    // ( arkadas listesi, aktif oyun varsa sil, chat sil )
                    // eski oyunlari friend id yoksa bos doncek o yuzden bilinmeyen kullanici gibi birsey yaparim
                    if( !output_ids.contains( dbFriends.get(i).getId() ) ){
                        System.out.println( "Silmis adam seni aha bu ibne : " + dbFriends.get(i).getName() );
                        deleteFriend(dbFriends.get(i).getId());
                        deleteGame(dbFriends.get(i).getId());
                    }
                }
            } catch (JSONException e) {
                System.out.println("UpdateDBFriendRequests json " + e.toString());
            }
        }

    }

    public void processFinishJSON(JSONObject output){
        System.out.println(output);
        try {
            Boolean success = output.getBoolean("success");
            if( success ){
                String type = output.getString("type");
                switch( type ){
                    case "do_get_user_data":
                        /*
                            * output:
                            *   games(jsonarray)
                            *   friend_requests(jsonarray)
                            *   game_requests(jsonarray)
                            *   points, level, leaderboard, wins, loses(int)
                        */
                        updateDBUserData(output.getInt("points"), output.getInt("level"), output.getInt("leaderboard"), output.getInt("wins"), output.getInt("loses"));
                        updateDBFriendRequests(output.getJSONArray("friend_requests"));
                        updateDBGameRequests(output.getJSONArray("game_requests"));
                        updateDBGameRequestsSent( output.getJSONArray("game_requests_sent") );
                        updateDBGames( output.getJSONArray("games") );
                        updateDBFriends( output.getJSONArray("friends") );
                        // ActivityMain baslat
                        Intent intent = new Intent( mContext, ActivityMain.class );
                        mContext.startActivity( intent );
                    break;

                    case "do_get_user_active_games":
                        updateDBGames( output.getJSONArray("games") );
                        // game adapter refresh
                        if( mAdapterUpdateFlag ){
                            List<GameData> games = getGames();
                            List<GameAdapterData> adapterData = new ArrayList<>();
                            for( int i = 0; i < games.size(); i++ ){
                                adapterData.add( new GameAdapterData( this, games.get(i) ) );
                            }
                            mGameAdapter.setNewData( adapterData );
                            mGameAdapter.notifyDataSetChanged();
                            PopupLoader.hide();
                        }
                    break;

                    case "do_get_friend_requests":
                        updateDBFriendRequests(output.getJSONArray("friend_requests"));
                        if( mAdapterUpdateFlag ) {
                            mRequestAdapter.setNewData(getFriendRequests());
                            mRequestAdapter.notifyDataSetChanged();
                            PopupLoader.hide();
                        }
                        break;

                    case "do_get_game_requests":
                        updateDBGameRequests(output.getJSONArray("game_requests"));
                        if( mAdapterUpdateFlag ){
                            mRequestAdapter.setNewData( getGameRequests() );
                            mRequestAdapter.notifyDataSetChanged();
                            PopupLoader.hide();
                        }
                    break;
                }
            }
        } catch ( JSONException e ){
            System.out.println("DataBundle json " + e.toString() );
        }
    }

    // register sonrasi login, direk login
    public void insert( JSONObject bundle ){
        try{
            // user stats
            ContentValues vals = new ContentValues();
            vals.put("name", bundle.getString("name"));
            vals.put("user_id", bundle.getInt("id") );
            vals.put("email", bundle.getString("email"));
            vals.put("level", bundle.getInt("level"));
            vals.put("points", bundle.getInt("points"));
            vals.put("leaderboard", bundle.getInt("leaderboard"));
            vals.put("wins", bundle.getInt("wins"));
            vals.put("loses", bundle.getInt("loses"));
            vals.put("avatar", "avatar");
            db.insert(DB.DBT_USER_DATA, null, vals);
            vals.clear();

            // friends
            JSONArray friends = bundle.getJSONArray("friends");
            for( int i = 0; i < friends.length(); i++  ){
                vals.put("friend_id", friends.getJSONObject(i).getInt("friend_id") );
                vals.put("friend_name", friends.getJSONObject(i).getString("friend_name") );
                vals.put("friend_avatar", "avatar" );
                db.insert(DB.DBT_FRIENDS, null, vals);
                vals.clear();
            }

            // friend requests ( received )
            JSONArray friendRequestsReceived = bundle.getJSONArray("friend_requests");
            for( int i = 0; i < friendRequestsReceived.length(); i++  ){
                vals.put("friend_id", friendRequestsReceived.getJSONObject(i).getInt("friend_id") );
                vals.put("friend_name", friendRequestsReceived.getJSONObject(i).getString("friend_name"));
                vals.put("friend_avatar", "default");
                vals.put("request_seen", 0 );
                db.insert(DB.DBT_USER_FRIEND_REQUESTS, null, vals);
                vals.clear();
            }


            // games
            // todo: users_wins olacak key
            JSONArray games = bundle.getJSONArray("games");
            for( int i = 0; i < games.length(); i++  ){
                vals.put("game_id", games.getJSONObject(i).getInt("id"));
                vals.put("friend_id", games.getJSONObject(i).getInt("opponents_id") );
                vals.put("friends_points", games.getJSONObject(i).getString("opponents_wins") );
                vals.put("users_points", games.getJSONObject(i).getString("user_wins") );
                vals.put("round", games.getJSONObject(i).getString("round") );
                vals.put("play_turn", games.getJSONObject(i).getString("play_turn") );
                db.insert(DB.DBT_USER_ACTIVE_GAMES, null, vals);
                vals.clear();
            }

            // finished games
            /*JSONArray finishedGames = bundle.getJSONArray("games");
            for( int i = 0; i < finishedGames.length(); i++  ){
                vals.put("friend_id", finishedGames.getJSONObject(i).getInt("opponents_id") );
                vals.put("friends_points", finishedGames.getJSONObject(i).getString("opponents_wins") );
                vals.put("users_points", finishedGames.getJSONObject(i).getString("users_wins") );
                db.insert(DB.DBT_USER_ACTIVE_GAMES, null, vals);
                vals.clear();
            }*/

            // game requests ( received )
            JSONArray gameRequestsReceived = bundle.getJSONArray("game_requests");
            for( int i = 0; i < gameRequestsReceived.length(); i++  ){
                vals.put("friend_id", gameRequestsReceived.getJSONObject(i).getInt("friend_id") );
                vals.put("friend_name", gameRequestsReceived.getJSONObject(i).getString("friend_name"));
                vals.put("friend_avatar", "default");
                vals.put("request_seen", 0 );
                db.insert(DB.DBT_USER_GAME_REQUESTS, null, vals);
                vals.clear();
            }

            // game requests ( sent )
            JSONArray gameRequestsSent = bundle.getJSONArray("game_requests_sent");
            for( int i = 0; i < gameRequestsSent.length(); i++  ){
                vals.put("friend_id", gameRequestsSent.getJSONObject(i).getInt("friend_id") );
                db.insert(DB.DBT_USER_GAME_REQUESTS_SENT, null, vals);
                vals.clear();
            }

        } catch( JSONException e ){
            System.out.println( "GameDataBundle json " + e.toString() );
        }
    }




}

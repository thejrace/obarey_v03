package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jeppe on 09.04.2016.
 */
public class PopupLoader {

    private static LayoutInflater mInflater;
    private static ViewGroup mBody;

    public static void show( Context ctx ){
        mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = mInflater.inflate(R.layout.popup_loader, null);
        mBody.addView(v, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public static void setBody( ViewGroup body ){
        mBody = body;
    }

    public static void hide(){
        if( mBody.findViewById(R.id.popup_loader_wrapper) != null ) mBody.removeViewAt(1);
    }

}

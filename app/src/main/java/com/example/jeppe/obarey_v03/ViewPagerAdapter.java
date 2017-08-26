package com.example.jeppe.obarey_v03;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 10.04.2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter( FragmentManager manager ){
        super(manager);
    }

    @Override
    public Fragment getItem(int pos){
        return mFragmentList.get(pos);
    }

    @Override
    public int getCount(){
        return mFragmentList.size();
    }

    public void addFragment( Fragment fragment, String title ){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int pos){
        return mFragmentTitleList.get(pos);
    }
}

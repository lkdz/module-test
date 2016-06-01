package com.lkdz.rfwirelessmoduletest1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖慧 on 2016/5/27.
 */
public class FragmentViwPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList=new ArrayList<>();

    public FragmentViwPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

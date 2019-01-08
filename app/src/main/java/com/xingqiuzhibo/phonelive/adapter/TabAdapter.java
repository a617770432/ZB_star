package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.xingqiuzhibo.phonelive.fragment.CircleFragment;
import com.xingqiuzhibo.phonelive.fragment.CommunityFragment;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {

    private List<String> list;
    private List<Fragment> fragments = new ArrayList<>();

    public TabAdapter(FragmentManager fm, List<String> list ) {
        super(fm);
        this.list = list;
        fragments.clear();
        fragments.add(CommunityFragment.newInstance());
        fragments.add(CircleFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return (Fragment)super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}

package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.xingqiuzhibo.phonelive.fragment.BeautifulIdFragment;
import com.xingqiuzhibo.phonelive.fragment.MountFragment;
import com.xingqiuzhibo.phonelive.fragment.VipFragment;

import java.util.ArrayList;
import java.util.List;

public class EquipAdapter extends FragmentPagerAdapter {

    private List<String> list;
    private List<Fragment> fragments = new ArrayList<>();

    public EquipAdapter(FragmentManager fm, List<String> list , int openType ) {
        super(fm);
        this.list = list;
        fragments.clear();
        fragments.add(BeautifulIdFragment.newInstance(openType));
        fragments.add(MountFragment.newInstance(openType));
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

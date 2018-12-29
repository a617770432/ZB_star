package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.xingqiuzhibo.phonelive.fragment.GuardFragment;
import com.xingqiuzhibo.phonelive.fragment.YinPiaoFragment;

import java.util.ArrayList;
import java.util.List;

public class NewViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public NewViewPagerAdapter(FragmentManager fm , String liveUid) {
        super(fm);
        fragments.clear();
        fragments.add(YinPiaoFragment.newInstance(liveUid));
        fragments.add(GuardFragment.newInstance(liveUid));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }


    @Override
    public int getCount() {
        return fragments.size();
    }
}

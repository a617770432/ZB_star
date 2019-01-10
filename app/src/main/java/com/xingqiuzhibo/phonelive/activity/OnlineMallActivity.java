package com.xingqiuzhibo.phonelive.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.OnlineMallAdapter;
import com.xingqiuzhibo.phonelive.adapter.TieZiTabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineMallActivity extends AbsActivity {

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_online_mall;
    }

    @Override
    protected void main() {
        super.main();
        ButterKnife.bind(this);

        List<String> tabList = new ArrayList<>();
        tabList.add("会员");
        tabList.add("靓号");
        tabList.add("坐骑");
        OnlineMallAdapter adapter = new OnlineMallAdapter(getSupportFragmentManager(), tabList , 0);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(tabList.size()-1);
        slidingTab.setViewPager(viewpager);

    }

}

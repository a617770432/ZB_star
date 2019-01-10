package com.xingqiuzhibo.phonelive.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.flyco.tablayout.SlidingTabLayout;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.EquipAdapter;
import com.xingqiuzhibo.phonelive.adapter.OnlineMallAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EquipActivity extends AbsActivity {

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_equip;
    }

    @Override
    protected void main() {
        super.main();
        ButterKnife.bind(this);

        List<String> tabList = new ArrayList<>();
        tabList.add("靓号");
        tabList.add("坐骑");
        EquipAdapter adapter = new EquipAdapter(getSupportFragmentManager(), tabList , 1);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(tabList.size()-1);
        slidingTab.setViewPager(viewpager);

    }
}

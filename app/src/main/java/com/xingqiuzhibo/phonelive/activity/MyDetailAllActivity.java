package com.xingqiuzhibo.phonelive.activity;

import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.MyDetailAllTabAdapter;
import com.xingqiuzhibo.phonelive.adapter.WithDrawCashTabAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hx
 * Time 2019/1/8/008.
 */

public class MyDetailAllActivity extends AbsActivity {
    SlidingTabLayout slidingTab;
    ViewPager viewPager;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_all;
    }

    @Override
    protected void main() {
        setTitle("我的明细");
        slidingTab = findViewById(R.id.sliding_tab);
        viewPager = findViewById(R.id.view_pager);
        initPage();
    }

    private void initPage() {

        List<String> tabList = new ArrayList<>();
        tabList.add("收礼物明细");
        tabList.add("直播时长明细");
        MyDetailAllTabAdapter adapter = new MyDetailAllTabAdapter(getSupportFragmentManager(), tabList);
        viewPager.setAdapter(adapter);
        slidingTab.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(tabList.size());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }
}

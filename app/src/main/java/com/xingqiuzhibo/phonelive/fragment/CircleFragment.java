package com.xingqiuzhibo.phonelive.fragment;

import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.CircleTabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CircleFragment extends BaseFragment {

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public CircleFragment() {
        // Required empty public constructor
    }

    public static CircleFragment newInstance(){
        CircleFragment fragment = new CircleFragment();
        return fragment;
    }

    @Override
    protected void initView() {
        List<String> tabList = new ArrayList<>();
        tabList.add("关注");
        tabList.add("推荐");
        tabList.add("香港");
        tabList.add("新加坡");
        tabList.add("澳大利亚");
        CircleTabAdapter adapter = new CircleTabAdapter(getChildFragmentManager(), tabList);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(tabList.size()-1);
        slidingTab.setViewPager(viewpager);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

}

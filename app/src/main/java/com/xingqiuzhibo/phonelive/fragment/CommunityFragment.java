package com.xingqiuzhibo.phonelive.fragment;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.CertificationStepTwoActivity;
import com.xingqiuzhibo.phonelive.activity.SeeActivity;
import com.xingqiuzhibo.phonelive.adapter.TieZiTabAdapter;
import com.xingqiuzhibo.phonelive.custom.AllViewPager;
import com.xingqiuzhibo.phonelive.custom.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CommunityFragment extends BaseFragment {

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.tv_diamond_count)
    TextView tvDiamondCount;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public CommunityFragment() {
        // Required empty public constructor
    }

    public static CommunityFragment newInstance() {
        CommunityFragment fragment = new CommunityFragment();
        return fragment;
    }

    @Override
    protected void initView() {

        List<String> tabList = new ArrayList<>();
        tabList.add("推荐");
        tabList.add("关注");
        tabList.add("图文");
        tabList.add("视频");
        TieZiTabAdapter adapter = new TieZiTabAdapter(getChildFragmentManager(), tabList);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(tabList.size()-1);
        slidingTab.setViewPager(viewpager);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @OnClick(R.id.tv_see)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), SeeActivity.class);
        startActivity(intent);
    }
}

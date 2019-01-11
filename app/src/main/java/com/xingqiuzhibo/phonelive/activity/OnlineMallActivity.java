package com.xingqiuzhibo.phonelive.activity;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.OnlineMallAdapter;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.sub_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initPager();
    }


    private void initPager() {
        List<String> tabList = new ArrayList<>();
        tabList.add("会员");
        tabList.add("靓号");
        tabList.add("坐骑");
        OnlineMallAdapter adapter = new OnlineMallAdapter(getSupportFragmentManager(), tabList, 0);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(tabList.size() - 1);
        slidingTab.setViewPager(viewpager);
    }

}

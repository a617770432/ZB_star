package com.xingqiuzhibo.phonelive.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.HtmlConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.WithDrawCashTabAdapter;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.utils.IconUtil;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.SpUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/20.
 */

public class MyProfitActivity extends AbsActivity implements View.OnClickListener {
    SlidingTabLayout slidingTab;
    ViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_profit;
    }

    @Override
    protected void main() {
        slidingTab = findViewById(R.id.sliding_tab);
        viewPager = findViewById(R.id.view_pager);
        initPage();
        findViewById(R.id.btn_cash_record).setOnClickListener(this);
    }

    private void initPage() {

        List<String> tabList = new ArrayList<>();
        tabList.add("钻石");
        tabList.add("映票");
        WithDrawCashTabAdapter adapter = new WithDrawCashTabAdapter(getSupportFragmentManager(), tabList);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cash_record:
                cashRecord();
                break;
        }
    }

    /**
     * 提现记录
     */
    private void cashRecord() {
        String url = HtmlConfig.CASH_RECORD;
        WebViewActivity.forward(mContext, url);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpConsts.DO_CASH);
        HttpUtil.cancel(HttpConsts.GET_PROFIT);
        super.onDestroy();
    }
}

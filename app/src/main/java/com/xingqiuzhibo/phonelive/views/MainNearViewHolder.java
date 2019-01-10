package com.xingqiuzhibo.phonelive.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.PublishTieZiActivity;
import com.xingqiuzhibo.phonelive.activity.SearchTieZiActivity;
import com.xingqiuzhibo.phonelive.adapter.TabAdapter;
import com.xingqiuzhibo.phonelive.adapter.TieZiTabAdapter;
import com.xingqiuzhibo.phonelive.adapter.ViewPagerAdapter;
import com.xingqiuzhibo.phonelive.custom.CustomViewPager;
import com.xingqiuzhibo.phonelive.fragment.CircleFragment;
import com.xingqiuzhibo.phonelive.fragment.CommunityFragment;
import com.xingqiuzhibo.phonelive.fragment.NoticeFragment;
import com.xingqiuzhibo.phonelive.fragment.ReportFragment;
import com.xingqiuzhibo.phonelive.interfaces.MainAppBarExpandListener;
import com.xingqiuzhibo.phonelive.utils.StartActivityUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 附近
 */

public class MainNearViewHolder extends AbsMainViewHolder {

    private FragmentManager fm;

    private PopupWindow popupWindow;
    private AppCompatImageView ivSearch;

    public MainNearViewHolder(Context context, ViewGroup parentView, FragmentManager fm) {
        super(context, parentView, fm);
    }

    @Override
    protected void processArguments(Object... args) {
        super.processArguments(args);
        this.fm = (FragmentManager) args[0];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_near;
    }

    @Override
    public void init() {

        ivSearch = (AppCompatImageView) findViewById(R.id.iv_search);

        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        List<String> tabList = new ArrayList<>();
        tabList.add("社区");
        tabList.add("圈子");
        TabAdapter adapter = new TabAdapter(fm, tabList);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);

        initPopupWindow();

        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(ivSearch, 0, 0, Gravity.END);
            }
        });

        findViewById(R.id.iv_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoticeFragment fragment = new NoticeFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                fragment.show(fm, "");
            }
        });

        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivityUtil.start(mContext , SearchTieZiActivity.class);
            }
        });

    }

    private void initPopupWindow() {
        View view = View.inflate(mContext, R.layout.layout_popupwindow, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true); //外部是否可点击 点击外部消失
        popupWindow.setFocusable(true);  //响应返回键  点击返回键 消失

        final Intent intent = new Intent(mContext, PublishTieZiActivity.class);

        view.findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //文章
                intent.putExtra("openType", 0);
                mContext.startActivity(intent);
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.tv_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //图文
                intent.putExtra("openType", 1);
                mContext.startActivity(intent);
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.tv_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //视频
                intent.putExtra("openType", 2);
                mContext.startActivity(intent);
                popupWindow.dismiss();
            }
        });

    }

}

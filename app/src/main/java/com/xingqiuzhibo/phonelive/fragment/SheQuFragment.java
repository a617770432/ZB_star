package com.xingqiuzhibo.phonelive.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.PublishTieZiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SheQuFragment extends BaseFragment {

    @BindView(R.id.tv_shequ)
    TextView tvSheQu;
    @BindView(R.id.view_shequ)
    View viewSheQu;
    @BindView(R.id.rl_shequ)
    RelativeLayout rlSheQu;
    @BindView(R.id.tv_circle)
    TextView tvCircle;
    @BindView(R.id.view_circle)
    View viewCircle;
    @BindView(R.id.rl_circle)
    RelativeLayout rlCircle;
    @BindView(R.id.iv_search)
    AppCompatImageView ivSearch;
    @BindView(R.id.frame)
    FrameLayout frame;

    private PopupWindow popupWindow;

    private CommunityFragment communityFragment;
    private CircleFragment circleFragment;

    public SheQuFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initView() {

        initFragment1();

        initPopupWindow();

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(ivSearch, 0, 0, Gravity.END);
            }
        });

        rlSheQu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFragment1();
                tvSheQu.setTextColor(Color.parseColor("#333333"));
                tvCircle.setTextColor(Color.parseColor("#999999"));
                viewSheQu.setVisibility(View.VISIBLE);
                viewCircle.setVisibility(View.GONE);
            }
        });

        rlCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFragment2();
                tvSheQu.setTextColor(Color.parseColor("#999999"));
                tvCircle.setTextColor(Color.parseColor("#333333"));
                viewSheQu.setVisibility(View.GONE);
                viewCircle.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_she_qu;
    }

    //显示第一个fragment
    private void initFragment1(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(communityFragment == null){
            communityFragment = CommunityFragment.newInstance();
            transaction.add(R.id.frame, communityFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(communityFragment);

        //提交事务
        transaction.commit();
    }

    //显示第一个fragment
    private void initFragment2(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(circleFragment == null){
            circleFragment = CircleFragment.newInstance();
            transaction.add(R.id.frame, circleFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(communityFragment);

        //提交事务
        transaction.commit();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction){
        if(communityFragment != null){
            transaction.hide(communityFragment);
        }
        if(circleFragment != null){
            transaction.hide(circleFragment);
        }
    }

    private void initPopupWindow() {
        View view = View.inflate(getActivity(), R.layout.layout_popupwindow, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true); //外部是否可点击 点击外部消失
        popupWindow.setFocusable(true);  //响应返回键  点击返回键 消失

        final Intent intent = new Intent(getActivity(), PublishTieZiActivity.class);

        view.findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //文章
                intent.putExtra("openType", 0);
                getActivity().startActivity(intent);
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.tv_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //图文
                intent.putExtra("openType", 1);
                getActivity().startActivity(intent);
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.tv_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //视频
                intent.putExtra("openType", 2);
                getActivity().startActivity(intent);
                popupWindow.dismiss();
            }
        });

    }

}

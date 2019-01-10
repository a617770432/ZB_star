package com.xingqiuzhibo.phonelive.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xingqiuzhibo.phonelive.R;

public class BeautifulIdFragment extends BaseFragment {

    private int openType; // 0 为在线商城点进来，  1 为装备中心点进来  两个的布局不同

    public BeautifulIdFragment() {
        // Required empty public constructor
    }

    public static BeautifulIdFragment newInstance(int openType){
        BeautifulIdFragment fragment = new BeautifulIdFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("openType" , openType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            openType = getArguments().getInt("openType");
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_beatiful_id;
    }

}

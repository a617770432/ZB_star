package com.xingqiuzhibo.phonelive.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingqiuzhibo.phonelive.R;

public class MountFragment extends BaseFragment {

    private int openType; // 0 为在线商城点进来，  1 为装备中心点进来  两个的布局不同

    public MountFragment() {
        // Required empty public constructor
    }

    public static MountFragment newInstance(int openType){
        MountFragment fragment = new MountFragment();
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
        return R.layout.fragment_monut;
    }

}

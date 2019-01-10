package com.xingqiuzhibo.phonelive.fragment;


import com.xingqiuzhibo.phonelive.R;

import butterknife.OnClick;

public class VipFragment extends BaseFragment {

    public VipFragment() {
        // Required empty public constructor
    }

    public static VipFragment newInstance() {
        VipFragment fragment = new VipFragment();
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vip;
    }

    @OnClick(R.id.tv_open)
    public void onViewClicked() {
    }
}

package com.xingqiuzhibo.phonelive.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.CircleDetailActivity;
import com.xingqiuzhibo.phonelive.adapter.FocusAdapter;
import com.xingqiuzhibo.phonelive.adapter.FocusGirdAdapter;
import com.xingqiuzhibo.phonelive.bean.FocusBean;
import com.xingqiuzhibo.phonelive.utils.GridSpacingItemDecoration;
import com.xingqiuzhibo.phonelive.utils.StartActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SecondMenuFragment extends BaseFragment implements BaseQuickAdapter.OnItemChildClickListener , BaseQuickAdapter.OnItemClickListener{

    @BindView(R.id.no_focus_recycler_view)
    RecyclerView noFocusRecyclerView;
    @BindView(R.id.ll_no_focus)
    LinearLayout llNoFocus;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private int openPosition;

    private List<FocusBean> list = new ArrayList<>();//数据源
    private FocusGirdAdapter adapter;//网格列表适配器
    private FocusAdapter focusAdapter;//列表视图适配器

    public SecondMenuFragment() {
        // Required empty public constructor
    }

    public static SecondMenuFragment newInstance(int position) {
        SecondMenuFragment fragment = new SecondMenuFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            openPosition = getArguments().getInt("position");
        }
    }

    @Override
    protected void initView() {
        list.clear();

        if(openPosition == 0){

            scroll.setVisibility(View.VISIBLE);

            for (int i = 0; i < 6; i++) {
                list.add(new FocusBean());
            }
            noFocusRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            noFocusRecyclerView.setItemAnimator(new DefaultItemAnimator());
            noFocusRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20, false));
            adapter = new FocusGirdAdapter(R.layout.item_no_focus, list);
            adapter.openLoadAnimation();
            adapter.setOnItemChildClickListener(this);
            noFocusRecyclerView.setAdapter(adapter);
        }else {

            scroll.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);

            for (int i = 0; i < 4; i++) {
                list.add(new FocusBean());
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            focusAdapter = new FocusAdapter(R.layout.item_focus, list);
            focusAdapter.openLoadAnimation();
            focusAdapter.setOnItemChildClickListener(this);
            focusAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(focusAdapter);
        }

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_second_menu;
    }

    @OnClick(R.id.ll_change)
    public void onViewClicked() {
        //换一批
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        //列表关注点击

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //虽然都是跳一样的页面，但是还是区分下吧，万一不一样呢。
        if(adapter == focusAdapter){
            StartActivityUtil.start(getActivity(), CircleDetailActivity.class);
        }else {

        }
    }
}

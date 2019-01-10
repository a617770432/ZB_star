package com.xingqiuzhibo.phonelive.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.CoinHisAdapter;
import com.xingqiuzhibo.phonelive.adapter.TimeDetailAdapter;
import com.xingqiuzhibo.phonelive.bean.CoinHisBean;
import com.xingqiuzhibo.phonelive.bean.TimeDetailBean;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hx
 * Time 2019/1/8/008.
 */

public class MyDetailAllFragment extends Fragment {
    private View view;
    private int mPosition;
    private static final int PAGE_SIZE = 20;
    private int mNextRequestPage = 1;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CoinHisAdapter coinHisAdapter;
    private List<CoinHisBean> data;
    private TimeDetailAdapter detailAdapter;
    private List<TimeDetailBean> timeData;

    public static MyDetailAllFragment newInstance(int position) {
        MyDetailAllFragment fragment = new MyDetailAllFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail_all, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt("position");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mRecyclerView = view.findViewById(R.id.rv_list);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout.setRefreshing(true);
        if (mPosition == 0) {
            data = new ArrayList<>();
        } else {
            timeData = new ArrayList<>();
            view.findViewById(R.id.layout_time).setVisibility(View.VISIBLE);
        }


        initAdapter();
        initRefreshLayout();
        refresh();
    }

    private void initAdapter() {
        if (mPosition == 0) {
            coinHisAdapter = new CoinHisAdapter(R.layout.item_lv_coin_history, data);
            coinHisAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    loadMore();
                }
            });
            coinHisAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
            mRecyclerView.setAdapter(coinHisAdapter);
        } else {
            detailAdapter = new TimeDetailAdapter(R.layout.item_lv_time_detail, timeData);
            detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    loadMore();
                }
            });
            detailAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
            mRecyclerView.setAdapter(detailAdapter);
        }

    }


    private void loadData1() {

        Map<String, Object> map = new HashMap<>();
        map.put("page", String.valueOf(mNextRequestPage));
        map.put("limit", String.valueOf(PAGE_SIZE));
        map.put("uid", AppConfig.getInstance().getUid());
        if (mPosition == 0) {
            NetWork.httpParamGet(UrlUtil.GET_GIFT_DETAIL, map, getContext(), request);
        } else {
            NetWork.httpParamGet(UrlUtil.GET_TIME_DETAIL_MSG, map, getContext(), request);
        }


    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        mNextRequestPage = 1;
        if (mPosition == 0) {
            coinHisAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        } else {
            detailAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        loadData1();
    }

    private void loadMore() {
        loadData1();
    }

    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            int code = jsb.getInteger("code");
            if (code == 0) {
                JSONObject pageJson = jsb.getJSONObject("page");
                Integer totalPage = pageJson.getInteger("totalPage");
                JSONArray arr = pageJson.getJSONArray("list");

                mSwipeRefreshLayout.setRefreshing(false);
                boolean isRefresh = mNextRequestPage == 1;

                if (mPosition == 0) {
                    data = JSON.parseArray(JSON.toJSONString(arr), CoinHisBean.class);
                    coinHisAdapter.setEnableLoadMore(true);
                    setData(isRefresh, data);
                } else {
                    timeData = JSON.parseArray(JSON.toJSONString(arr), TimeDetailBean.class);
                    detailAdapter.setEnableLoadMore(true);
                    setData(isRefresh, timeData);
                }


            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private void setData(boolean isRefresh, List data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (mPosition == 0) {
            if (isRefresh) {
                coinHisAdapter.setNewData(data);
            } else {
                if (size > 0) {
                    coinHisAdapter.addData(data);
                }
            }
            if (size < PAGE_SIZE) {
                //第一页如果不够一页就不显示没有更多数据布局
                coinHisAdapter.loadMoreEnd(isRefresh);
//            Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show();
            } else {
                coinHisAdapter.loadMoreComplete();
            }
        } else {
            if (isRefresh) {
                detailAdapter.setNewData(data);
            } else {
                if (size > 0) {
                    detailAdapter.addData(data);
                }
            }
            if (size < PAGE_SIZE) {
                //第一页如果不够一页就不显示没有更多数据布局
                detailAdapter.loadMoreEnd(isRefresh);
//            Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show();
            } else {
                detailAdapter.loadMoreComplete();
            }
        }


    }
}

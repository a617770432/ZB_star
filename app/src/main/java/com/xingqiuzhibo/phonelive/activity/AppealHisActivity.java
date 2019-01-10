package com.xingqiuzhibo.phonelive.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.AppealHisAdapter;
import com.xingqiuzhibo.phonelive.bean.AppealHisBean;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hx
 * Time 2019/1/10/010.
 * 订单申诉历史
 */

public class AppealHisActivity extends AbsActivity {

    private static final int PAGE_SIZE = 20;
    private int mNextRequestPage = 1;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AppealHisAdapter mAdapter;
    private List<AppealHisBean> data = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appeal_hitstory;
    }

    @Override
    protected void main() {
        setTitle("历史记录");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.setRefreshing(true);
        initAdapter();
        initRefreshLayout();
        refresh();
    }

    private void initAdapter() {
        mAdapter = new AppealHisAdapter(R.layout.item_lv_appeal_history, data);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setAdapter(mAdapter);
    }


    private void loadData() {

        Map<String, Object> map = new HashMap<>();
        map.put("currpage", String.valueOf(mNextRequestPage));
        map.put("limit", String.valueOf(PAGE_SIZE));
        map.put("uid", AppConfig.getInstance().getUid());
        NetWork.httpParamGet(UrlUtil.APPEAL_HISTORY_LIST, map, this, request);

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
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        loadData();
    }

    private void loadMore() {
        loadData();
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
                data = JSON.parseArray(JSON.toJSONString(arr), AppealHisBean.class);

                mAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
                boolean isRefresh = mNextRequestPage == 1;
                setData(isRefresh, data);
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
            mAdapter.setEnableLoadMore(true);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private void setData(boolean isRefresh, List data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh);
//            Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }
}

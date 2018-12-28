package com.xingqiuzhibo.phonelive.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.MainHomeTestAdapter;
import com.xingqiuzhibo.phonelive.adapter.RefreshAdapter;
import com.xingqiuzhibo.phonelive.custom.RefreshView;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpUtil;

import java.util.List;

/**
 * Created by cxf on 2018/11/7.
 */

public class TestViewHolder extends AbsViewHolder {

    private RefreshView mRefreshView;

    public TestViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_test_vh;
    }

    @Override
    public void init() {
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new RefreshView.DataHelper<String>() {
            @Override
            public RefreshAdapter<String> getAdapter() {
                return  null;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                //HttpUtil.getLiveRecord(mToUid, p, callback);
            }

            @Override
            public List<String> processData(String[] info) {
                return null;
            }

            @Override
            public void onRefresh(List<String> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {

            }
        });

    }
}

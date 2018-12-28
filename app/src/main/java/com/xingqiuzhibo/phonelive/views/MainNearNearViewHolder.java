package com.xingqiuzhibo.phonelive.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.MainNearNearAdapter;
import com.xingqiuzhibo.phonelive.adapter.RefreshAdapter;
import com.xingqiuzhibo.phonelive.bean.LiveBean;
import com.xingqiuzhibo.phonelive.custom.ItemDecoration;
import com.xingqiuzhibo.phonelive.custom.RefreshView;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.interfaces.LifeCycleAdapter;
import com.xingqiuzhibo.phonelive.interfaces.OnItemClickListener;
import com.xingqiuzhibo.phonelive.utils.L;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 附近 附近
 */

public class MainNearNearViewHolder extends AbsMainChildTopViewHolder implements OnItemClickListener<LiveBean> {

    private MainNearNearAdapter mAdapter;


    public MainNearNearViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home_follow;
    }

    @Override
    public void init() {
        super.init();
        mRefreshView = (RefreshView) findViewById(R.id.refreshView);
        mRefreshView.setNoDataLayoutId(R.layout.view_no_data_default);
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 5);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<LiveBean>() {
            @Override
            public RefreshAdapter<LiveBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new MainNearNearAdapter(mContext);
                    mAdapter.setOnItemClickListener(MainNearNearViewHolder.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getNear(p, callback);
            }

            @Override
            public List<LiveBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), LiveBean.class);
            }

            @Override
            public void onRefresh(List<LiveBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {
//                if (dataCount < HttpConsts.ITEM_COUNT) {
//                    mRefreshView.setLoadMoreEnable(false);
//                } else {
//                    mRefreshView.setLoadMoreEnable(true);
//                }
            }
        });
        mLifeCycleListener = new LifeCycleAdapter() {

            @Override
            public void onDestroy() {
                L.e("main----MainNearNearViewHolder-------LifeCycle---->onDestroy");
                HttpUtil.cancel(HttpConsts.GET_NEAR);
            }
        };
    }

    @Override
    public void loadData() {
        if (!isFirstLoadData()) {
            return;
        }
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    @Override
    public void onItemClick(LiveBean bean, int position) {
        watchLive(bean);
    }
}

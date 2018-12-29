package com.xingqiuzhibo.phonelive.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.GuardAdapter;
import com.xingqiuzhibo.phonelive.adapter.RefreshAdapter;
import com.xingqiuzhibo.phonelive.bean.GuardUserBean;
import com.xingqiuzhibo.phonelive.custom.RefreshView;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * ..守护榜
 */
public class GuardFragment extends Fragment {

    private RefreshView mRefreshView;
    private GuardAdapter mGuardAdapter;
    private String mToUid;

    public GuardFragment() {
        // Required empty public constructor
    }

    public static GuardFragment newInstance(String liveUid){
        GuardFragment fragment = new GuardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("liveUid" , liveUid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mToUid = getArguments().getString("liveUid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guard, container, false);

        mRefreshView = (RefreshView) view.findViewById(R.id.refreshView);
        boolean self = AppConfig.getInstance().getUid().equals(mToUid);
        mRefreshView.setNoDataLayoutId(self ? R.layout.view_no_data_guard_anc_2 : R.layout.view_no_data_guard_aud_2);
        mRefreshView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new RefreshView.DataHelper<GuardUserBean>() {
            @Override
            public RefreshAdapter<GuardUserBean> getAdapter() {
                if (mGuardAdapter == null) {
                    mGuardAdapter = new GuardAdapter(getActivity(), false);
                }
                return mGuardAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                HttpUtil.getGuardList(mToUid, p, callback);
            }

            @Override
            public List<GuardUserBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), GuardUserBean.class);
            }

            @Override
            public void onRefresh(List<GuardUserBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {

            }
        });
        mRefreshView.initData();

        return view;
    }

    @Override
    public void onDestroyView() {
        HttpUtil.cancel(HttpConsts.GET_GUARD_LIST);
        super.onDestroyView();
    }
}

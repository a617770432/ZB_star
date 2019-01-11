package com.xingqiuzhibo.phonelive.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.BuyCarAdapter;
import com.xingqiuzhibo.phonelive.adapter.NiceAccountAdapter;
import com.xingqiuzhibo.phonelive.bean.OnLineMallBean;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MountFragment extends BaseFragment implements BuyCarAdapter.OnBuyCarListener {
    private RecyclerView mRecyclerView;
    private OnLineMallBean bean;
    private int openType; // 0 为在线商城点进来，  1 为装备中心点进来  两个的布局不同
    private BuyCarAdapter mAdapter;
    private List<OnLineMallBean.CarInfo> data = new ArrayList<>();

    public MountFragment() {
        // Required empty public constructor
    }

    public static MountFragment newInstance(int openType) {
        MountFragment fragment = new MountFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("openType", openType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            openType = getArguments().getInt("openType");
        }
    }

    @Override
    protected void initView() {
        mRecyclerView = mRootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mAdapter = new BuyCarAdapter(R.layout.item_mount, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.setOnBuyCarListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        if (openType == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("g", "Appapi");
            map.put("m", "Mall");
            map.put("a", "mall_index");
            map.put("uid", AppConfig.getInstance().getUid());
            map.put("token", AppConfig.getInstance().getToken());
            NetWork.httpParamGet(UrlUtil.BASE_PHP_URL, map, getContext(), mallRequest);
        }
    }

    RequestCallBack<String> mallRequest = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                JSONObject dataJson = jsb.getJSONObject("info");
                bean = JSON.parseObject(dataJson.toJSONString(), OnLineMallBean.class);
                data.clear();
                data = bean.getCar_list();
                mAdapter.setNewData(data);
            } else {
                ToastUtil.show(jsb.getString("msg"));
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_monut;
    }

    @Override
    public void onBuyCarClick(String msg, final String accountID) {
        //购买坐骑按钮点击事件
        DialogUitl.showSimpleDialog(getContext(), msg, true, new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                Map<String, Object> map = new HashMap<>();
                map.put("g", "Appapi");
                map.put("m", "Mall");
                map.put("a", "buycar");
                map.put("uid", AppConfig.getInstance().getUid());
                map.put("token", AppConfig.getInstance().getToken());
                map.put("carid", accountID);
                NetWork.httpParamGet(UrlUtil.BASE_PHP_URL, map, getContext(), request);
            }
        });
    }

    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            Log.e("66666", responseInfo.result);
            if (JSON.parseObject(responseInfo.result).getInteger("code") == 0) {
                ToastUtil.show("购买成功！");
            } else {
                ToastUtil.show(JSON.parseObject(responseInfo.result).getString("msg"));
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Log.e("66666", s);
        }
    };
}

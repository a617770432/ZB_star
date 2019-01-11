package com.xingqiuzhibo.phonelive.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.OnlineMallActivity;
import com.xingqiuzhibo.phonelive.bean.OnLineMallBean;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import java.util.HashMap;
import java.util.Map;


public class VipFragment extends BaseFragment implements BuyVIPBottomFragment.OnBuyVIPListener {
    private String mallMsg;
    private OnLineMallBean bean;
    private TextView buyVIP, timeLeft;
    private BuyVIPBottomFragment fragment;

    public VipFragment() {
        // Required empty public constructor
    }

    public static VipFragment newInstance() {
        VipFragment fragment = new VipFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("mall_msg", msg);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mallMsg = getArguments().getString("mall_msg");
        }
    }

    @Override
    protected void initView() {
        buyVIP = mRootView.findViewById(R.id.tv_open);
        timeLeft = mRootView.findViewById(R.id.tv_vip_left_time);

        fragment = new BuyVIPBottomFragment();
        buyVIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mallMsg)
                    return;
                Bundle bundle = new Bundle();
                bundle.putString("mall_msg", mallMsg);
                fragment.setArguments(bundle);
                fragment.show(getFragmentManager(), "");
            }
        });
        fragment.setOnBuyVIPListener(this);

    }

    public void loadMall() {
        Map<String, Object> map = new HashMap<>();
        map.put("g", "Appapi");
        map.put("m", "Mall");
        map.put("a", "mall_index");
        map.put("uid", AppConfig.getInstance().getUid());
        map.put("token", AppConfig.getInstance().getToken());
        NetWork.httpParamGet(UrlUtil.BASE_PHP_URL, map, getContext(), mallRequest);
    }


    @Override
    protected void loadData() {

        loadMall();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vip;
    }


    /**
     * 购买按钮点击事件
     *
     * @param mPosition
     */
    @Override
    public void onBuyVIPClick(final int mPosition) {
        String coin = bean.getVip_list().get(mPosition).getCoin();
        String status;
        //是否vip 0：否 1：是
        if (bean.getUser().getUser_vip_status() == 0) {
            status = "开通";
        } else {
            status = "续费";
        }
        String time = bean.getVip_list().get(mPosition).getLength_text();

        String msg = "您将花费" + coin + "钻石，" + status + time + "VIP会员";


        DialogUitl.showSimpleDialog(getContext(), msg, true, new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                Map<String, Object> map = new HashMap<>();
                map.put("g", "Appapi");
                map.put("m", "Mall");
                map.put("a", "buyvip");
                map.put("uid", AppConfig.getInstance().getUid());
                map.put("token", AppConfig.getInstance().getToken());
                map.put("vipid", bean.getVip_list().get(mPosition).getId());
                NetWork.httpParamGet(UrlUtil.BASE_PHP_URL, map, getContext(), request);
            }
        });
    }

    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            if (JSON.parseObject(responseInfo.result).getInteger("code") == 0) {
                ToastUtil.show("购买成功！");
                loadMall();
                if (null != fragment)
                    fragment.dismiss();
            } else {
                ToastUtil.show(JSON.parseObject(responseInfo.result).getString("msg"));
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    RequestCallBack<String> mallRequest = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                JSONObject dataJson = jsb.getJSONObject("info");
                mallMsg = dataJson.toJSONString();
                bean = JSON.parseObject(mallMsg, OnLineMallBean.class);

                //是否vip 0：否 1：是
                if (bean.getUser().getUser_vip_status() == 0) {
                    buyVIP.setText("开通会员");
                } else {
                    buyVIP.setText("续费会员");
                    timeLeft.setText("当前VIP到期时间：" + bean.getUser().getUser_vip_endtime());
                }

            } else {
                ToastUtil.show(jsb.getString("msg"));
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
}

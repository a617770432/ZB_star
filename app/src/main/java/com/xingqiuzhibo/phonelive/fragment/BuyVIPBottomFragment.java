package com.xingqiuzhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.BuyVipAdapter;
import com.xingqiuzhibo.phonelive.bean.OnLineMallBean;
import com.xingqiuzhibo.phonelive.views.DangerousOperationDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hx
 * Time 2019/1/10/010.
 */

public class BuyVIPBottomFragment extends BottomSheetDialogFragment {
    private TextView tvNick, tvCost, tvNum, tvBuy, tvGridMsg;
    private GridView gvMsg;
    private String mallMsg;
    private OnLineMallBean bean;
    private BuyVipAdapter adapter;
    private int mPosition = 0;
    private List<OnLineMallBean.VIPList> data = new ArrayList<>();


    public BuyVIPBottomFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mallMsg = getArguments().getString("mall_msg");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_vip, container, false);
        tvNick = view.findViewById(R.id.tv_account_nick);
        gvMsg = view.findViewById(R.id.gv_vip_msg);
        tvCost = view.findViewById(R.id.tv_cost);
        tvNum = view.findViewById(R.id.tv_diamond_num);
        tvBuy = view.findViewById(R.id.tv_buy_vip_now);
        tvGridMsg = view.findViewById(R.id.tv_grid_msg);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData() {
        bean = JSON.parseObject(mallMsg, OnLineMallBean.class);

        tvNick.setText(bean.getUser().getUser_nicename() + "(" + bean.getUser().getId() + ")");
        //是否vip 0：否 1：是
        if (bean.getUser().getUser_vip_status() == 0) {
            tvGridMsg.setText("购买时长");
            tvBuy.setText("立即购买");
        } else {
            tvGridMsg.setText("续费时长");
            tvBuy.setText("立即续费");
        }
        tvNum.setText(bean.getUser().getCoin());
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBuyVIPClick(mPosition);
            }
        });
        data = bean.getVip_list();
        data.get(0).setCheck(true);
        tvCost.setText(data.get(0).getCoin());
        adapter = new BuyVipAdapter(getContext(), data);
        gvMsg.setAdapter(adapter);
        gvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setCheck(false);
                }
                data.get(mPosition).setCheck(true);
                tvCost.setText(data.get(position).getCoin());
                adapter.notifyDataSetChanged();
            }
        });
    }


    public interface OnBuyVIPListener {
        void onBuyVIPClick(int positon);
    }

    private BuyVIPBottomFragment.OnBuyVIPListener listener;

    public void setOnBuyVIPListener(OnBuyVIPListener listener) {
        this.listener = listener;
    }
}

package com.xingqiuzhibo.phonelive.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextView orderNum;
    private TextView orderTime;
    private TextView orderCount;
    private TextView orderMoney;
    private TextView btn;
    private RelativeLayout rlBank, rlWechat, rlAlipay;
    private AppCompatImageView ivBank, ivWechat, ivAlipay;

    private String mOrderNum, mTime, mCoin;
    private float mMoney;
    private String typeAlipay, typeWechat, typeBank, mAccount;

    //1表示支付宝，2表示微信，3表示银行卡'
    private int payType = 3;

    public PayBottomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOrderNum = getArguments().getString("order_num");
            mMoney = getArguments().getFloat("pay_money");
            typeAlipay = getArguments().getString("type_alipay");
            typeWechat = getArguments().getString("type_wechat");
            typeBank = getArguments().getString("type_bank");
            mTime = getArguments().getString("order_time");
            mCoin = getArguments().getString("order_coin");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题栏
        View view = inflater.inflate(R.layout.fragment_pay_bottom, container, false);

        orderNum = view.findViewById(R.id.tv_order_num);
        orderTime = view.findViewById(R.id.tv_order_time);
        orderCount = view.findViewById(R.id.tv_order_count);
        orderMoney = view.findViewById(R.id.tv_order_money);
        btn = view.findViewById(R.id.btn);
        rlBank = view.findViewById(R.id.rl_bank);
        rlWechat = view.findViewById(R.id.rl_wechat);
        rlAlipay = view.findViewById(R.id.rl_alipay);
        ivBank = view.findViewById(R.id.iv_bank);
        ivWechat = view.findViewById(R.id.iv_wechat);
        ivAlipay = view.findViewById(R.id.iv_alipay);
        rlBank.setOnClickListener(this);
        rlAlipay.setOnClickListener(this);
        rlWechat.setOnClickListener(this);

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPayTypeClick(payType, mAccount);
                dismiss();
            }
        });

        setMsg();
        return view;
    }

    private void setMsg() {
        orderNum.setText("订单号:" + mOrderNum);
        orderTime.setText("订单时间：" + mTime);
        orderMoney.setText("订单金额：" + mMoney);
        orderCount.setText("数量：" + mCoin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_bank:
                ivBank.setImageResource(R.mipmap.icon_radio);
                ivWechat.setImageResource(R.mipmap.icon_radio01);
                ivAlipay.setImageResource(R.mipmap.icon_radio01);
                payType = 3;
                mAccount = typeBank;
                break;
            case R.id.rl_alipay:
                ivAlipay.setImageResource(R.mipmap.icon_radio);
                ivWechat.setImageResource(R.mipmap.icon_radio01);
                ivBank.setImageResource(R.mipmap.icon_radio01);
                payType = 1;
                mAccount = typeAlipay;
                break;
            case R.id.rl_wechat:
                ivWechat.setImageResource(R.mipmap.icon_radio);
                ivBank.setImageResource(R.mipmap.icon_radio01);
                ivAlipay.setImageResource(R.mipmap.icon_radio01);
                payType = 2;
                mAccount = typeWechat;
                break;
        }
    }

    public interface OnItemClickListener {
        void onPayTypeClick(int type, String account);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

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
public class PayBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener{

    private TextView orderNum;
    private TextView orderTime;
    private TextView orderCount;
    private TextView orderMoney;
    private TextView btn;
    private RelativeLayout rlBank , rlWechat , rlAlipay;
    private AppCompatImageView ivBank , ivWechat , ivAlipay;

    private int payType = 1;//支付类型  1 银行卡  2 wechat 3 alipay

    public PayBottomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

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
                listener.onPayTypeClick(payType);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_bank:
                ivBank.setImageResource(R.mipmap.icon_radio);
                ivWechat.setImageResource(R.mipmap.icon_radio01);
                ivAlipay.setImageResource(R.mipmap.icon_radio01);
                payType = 1;
                break;
            case R.id.rl_alipay:
                ivAlipay.setImageResource(R.mipmap.icon_radio);
                ivWechat.setImageResource(R.mipmap.icon_radio01);
                ivBank.setImageResource(R.mipmap.icon_radio01);
                payType = 3;
                break;
            case R.id.rl_wechat:
                ivWechat.setImageResource(R.mipmap.icon_radio);
                ivBank.setImageResource(R.mipmap.icon_radio01);
                ivAlipay.setImageResource(R.mipmap.icon_radio01);
                payType = 2;
                break;
        }
    }

    public interface OnItemClickListener {
        void onPayTypeClick(int type);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

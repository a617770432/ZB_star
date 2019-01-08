package com.xingqiuzhibo.phonelive.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;

public class QRCodeFragment extends DialogFragment {
    private int payType;
    private float payNum;
    private String qrCode;

    public QRCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        if (getArguments() != null) {
            payType = getArguments().getInt("pay_type");
            payNum = getArguments().getFloat("pay_money");
            qrCode = getArguments().getString("qr_code");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题栏
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        LinearLayout layout = view.findViewById(R.id.layout_qr_bg);
        ImageView imgLogo = view.findViewById(R.id.iv_img);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvStep2 = view.findViewById(R.id.tv_step2);
        TextView tvMoney = view.findViewById(R.id.tv_money);
        AppCompatImageView imgCode = view.findViewById(R.id.iv_code);

        //1表示支付宝，2表示微信，3表示银行卡'
        switch (payType) {
            case 1:
                layout.setBackgroundResource(R.drawable.round_bule_5);
                imgLogo.setImageResource(R.mipmap.icon_alipay);
                tvTitle.setText("支付宝");
                tvStep2.setText("步骤二： 支付宝扫一扫，点击相册");
                break;
            case 2:
                layout.setBackgroundResource(R.drawable.round_green_bottom_5);
                imgLogo.setImageResource(R.mipmap.icon_wechat);
                tvTitle.setText("微信");
                tvStep2.setText("步骤二： 微信扫一扫，点击相册");
                break;
        }

        //设置收款金额
        tvMoney.setText(payNum + "元");

        //设置收款码
        ImgLoader.display(qrCode, imgCode);

        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }


}

package com.xingqiuzhibo.phonelive.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.OrderPayAdapter;
import com.xingqiuzhibo.phonelive.bean.OrderPayBean;
import com.xingqiuzhibo.phonelive.fragment.PayBottomFragment;
import com.xingqiuzhibo.phonelive.fragment.QRCodeFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AbsActivity implements OrderPayAdapter.OnItemClickListener , PayBottomFragment.OnItemClickListener{

    private String orderId;

    private TextView orderNum;
    private TextView orderState;
    private TextView orderTime;
    private TextView orderCount;
    private TextView orderMoney;
    private TextView remindTime;
    private RecyclerView recyclerView;
    private TextView btn;

    //数据源
    private List<OrderPayBean> list = new ArrayList<>();
    private OrderPayAdapter adapter;

    private PayBottomFragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void main() {

        setTitle("订单详情");

        orderNum = findViewById(R.id.tv_order_num);
        orderState = findViewById(R.id.tv_order_state);
        orderTime = findViewById(R.id.tv_order_time);
        orderCount = findViewById(R.id.tv_order_count);
        orderMoney = findViewById(R.id.tv_order_money);
        remindTime = findViewById(R.id.tv_remind_time);
        recyclerView = findViewById(R.id.recycler_view);
        btn = findViewById(R.id.btn);

        orderId = getIntent().getStringExtra("orderid");

        list.add(new OrderPayBean());
        list.add(new OrderPayBean());
        list.add(new OrderPayBean());
        adapter = new OrderPayAdapter(this , list);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fragment = new PayBottomFragment();
        fragment.setOnItemClickListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager() , "");
            }
        });
    }


    @Override
    public void onCodeClick(int position) {
        //查看收款码点击事件
        QRCodeFragment fragment = new QRCodeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager() , "");
    }

    @Override
    public void onCopyClick(int position) {
        //复制点击事件
        //复制链接
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager mClipboardManager =(ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        ClipData mClipData =ClipData.newPlainText("label", "");         //‘Label’这是任意文字标签
        mClipboardManager.setPrimaryClip(mClipData);
        Toast.makeText(mContext, "已成功复制到剪切板", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayTypeClick(int type) {
        //点击支付回调
        //支付类型  1 银行卡  2 wechat 3 alipay
    }
}

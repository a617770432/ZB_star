package com.xingqiuzhibo.phonelive.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.OrderPayAdapter;
import com.xingqiuzhibo.phonelive.bean.OrderMsgBean;
import com.xingqiuzhibo.phonelive.fragment.PayBottomFragment;
import com.xingqiuzhibo.phonelive.fragment.QRCodeFragment;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单详情（申诉）界面
 */
public class OrderDetailActivity extends AbsActivity implements OrderPayAdapter.OnItemClickListener, PayBottomFragment.OnItemClickListener {

    private String orderId;
    private TextView orderNum;
    private TextView orderState;
    private TextView orderTime;
    private TextView orderCount;
    private TextView orderMoney;
    private TextView remindTime;
    private RecyclerView recyclerView;
    private TextView btn;
    private OrderMsgBean.OrderInfo myOrder;

    //数据源
    private List<OrderMsgBean.PayStyle> list = new ArrayList<>();
    private OrderPayAdapter adapter;

    private PayBottomFragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void main() {
        acts.add(this);
        setTitle("订单详情");

        orderNum = findViewById(R.id.tv_order_num);
        orderState = findViewById(R.id.tv_order_state);
        orderTime = findViewById(R.id.tv_order_time);
        orderCount = findViewById(R.id.tv_order_count);
        orderMoney = findViewById(R.id.tv_order_money);
        remindTime = findViewById(R.id.tv_remind_time);
        recyclerView = findViewById(R.id.recycler_view);
        btn = findViewById(R.id.btn);

        orderId = getIntent().getStringExtra("order_id");

        fragment = new PayBottomFragment();
        fragment.setOnItemClickListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myOrder.getStatus() == 0) {
                    Bundle bundle = new Bundle();
                    //1表示支付宝，2表示微信，3表示银行卡'
                    String numAlipay = null, numWechat = null, numBank = null;
                    for (int i = 0; i < list.size(); i++) {
                        switch (list.get(i).getType()) {
                            case 1:
                                numAlipay = list.get(i).getAccount();
                                break;
                            case 2:
                                numWechat = list.get(i).getAccount();
                                break;
                            case 3:
                                numBank = list.get(i).getAccount();
                                break;
                        }
                    }
                    bundle.putString("order_num", myOrder.getOrderno());
                    bundle.putFloat("pay_money", myOrder.getMoney());
                    bundle.putString("order_time", myOrder.getAddtime() + "");
                    if (null != numAlipay)
                        bundle.putString("type_alipay", numAlipay);
                    if (null != numWechat)
                        bundle.putString("type_wechat", numWechat);
                    if (null != numBank)
                        bundle.putString("type_bank", numBank);
                    bundle.putString("order_coin", myOrder.getCoin() + "");
                    fragment.setArguments(bundle);
                    fragment.show(getSupportFragmentManager(), "");
                } else if (myOrder.getStatus() == 1) {
                    Intent intent = new Intent(OrderDetailActivity.this, AppealContentActivity.class);
                    intent.putExtra("order_id", myOrder.getId());
                    startActivity(intent);
                }

            }
        });
        //加载数据
        loadData(orderId);
    }

    private void loadData(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", orderId);
        NetWork.httpParamGet(UrlUtil.GET_ORDER_MSG, map, this, request);
    }


    @Override
    public void onCodeClick(int position) {
        //查看收款码点击事件
        QRCodeFragment fragment = new QRCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pay_type", list.get(position).getType());
        bundle.putFloat("pay_money", myOrder.getMoney());
        bundle.putString("qr_code", list.get(position).getQrcode());
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "code");
    }

    @Override
    public void onCopyClick(int position) {
        //复制点击事件
        //复制链接
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager mClipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        ClipData mClipData = ClipData.newPlainText("label", list.get(position).getAccount());         //‘Label’这是任意文字标签
        mClipboardManager.setPrimaryClip(mClipData);
        Toast.makeText(mContext, "已成功复制到剪切板", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayTypeClick(int type, String account) {
        //点击支付回调
        //1表示支付宝，2表示微信，3表示银行卡'
        Map<String, Object> map = new HashMap<>();
        map.put("orderno", myOrder.getOrderno());
        if (null == account) {
            ToastUtil.show("当前账号未启用！");
            return;
        }
        map.put("account", account);
        map.put("type", String.valueOf(type));
        map.put("paymode", "1");
        map.put("uid", AppConfig.getInstance().getUid());
        NetWork.httpPost(UrlUtil.CONFIRM_CHARGE_ORDER, map, this, confirmRequest);
    }

    private RequestCallBack<String> confirmRequest = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                ToastUtil.show("您的订单提交成功！");
                finish();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    /**
     * 信息查询返回数据
     */
    private RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                JSONObject data = jsb.getJSONObject("page");
                OrderMsgBean bean = JSON.parseObject(JSON.toJSONString(data), OrderMsgBean.class);
//                        JSON.toJavaObject(data, OrderMsgBean.class);

                //设置订单信息
                myOrder = bean.getOrderInfo();
                setOrderMsg(myOrder);
                //设置支付方式列表
                list = bean.getPayStyle();
                adapter = new OrderPayAdapter(OrderDetailActivity.this, list);
                adapter.setOnItemClickListener(OrderDetailActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
                recyclerView.setAdapter(adapter);

            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private void setOrderMsg(OrderMsgBean.OrderInfo bean) {
        //订单编号
        orderNum.setText("订单号：" + bean.getOrderno());
        //支付状态
        switch (bean.getStatus()) {
            case 0:
                orderState.setText("未付款");
                btn.setText("我已付款");
                break;
            case 1:
                orderState.setText("已付款");
                btn.setText("订单申诉");
                break;
            case 2:
                orderState.setText("已完成");
                btn.setVisibility(View.GONE);
                break;
        }
        //订单时间
        orderTime.setText("订单时间：" + bean.getAddtime());
        //钻石充值数量
        orderCount.setText("数量：" + bean.getCoin());
        //订单金额
        orderMoney.setText("订单金额：¥ " + bean.getMoney());
    }

}

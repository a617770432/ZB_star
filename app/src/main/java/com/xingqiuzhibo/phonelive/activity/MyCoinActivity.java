package com.xingqiuzhibo.phonelive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingqiuzhibo.phonelive.utils.L;
import com.umeng.commonsdk.debug.W;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.CoinAdapter;
import com.xingqiuzhibo.phonelive.bean.CoinBean;
import com.xingqiuzhibo.phonelive.bean.ConfigBean;
import com.xingqiuzhibo.phonelive.bean.UserBean;
import com.xingqiuzhibo.phonelive.custom.RefreshLayout;
import com.xingqiuzhibo.phonelive.event.CoinChangeEvent;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.interfaces.OnItemClickListener;
import com.xingqiuzhibo.phonelive.pay.PayCallback;
import com.xingqiuzhibo.phonelive.pay.ali.AliPayBuilder;
import com.xingqiuzhibo.phonelive.pay.wx.WxPayBuilder;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.DpUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/23.
 * 我的钻石
 */

public class MyCoinActivity extends AbsActivity implements OnItemClickListener<CoinBean>, View.OnClickListener, RefreshLayout.OnRefreshListener {

    private View mTop;
    private TextView mBalance;
    private long mBalanceValue;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CoinAdapter mAdapter;
    private String mCoinName;
    private CoinBean mCheckedCoinBean;
    private boolean mAliPayEnable;//支付宝是否开启
    private String mAliPartner;// 支付宝商户ID
    private String mAliSellerId; // 支付宝商户收款账号
    private String mAliPrivateKey; // 支付宝商户私钥，pkcs8格式
    private boolean mWxPayEnable;//微信支付是否开启
    private boolean mOffLineEnable;//线下支付开关
    private String mWxAppID;//微信AppID
    private boolean mFirstLoad = true;
    private int mPaymode;//线上线下开关
    private int mCurOrderP = 1;
    private int mAppealOrderP = 1;
    private int mStatus;//0充值 1当前订单 2申诉订单

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coin;
    }

    @Override
    protected void main() {
        mCoinName = AppConfig.getInstance().getCoinName();
        setTitle(WordUtil.getString(R.string.main_me) + mCoinName);
        mTop = findViewById(R.id.top);
        mBalance = (TextView) findViewById(R.id.balance);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshLayout.setScorllView(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setLoadMoreEnable(false);
        mAdapter = new CoinAdapter(mContext,  mCoinName);
        //mAdapter.setContactView(mTop);
        mAdapter.setOnItemClickListener(this);
        findViewById(R.id.btn_coin).setOnClickListener(this);
        findViewById(R.id.btn_cur_order).setOnClickListener(this);
        findViewById(R.id.btn_appeal_order).setOnClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirstLoad) {
            mFirstLoad = false;
            loadData();
        } else {
            checkPayResult();
        }
        if (mStatus!=0){
            loadOrderData();
        }
    }

    private void loadData() {
        HttpUtil.getBalance(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String coin = obj.getString("coin");
                    mBalanceValue = Long.parseLong(coin);
                    mBalance.setText(coin);
                    List<CoinBean> list = JSON.parseArray(obj.getString("rules"), CoinBean.class);
                    if (mAdapter != null) {
                        mAdapter.setList(list, CoinAdapter.CHARGE);
                    }
                    mPaymode = obj.getIntValue("paymode");
                    mAliPayEnable = (mPaymode == 0 || mPaymode == 2) && obj.getIntValue("aliapp_switch") == 1;
                    mAliPartner = obj.getString("aliapp_partner");
                    mAliSellerId = obj.getString("aliapp_seller_id");
                    mAliPrivateKey = obj.getString("aliapp_key_android");
                    mWxPayEnable = (mPaymode == 0 || mPaymode == 2) && obj.getIntValue("wx_switch") == 1;
                    mWxAppID = obj.getString("wx_appid");
                    mOffLineEnable = mPaymode == 1 || mPaymode == 2;
                }
            }
        });
    }

    private void loadOrderData() {
        HttpUtil.getOrder(1,mStatus, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<CoinBean> list = JSON.parseArray(obj.getString("lists"), CoinBean.class);
                    if (mAdapter != null) {
                        mAdapter.setList(list, CoinAdapter.ORDER);
                    }
                }else {
                    ToastUtil.show(msg);
                }
            }
        });
    }
    private void loadMoreOrderData() {
        HttpUtil.getOrder(mStatus==1?mCurOrderP:mAppealOrderP,mStatus, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                mRefreshLayout.completeLoadMore();
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<CoinBean> list = JSON.parseArray(obj.getString("lists"), CoinBean.class);
                    if (mAdapter != null) {
                        mAdapter.insertList(list);
                    }
                }else {
                    if (mStatus==1){
                        mCurOrderP--;
                    }else if (mStatus==2){
                        mAppealOrderP--;
                    }
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {
                super.onError();
                mRefreshLayout.completeLoadMore();
            }
        });
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(HttpConsts.GET_BALANCE);
        HttpUtil.cancel(HttpConsts.GET_ALI_ORDER);
        HttpUtil.cancel(HttpConsts.GET_WX_ORDER);
        super.onDestroy();
    }

    @Override
    public void onItemClick(CoinBean bean, int position) {
        mCheckedCoinBean = bean;
        List<Integer> list = new ArrayList<>();
        if (mAliPayEnable) {
            list.add(R.string.online_pay_ali);
        }
        if (mWxPayEnable) {
            list.add(R.string.online_pay_wx);
        }
        if (mOffLineEnable) {
            list.add(R.string.offline_pay);
        }
        if (list.size() == 0) {
            ToastUtil.show(Constants.PAY_ALL_NOT_ENABLE);
            return;
        }
        DialogUitl.showStringArrayDialog(mContext, list.toArray(new Integer[list.size()]), mArrayDialogCallback);
    }

    private DialogUitl.StringArrayDialogCallback mArrayDialogCallback = new DialogUitl.StringArrayDialogCallback() {
        @Override
        public void onItemClick(String text, int tag) {
            switch (tag) {
                case R.string.online_pay_ali://支付宝支付
                    aliPay();
                    break;
                case R.string.online_pay_wx://微信支付
                    wxPay();
                    break;
                case R.string.offline_pay:
                    offPay();
                    break;
            }
        }
    };

    private void aliPay() {
        if (!AppConfig.isAppExist(Constants.PACKAGE_NAME_ALI)) {
            ToastUtil.show(R.string.coin_ali_not_install);
            return;
        }
        if (TextUtils.isEmpty(mAliPartner) || TextUtils.isEmpty(mAliSellerId) || TextUtils.isEmpty(mAliPrivateKey)) {
            ToastUtil.show(Constants.PAY_ALI_NOT_ENABLE);
            return;
        }
        AliPayBuilder builder = new AliPayBuilder(this, mAliPartner, mAliSellerId, mAliPrivateKey);
        builder.setCoinName(mCoinName);
        builder.setCoinBean(mCheckedCoinBean);
        builder.setPayCallback(mPayCallback);
        builder.pay();
    }

    private void wxPay() {
        if (!AppConfig.isAppExist(Constants.PACKAGE_NAME_WX)) {
            ToastUtil.show(R.string.coin_wx_not_install);
            return;
        }
        if (TextUtils.isEmpty(mWxAppID)) {
            ToastUtil.show(Constants.PAY_WX_NOT_ENABLE);
            return;
        }
        WxPayBuilder builder = new WxPayBuilder(mContext, mWxAppID);
        builder.setCoinBean(mCheckedCoinBean);
        builder.setPayCallback(mPayCallback);
        builder.pay();
    }

    private void offPay(){
        HttpUtil.getOffLineOrder(mCheckedCoinBean.getMoney(), mCheckedCoinBean.getId(), mCheckedCoinBean.getCoin(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String orderInfo = obj.getString("orderid");
                    WebViewActivity.forward(mContext, AppConfig.HOST+"/index.php?g=Appapi&m=Diamonds&a=index&uid="+AppConfig.getInstance().getUid()+"&token="+AppConfig.getInstance().getToken()+"&orderno="+orderInfo);
                }else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });
    }

    PayCallback mPayCallback = new PayCallback() {
        @Override
        public void onSuccess() {
            // checkPayResult();
        }

        @Override
        public void onFailed() {
            //ToastUtil.show(R.string.coin_charge_failed);
        }
    };

    /**
     * 检查支付结果
     */
    private void checkPayResult() {
        HttpUtil.getBalance(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String coin = obj.getString("coin");
                    mBalance.setText(coin);
                    long balanceValue = Long.parseLong(coin);
                    if (balanceValue > mBalanceValue) {
                        ToastUtil.show(R.string.coin_charge_success);
                        UserBean u = AppConfig.getInstance().getUserBean();
                        if (u != null) {
                            u.setCoin(coin);
                        }
                        EventBus.getDefault().post(new CoinChangeEvent(coin, true));
                    }
                }
            }
        });
    }

    public static void forward(Context context) {
        context.startActivity(new Intent(context, MyCoinActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_coin:
                mStatus=0;
                mRefreshLayout.setLoadMoreEnable(false);
                loadData();
                break;
            case R.id.btn_cur_order:
                mStatus=1;
                mRefreshLayout.setLoadMoreEnable(true);
                loadOrderData();
                break;
            case R.id.btn_appeal_order:
                mStatus=2;
                mRefreshLayout.setLoadMoreEnable(true);
                loadOrderData();
                break;
        }

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        if (mStatus==1){
            mCurOrderP++;
        }else if (mStatus==2){
            mAppealOrderP++;
        }
        loadMoreOrderData();
    }



}

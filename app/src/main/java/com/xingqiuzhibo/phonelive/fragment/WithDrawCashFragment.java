package com.xingqiuzhibo.phonelive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.HtmlConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.CashActivity;
import com.xingqiuzhibo.phonelive.activity.WebViewActivity;
import com.xingqiuzhibo.phonelive.bean.ProfitBean;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.IconUtil;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.SpUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hx
 * Time 2019/1/7/007.
 */

public class WithDrawCashFragment extends Fragment implements View.OnClickListener {
    private int mPosition;
    //    private TextView mAllName;//总映票数TextView
//    private TextView mAll;//总映票数
//    private TextView mCanName;//可提取映票数TextView
//    private TextView mCan;//可提取映票数
    private TextView mGetName;//输入要提取的映票数
    private TextView mMoney;
    private TextView mTip;//温馨提示
    private EditText mEdit;
    private int mRate;
    private long mMaxCanMoney;//可提取映票数
    private View mChooseTip;
    private View mAccountGroup;
    private ImageView mAccountIcon;
    private TextView mAccount;
    private String mAccountID;
    private String mVotesName;
    private View mBtnCash;
    private View view;

    private TextView tvHead1, tvHead2, tvHead3;
    private TextView tvOwn, tvCan, tvTotal;
    private LinearLayout layoutHead;
    private float proportion;//提现比例
    private float mCash;//提现金额
    private int mMinCash;//最低提现额度

    public static WithDrawCashFragment newInstance(int position) {
        WithDrawCashFragment fragment = new WithDrawCashFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_with_draw_cash, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt("position");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        main();
    }

    private void main() {
        mGetName = view.findViewById(R.id.get_name);
        mTip = view.findViewById(R.id.tip);
        mMoney = view.findViewById(R.id.money);
        mEdit = view.findViewById(R.id.edit);
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    long i = Long.parseLong(s.toString());
                    if (i > mMaxCanMoney) {
                        i = mMaxCanMoney;
                        s = String.valueOf(mMaxCanMoney);
                        mEdit.setText(s);
                        mEdit.setSelection(s.length());
                    }
                    if (proportion != 0) {
                        if (mPosition == 0) {
                            float a = (i * proportion) / 100;
                            if (a > 0.1) {
                                int b = (int) a * 100;
                                mCash = b / 100;
                                mMoney.setText("¥" + mCash);
                            }
                        } else {
                            mCash = ((int) (i * proportion));
                            mMoney.setText("¥" + mCash);
                        }

                    }
                    mBtnCash.setEnabled(true);
                } else {
                    mMoney.setText("¥");
                    mBtnCash.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mVotesName = AppConfig.getInstance().getVotesName();
        mBtnCash = view.findViewById(R.id.btn_cash);
        mBtnCash.setOnClickListener(this);
        view.findViewById(R.id.btn_choose_account).setOnClickListener(this);
        mChooseTip = view.findViewById(R.id.choose_tip);
        mAccountGroup = view.findViewById(R.id.account_group);
        mAccountIcon = view.findViewById(R.id.account_icon);
        mAccount = view.findViewById(R.id.account);

        tvHead1 = view.findViewById(R.id.tv_head_1);
        tvHead2 = view.findViewById(R.id.tv_head_2);
        tvHead3 = view.findViewById(R.id.tv_head_3);
        layoutHead = view.findViewById(R.id.layout_title_bg);
        tvOwn = view.findViewById(R.id.tv_data_user_own);
        tvCan = view.findViewById(R.id.tv_data_user_can);
        tvTotal = view.findViewById(R.id.tv_data_user_total);

        if (mPosition == 0) {
            tvHead1.setText("钻石总数");
            tvHead3.setText("可提现钻石数");
            mGetName.setText("输入要提取的钻石数");
            layoutHead.setBackgroundResource(R.mipmap.img_diamond);
        } else {
            tvHead1.setText(mVotesName + "总数");
            tvHead3.setText("可提现" + mVotesName + "数");
            mGetName.setText("输入要提取的" + mVotesName + "数");
            layoutHead.setBackgroundResource(R.mipmap.img_hit_ticket);
        }


        loadData();
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", AppConfig.getInstance().getUid());
        NetWork.httpParamGet(UrlUtil.GET_USER_PROFIT_MSG, map, getContext(), request);
    }


    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            try {
                JSONObject jsb = JSON.parseObject(responseInfo.result);
                if (jsb.getInteger("code") == 0) {
                    JSONObject data = jsb.getJSONObject("cashInfo");
                    ProfitBean bean = JSON.parseObject(JSON.toJSONString(data), ProfitBean.class);
                    //设置头部信息
                    if (mPosition == 0) {
                        proportion = Float.valueOf(bean.getDiamondRate());
                        tvHead2.setText("100钻石=" + proportion + "元");
                        tvOwn.setText(String.valueOf(bean.getCoin()));
                        tvCan.setText(String.valueOf(bean.getUseCoin()));
                        tvTotal.setText(String.valueOf(bean.getCoinTotal()));
                        mMaxCanMoney = Long.valueOf(bean.getUseCoin());
                    } else {
                        proportion = Float.valueOf(bean.getTickRate());
                        tvHead2.setText("1" + mVotesName + "=" + proportion + "元");
                        tvOwn.setText(String.valueOf(bean.getVotes()));
                        tvCan.setText(String.valueOf(bean.getUseVotes()));
                        tvTotal.setText(String.valueOf(bean.getVotestotal()));
                        mMaxCanMoney = Long.valueOf(bean.getUseVotes());
                    }
                    //设置提示信息
                    mTip.setText(bean.getCashExplain());
                    mMinCash = bean.getCashMin();
                }
            } catch (Exception e) {
                L.e("用户收益基础信息查询错误------>" + e.getClass() + "------>" + e.getMessage());
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cash:
                cash();
                break;
            case R.id.btn_choose_account:
                chooseAccount();
                break;
        }
    }


    /**
     * 提现
     */
    private void cash() {
        String votes = mEdit.getText().toString().trim();
        if (TextUtils.isEmpty(votes)) {
            if (mPosition == 0)
                mVotesName = "钻石";
            ToastUtil.show("输入要提现的" + mVotesName + "数");
            return;
        }
        if (TextUtils.isEmpty(mAccountID)) {
            ToastUtil.show(R.string.profit_choose_account);
            return;
        }
        if (mCash < mMinCash) {
            ToastUtil.show("最低提现额度不能小于" + mMinCash);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("uid", AppConfig.getInstance().getUid());
        String type = "1";
        if (mPosition == 1)
            type = "0";
        map.put("cashType", type);//(提现类型 0银票提现 1钻石提现)
        map.put("accountId", mAccountID);
        map.put("votes", votes);
        map.put("money", (int) mCash + "");
        NetWork.httpPost(UrlUtil.USER_PROFIT_APPLY, map, getContext(), requestCash);
//        HttpUtil.doCash(votes, mAccountID, new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                ToastUtil.show(msg);
//            }
//        });
    }

    /**
     * 提现申请返回参数
     */
    RequestCallBack<String> requestCash = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {

            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                ToastUtil.show("信息提交成功！");
                String url = HtmlConfig.CASH_RECORD;
                WebViewActivity.forward(getContext(), url);
                getActivity().finish();
            } else {
                ToastUtil.show(jsb.getString("msg"));
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    /**
     * 选择账户
     */
    private void chooseAccount() {
        Intent intent = new Intent(getContext(), CashActivity.class);
        intent.putExtra(Constants.CASH_ACCOUNT_ID, mAccountID);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAccount();
    }

    private void getAccount() {
        String[] values = SpUtil.getInstance().getMultiStringValue(Constants.CASH_ACCOUNT_ID, Constants.CASH_ACCOUNT, Constants.CASH_ACCOUNT_TYPE);
        if (values != null && values.length == 3) {
            String accountId = values[0];
            String account = values[1];
            String type = values[2];
            if (!TextUtils.isEmpty(accountId) && !TextUtils.isEmpty(account) && !TextUtils.isEmpty(type)) {
                if (mChooseTip.getVisibility() == View.VISIBLE) {
                    mChooseTip.setVisibility(View.INVISIBLE);
                }
                if (mAccountGroup.getVisibility() != View.VISIBLE) {
                    mAccountGroup.setVisibility(View.VISIBLE);
                }
                mAccountID = accountId;
                mAccountIcon.setImageResource(IconUtil.getCashTypeIcon(Integer.parseInt(type)));
                mAccount.setText(account);
            } else {
                if (mAccountGroup.getVisibility() == View.VISIBLE) {
                    mAccountGroup.setVisibility(View.INVISIBLE);
                }
                if (mChooseTip.getVisibility() != View.VISIBLE) {
                    mChooseTip.setVisibility(View.VISIBLE);
                }
                mAccountID = null;
            }
        } else {
            if (mAccountGroup.getVisibility() == View.VISIBLE) {
                mAccountGroup.setVisibility(View.INVISIBLE);
            }
            if (mChooseTip.getVisibility() != View.VISIBLE) {
                mChooseTip.setVisibility(View.VISIBLE);
            }
            mAccountID = null;
        }
    }

//    @Override
//    protected void onDestroy() {
//        HttpUtil.cancel(HttpConsts.DO_CASH);
//        HttpUtil.cancel(HttpConsts.GET_PROFIT);
//        super.onDestroy();
//    }
}

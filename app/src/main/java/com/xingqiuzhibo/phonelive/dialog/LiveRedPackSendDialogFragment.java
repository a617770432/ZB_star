package com.xingqiuzhibo.phonelive.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.LiveActivity;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.utils.DpUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

/**
 * Created by cxf on 2018/11/19.
 * 发红包的弹窗
 */

public class LiveRedPackSendDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private EditText mEditCoin;//钻石数量
    private EditText mEditCount;//红包数量
    private EditText mEditTitle;//红包标题
    private CheckBox mCheckBox;
    private TextView mCoinNameTextView;
    private int mRedPackType;
    private String mStream;
    private String mCoinName;
    private String mGe;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_red_pack_send;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(300);
        params.height = DpUtil.dp2px(380);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public void setCoinName(String coinName) {
        mCoinName = coinName;
    }

    public void setStream(String stream) {
        mStream = stream;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (TextUtils.isEmpty(mStream)) {
            return;
        }
        mRootView.findViewById(R.id.btn_shou_qi).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_average).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_send).setOnClickListener(this);
        mEditCoin = mRootView.findViewById(R.id.edit_coin);
        String coin = mEditCoin.getText().toString().trim();
        if (!TextUtils.isEmpty(coin)) {
            mEditCoin.setSelection(coin.length());
        }
        mEditCount = mRootView.findViewById(R.id.edit_count);
        mEditTitle = mRootView.findViewById(R.id.edit_title);
        mCheckBox = mRootView.findViewById(R.id.checkbox);
        mCoinNameTextView = mRootView.findViewById(R.id.coin_name);
        mCoinNameTextView.setText(mCoinName);
        mRedPackType = Constants.RED_PACK_TYPE_SHOU_QI;
        mGe = WordUtil.getString(R.string.ge);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shou_qi://拼手气
                mRedPackType = Constants.RED_PACK_TYPE_SHOU_QI;
                mCoinNameTextView.setText(mCoinName);
                break;
            case R.id.btn_average://平均红包
                mRedPackType = Constants.RED_PACK_TYPE_AVERAGE;
                mCoinNameTextView.setText(mCoinName + "/" + mGe);
                break;
            case R.id.btn_send://发红包按钮
                sendRedPack();
                break;
        }
    }

    /**
     * 发红包
     */
    private void sendRedPack() {
        String coin = mEditCoin.getText().toString().trim();
        if (TextUtils.isEmpty(coin)) {
            ToastUtil.show(R.string.red_pack_7);
            return;
        }
        String count = mEditCount.getText().toString().trim();
        if (TextUtils.isEmpty(count)) {
            ToastUtil.show(R.string.red_pack_8);
            return;
        }
        String title = mEditTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            title = mEditTitle.getHint().toString().trim();
        }
        int sendType = mCheckBox.isChecked() ? Constants.RED_PACK_SEND_TIME_NORMAL : Constants.RED_PACK_SEND_TIME_DELAY;
        HttpUtil.sendRedPack(mStream, coin, count, title, mRedPackType, sendType, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    dismiss();
                    ((LiveActivity) mContext).sendRedPackMessage();
                }
                ToastUtil.show(msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        HttpUtil.cancel(HttpConsts.SEND_RED_PACK);
        super.onDestroy();
    }
}

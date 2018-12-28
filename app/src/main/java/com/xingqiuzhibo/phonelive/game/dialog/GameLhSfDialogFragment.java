package com.xingqiuzhibo.phonelive.game.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.LiveActivity;
import com.xingqiuzhibo.phonelive.dialog.AbsDialogFragment;
import com.xingqiuzhibo.phonelive.game.adapter.GameLhSfAdapter;
import com.xingqiuzhibo.phonelive.game.adapter.GameNzSfAdapter;
import com.xingqiuzhibo.phonelive.game.bean.GameLhRecordBean;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.utils.DpUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/11/5.
 * 龙虎胜负走势图
 */

public class GameLhSfDialogFragment extends AbsDialogFragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private TextView mTvCoinName;
    @Override
    protected int getLayoutId() {
        return R.layout.game_dialog_lh_sf;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return false;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(280);
        params.height = DpUtil.dp2px(360);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        String stream = bundle.getString(Constants.STREAM);
        String liveUid = bundle.getString(Constants.LIVEUID);
        if (TextUtils.isEmpty(stream)) {
            return;
        }
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        mTvCoinName = mRootView.findViewById(R.id.tv_coin_name);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRootView.findViewById(R.id.btn_close).setOnClickListener(this);
        mTvCoinName.setText(AppConfig.getInstance().getCoinName());
        HttpUtil.dragonLogByuser(stream,liveUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<GameLhRecordBean> arrays = JSON.parseArray(info[0], GameLhRecordBean.class);
                    GameLhSfAdapter adapter = new GameLhSfAdapter(mContext, arrays);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onDestroy() {
        HttpUtil.cancel(HttpConsts.GAME_NIU_RECORD);
        super.onDestroy();
    }
}

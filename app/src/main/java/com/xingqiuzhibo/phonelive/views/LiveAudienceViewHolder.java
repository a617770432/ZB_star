package com.xingqiuzhibo.phonelive.views;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.LiveActivity;
import com.xingqiuzhibo.phonelive.activity.LiveAudienceActivity;
import com.xingqiuzhibo.phonelive.dialog.LiveGiftDialogFragment;
import com.xingqiuzhibo.phonelive.dialog.LiveShareDialogFragment;

/**
 * Created by cxf on 2018/10/9.
 * 观众直播间逻辑
 */

public class LiveAudienceViewHolder extends AbsLiveViewHolder {

    private String mLiveUid;
    private String mStream;

    public LiveAudienceViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_audience;
    }

    @Override
    public void init() {
        super.init();
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_red_pack).setOnClickListener(this);
        findViewById(R.id.btn_gift).setOnClickListener(this);
    }

    public void setLiveInfo(String liveUid, String stream) {
        mLiveUid = liveUid;
        mStream = stream;
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_close:
                close();
                break;
            case R.id.btn_share:
                openShareWindow();
                break;
            case R.id.btn_red_pack:
                ((LiveActivity) mContext).openRedPackSendWindow();
                break;
            case R.id.btn_gift:
                openGiftWindow();
                break;
        }
    }

    /**
     * 退出直播间
     */
    private void close() {
        ((LiveAudienceActivity) mContext).onBackPressed();
    }


    /**
     * 打开礼物窗口
     */
    private void openGiftWindow() {
        ((LiveAudienceActivity) mContext).openGiftWindow();
    }

    /**
     * 打开分享窗口
     */
    private void openShareWindow() {
        ((LiveActivity) mContext).openShareWindow();
    }

}

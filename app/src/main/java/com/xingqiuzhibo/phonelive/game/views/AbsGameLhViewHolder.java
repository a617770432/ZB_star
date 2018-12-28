package com.xingqiuzhibo.phonelive.game.views;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.LiveActivity;
import com.xingqiuzhibo.phonelive.custom.ItemDecoration;
import com.xingqiuzhibo.phonelive.event.LhGameCloseEvent;
import com.xingqiuzhibo.phonelive.game.GameSoundPool;
import com.xingqiuzhibo.phonelive.game.adapter.LhTextAdapter;
import com.xingqiuzhibo.phonelive.game.bean.GameLhBean;
import com.xingqiuzhibo.phonelive.game.bean.GameParam;
import com.xingqiuzhibo.phonelive.game.dialog.GameLhSfDialogFragment;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.utils.DpUtil;
import com.xingqiuzhibo.phonelive.utils.GameDialogUils;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.ScreenDimenUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by debug on 2018/12/10.
 * 龙虎 游戏
 */
public abstract class AbsGameLhViewHolder extends AbsGameViewHolder {
    protected static final int WHAT_BET_COUNT_DOWN = 104;//下注倒计时
    protected static final int WHAT_HIDE_DIALOG = 107;//隐藏弹窗
    protected TextView mBetCountDown;//下注倒计时的TextView
    protected TextView mRecordL;//记录 龙
    protected TextView mRecordHe;//记录 和
    protected TextView mRecordHu;//记录 虎
    private RecyclerView mResultTextRv;
    private RecyclerView mResultNoTextRv;
    protected Handler mHandler;
    protected int mBetCount;
    protected TextView[] mWinPerCent;//赔率
    private int mBotViewHeight;
    private LhTextAdapter mLhTextAdapter;
    private LhTextAdapter mLhTextAdapterC;
    private List<Integer> mLhList;
    private List<Integer> mLhCList;
    private GameLhSfDialogFragment mGameLhSfDialogFragment;
    private Dialog mResultDialog;
    protected boolean isCanBalance;//结算，下注倒计时结束可结算
    public GameLhBean mEnterGameLhBean;
    protected JSONObject mBetEndJSONObject;

    public AbsGameLhViewHolder(GameParam param, GameSoundPool gameSoundPool) {
        super(param, gameSoundPool);
        isLhGame = true;
        mBotViewHeight = (ScreenDimenUtil.getInstance().getScreenWdith()) / 3;
        mGameViewHeight = mBotViewHeight + DpUtil.dp2px(170) + DpUtil.dp2px(40);
        LinearLayout result = (LinearLayout) findViewById(R.id.root_result);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) result.getLayoutParams();
        lp.height = mBotViewHeight;
        result.setLayoutParams(lp);
        lp = (LinearLayout.LayoutParams) mResultTextRv.getLayoutParams();
        lp.width = mBotViewHeight-(mBotViewHeight - DpUtil.dp2px(6)) %6*6;
        lp.setMargins((mBotViewHeight - DpUtil.dp2px(6)) %6*6,0,0,0);
        mResultTextRv.setLayoutParams(lp);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 6, LinearLayoutManager.HORIZONTAL, false);
        mResultTextRv.setLayoutManager(gridLayoutManager);
        ItemDecoration decoration = new ItemDecoration(mContext, 0xffb5b5b5, 1, 1);
        decoration.setOnlySetItemOffsetsButNoDraw(false);
        mResultTextRv.addItemDecoration(decoration);
        gridLayoutManager = new GridLayoutManager(mContext, 12, LinearLayoutManager.HORIZONTAL, false);
        mResultNoTextRv.setLayoutManager(gridLayoutManager);
        decoration = new ItemDecoration(mContext, 0xffb5b5b5, 1, 1);
        decoration.setOnlySetItemOffsetsButNoDraw(false);
        mResultNoTextRv.addItemDecoration(decoration);
        initBotView();
        mRecordL.setOnClickListener(this);
        mRecordHe.setOnClickListener(this);
        mRecordHu.setOnClickListener(this);
    }

    private void initBotView() {
        mLhList = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            mLhList.add(i);
        }
        if (mLhTextAdapter == null) {
            mLhTextAdapter = new LhTextAdapter(mContext, mLhList, (mBotViewHeight - DpUtil.dp2px(6)) / 6);
            mResultTextRv.setAdapter(mLhTextAdapter);
        }
        mLhCList = new ArrayList<>();
        for (int i = 0; i < 12 * 24; i++) {
            mLhCList.add(i);
        }
        if (mLhTextAdapterC == null) {
            mLhTextAdapterC = new LhTextAdapter(mContext, mLhCList, (mBotViewHeight - DpUtil.dp2px(12)) / 12);
            mResultNoTextRv.setAdapter(mLhTextAdapterC);
        }
    }

    @Override
    public void init() {
        mResultTextRv = (RecyclerView) findViewById(R.id.result_text);
        mResultNoTextRv = (RecyclerView) findViewById(R.id.result_notext);
        mBetCountDown = (TextView) findViewById(R.id.count_down_2);
        mRecordL = (TextView) findViewById(R.id.record_l);
        mRecordHe = (TextView) findViewById(R.id.record_he);
        mRecordHu = (TextView) findViewById(R.id.record_hu);
        mWinPerCent = new TextView[3];
        mWinPerCent[0] = (TextView) findViewById(R.id.per_l);
        mWinPerCent[1] = (TextView) findViewById(R.id.per_hu);
        mWinPerCent[2] = (TextView) findViewById(R.id.per_he);
        mRecordL.setOnClickListener(this);
        mRecordHe.setOnClickListener(this);
        mRecordHu.setOnClickListener(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_BET_COUNT_DOWN://下注倒计时
                        betCountDown();
                        break;
                    case WHAT_HIDE_DIALOG:
                        if (mResultDialog != null) {
                            mResultDialog.dismiss();
                        }
                        break;
                }
            }
        };
    }

    /**
     * 所有人收到 打开游戏窗口的socket后，   打开游戏窗口，启动角色待机动画，进入8秒准备倒计时，
     */
    protected void onGameWindowShow(JSONObject object) {
        if (!mShowed) {
            JSONObject objCt = object.getJSONObject("ct");
            if (mWinPerCent != null) {
                mWinPerCent[0].setText(objCt.getString("rate_1") == null ? "" : objCt.getString("rate_1"));
                mWinPerCent[1].setText(objCt.getString("rate_2") == null ? "" : objCt.getString("rate_2"));
                mWinPerCent[2].setText(objCt.getString("rate_3") == null ? "" : objCt.getString("rate_3"));
            }
            showGameWindow();
            mBetStarted = false;
        }
    }

    @Override
    public void anchorCreateGame() {
    }

    @Override
    public void enterRoomOpenGameWindow() {

    }

    /**
     * 收到主播通知下注的socket，开始下注倒计时
     */
    protected void onGameBetStart(JSONObject obj) {
        JSONObject objCt = obj.getJSONObject("ct");
        if (!mShowed) {
            showGameWindow();
        }
        mBetStarted = true;
        mGameID = objCt.getString("gameid");
        mBetTime = objCt.getIntValue("time");
        mBetCount = mBetTime;
        if (mBetCountDown != null) {
            mBetCountDown.setBackgroundResource(R.mipmap.game_lh_clock);
            mBetCountDown.setText(String.valueOf(mBetCount));
        }
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(WHAT_BET_COUNT_DOWN, 1000);
        }
        playGameSound(GameSoundPool.GAME_SOUND_BET_START);
    }

    protected abstract void onGameBetChanged(JSONObject object);

    protected abstract void onBetEnd(JSONObject object);

    protected abstract void onGameResult(JSONObject object);


    //---------------------
    @Override
    public void anchorCloseGame() {
    }

    @Override
    protected void getGameResult() {
    }

    @Override
    protected void nextGame() {
    }
    //---------------------

    /**
     * 主播关闭游戏的回调
     */
    protected void onGameClose() {
        hideGameWindow();
        release();
        EventBus.getDefault().post(new LhGameCloseEvent());
    }

    protected void onGameResultView(JSONObject object) {
        onGameResultBot(object.getString("logarray"), object.getString("mainarr"));
        setRecordView(object.getString("long"), object.getString("he"), object.getString("hu"));
    }

    protected void onGameResultDialog(JSONObject object) {
        int coinwin = object.getIntValue("coinwin");
        mResultDialog = GameDialogUils.showGameResultDialog(mContext, coinwin >= 0 ? WordUtil.getString(R.string.game_lh_win) : WordUtil.getString(R.string.game_lh_lose), String.valueOf(coinwin), mAnchor);
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(WHAT_HIDE_DIALOG, 3000);
        }
    }

    protected void onGameResultBot(String logarray, String mainarr) {
        mLhList = JSON.parseArray(logarray, Integer.class);
        mLhCList = JSON.parseArray(mainarr, Integer.class);
        if (mLhTextAdapter != null) {
            int size = mLhList.size();
            if (size < 36) {
                for (int i = 0; i < 36 - size; i++) {
                    mLhList.add(0);
                }
            } else if (size > 36) {
                if (size % 6 != 0) {
                    for (int i = 0; i < 6 - size % 6; i++) {
                        mLhList.add(0);
                    }
                }
            }
            mLhTextAdapter.setList(mLhList, 1);
            mResultTextRv.smoothScrollToPosition(mLhList.size());
        }
        if (mLhTextAdapterC != null) {
            mLhTextAdapterC.setList(mLhCList, 2);
            mResultNoTextRv.smoothScrollToPosition(mLhCList.size());
            L.e("---smoothScrollToPosition>>22---" + mLhCList.size());
        }
    }

    @Override
    public void removeFromParent() {
        super.removeFromParent();
        if (mResultDialog != null) {
            mResultDialog = null;
        }
    }

    /**
     * 下注倒计时
     */
    protected void betCountDown() {
        if (mBetCount > 0) {
            mBetCount--;
        }
        mBetCountDown.setText(String.valueOf(mBetCount));
        if (mBetCount > 0) {
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(WHAT_BET_COUNT_DOWN, 1000);
            }
        } else {
            mBetCountDown.setBackgroundResource(R.mipmap.game_lh_unclock);
            isCanBalance = true;
            mBetStarted = false;
        }
    }

    /**
     * 胜负记录
     */
    private void showRecord() {
        mGameLhSfDialogFragment = new GameLhSfDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STREAM, mStream);
        bundle.putString(Constants.LIVEUID, mLiveUid);
        mGameLhSfDialogFragment.setArguments(bundle);
        mGameLhSfDialogFragment.show(((LiveActivity) mContext).getSupportFragmentManager(), "GameNzSfDialogFragment");
    }

    @Override
    public void release() {
        mEnd = true;
        isLhGame = false;
        HttpUtil.cancel(HttpConsts.GET_COIN);
        HttpUtil.cancel(HttpConsts.GAME_LH_OPENVIEW);
        HttpUtil.cancel(HttpConsts.GAME_LH_START);
        HttpUtil.cancel(HttpConsts.GAME_LH_OVER);
        HttpUtil.cancel(HttpConsts.GAME_LH_END);
        HttpUtil.cancel(HttpConsts.GAME_LH_VOTES);
        super.release();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mGameLhSfDialogFragment != null) {
            mGameLhSfDialogFragment.dismiss();
        }
        mHandler = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_l:
            case R.id.record_he:
            case R.id.record_hu:
                if (!mAnchor) {
                    showRecord();
                }
                break;
        }
    }

    public void setEnterGameLhBean(GameLhBean enterGameLhBean) {
        mEnterGameLhBean = enterGameLhBean;
    }

    public boolean isCanBalance() {
        return isCanBalance;
    }

    protected void setRecordView(String num_1, String num_2, String num_3) {
        mRecordL.setText(WordUtil.getString(R.string.game_lh_l) + "(" + num_1 + ")");
        mRecordHu.setText(WordUtil.getString(R.string.game_lh_hu) + "(" + num_3 + ")");
        mRecordHe.setText(WordUtil.getString(R.string.game_lh_he) + "(" + num_2 + ")");
    }

}

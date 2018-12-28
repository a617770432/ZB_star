package com.xingqiuzhibo.phonelive.game.views;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.custom.ItemDecoration;
import com.xingqiuzhibo.phonelive.event.GameWindowEvent;
import com.xingqiuzhibo.phonelive.game.GameSoundPool;
import com.xingqiuzhibo.phonelive.game.adapter.LhTextAdapter;
import com.xingqiuzhibo.phonelive.game.bean.GameParam;
import com.xingqiuzhibo.phonelive.game.socket.SocketGameUtil;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.DpUtil;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.ScreenDimenUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by debug on 2018/12/10.
 * 龙虎 游戏 用户
 */

public class GameLh_GzViewHolder extends AbsGameLhViewHolder {
    private TextView mCoinTextView;//显示用户余额的TextView
    private TextView[] mBetSumAndPerNum;//下注总额和下注人数
    private TextView[] mMyBetView;//自己的下注数
    private TextView[] mBetView;//龙虎和
    private LinearLayout[] mLlBetClick;
    private TextView mBetConfirm;//下注确认
    private TextView mGameTips;//游戏提示
    private int mBetWhich;//下注哪个


    public GameLh_GzViewHolder(GameParam param, GameSoundPool gameSoundPool) {
        super(param, gameSoundPool);
        ViewStub viewStub = (ViewStub) findViewById(R.id.view_stub);
        View view = viewStub.inflate();
        view.findViewById(R.id.btn_bet_shi).setOnClickListener(this);
        view.findViewById(R.id.btn_bet_bai).setOnClickListener(this);
        view.findViewById(R.id.btn_bet_qian).setOnClickListener(this);
        view.findViewById(R.id.btn_bet_wan).setOnClickListener(this);
        mCoinTextView = (TextView) view.findViewById(R.id.coin);
        mCoinTextView.setOnClickListener(this);
        mBetMoney = 10;
        HttpUtil.getCoin(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    setLastCoin(JSONObject.parseObject(info[0]).getString("coin"));
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.game_view_lhh_gz;
    }

    @Override
    public void init() {
        super.init();
        mBetSumAndPerNum = new TextView[3];
        mMyBetView = new TextView[3];
        mBetView = new TextView[3];
        mLlBetClick = new LinearLayout[3];
        mBetSumAndPerNum[0] = (TextView) findViewById(R.id.bet_and_per_l);
        mBetSumAndPerNum[1] = (TextView) findViewById(R.id.bet_and_per_hu);
        mBetSumAndPerNum[2] = (TextView) findViewById(R.id.bet_and_per_he);
        mMyBetView[0] = (TextView) findViewById(R.id.mybet_l);
        mMyBetView[1] = (TextView) findViewById(R.id.mybet_hu);
        mMyBetView[2] = (TextView) findViewById(R.id.mybet_he);
        mBetView[0] = (TextView) findViewById(R.id.bet_view_l);
        mBetView[1] = (TextView) findViewById(R.id.bet_view_hu);
        mBetView[2] = (TextView) findViewById(R.id.bet_view_he);
        mBetConfirm = (TextView) findViewById(R.id.bet_confirm);
        mGameTips = (TextView) findViewById(R.id.tv_tip);
        mLlBetClick[0] = (LinearLayout) findViewById(R.id.ll_bet_l);
        mLlBetClick[1] = (LinearLayout) findViewById(R.id.ll_bet_he);
        mLlBetClick[2] = (LinearLayout) findViewById(R.id.ll_bet_hu);
        mBetConfirm.setOnClickListener(this);
        for (View v : mLlBetClick) {
            v.setEnabled(false);
            v.setOnClickListener(this);
        }
    }

    /**
     * 显示观众的余额
     */
    @Override
    public void setLastCoin(String coin) {
        if (mCoinTextView != null) {
            mCoinTextView.setText(coin + " " + mChargeString);
        }
    }

    /**
     * 处理socket回调的数据
     */
    public void handleSocket(int action, JSONObject obj) {
        if (mEnd) {
            return;
        }
        switch (action) {
            case SocketGameUtil.GAME_ACTION_LH_OPENVIEW://打开游戏窗口
                onGameWindowShow(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_STARTBET://开始下注
                onGameBetStart(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_BETING://收到下注消息
                onGameBetChanged(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_BETEND:
                onBetEnd(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_BANLANCE_RESULT://收到游戏结果揭晓的的消息
                onGameResult(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_BANLANCE_RESULT2:
                onGameResultView(obj.getJSONObject("ct"));
                break;
            case SocketGameUtil.GAME_ACTION_LH_CLOSE://主播关闭游戏
                onGameClose();
                break;
        }
    }

    /**
     * 观众下注
     */
    private void audienceBetGame() {
        HttpUtil.dragonVotes(mStream, mLiveUid, mBetWhich, mBetMoney, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    setLastCoin(JSON.parseObject(info[0]).getString("coin_left"));
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }


    @Override
    protected void onGameBetStart(JSONObject obj) {
        super.onGameBetStart(obj);
        gameStart();
    }

    /**
     * 所有人收到下注的观众socket，更新下注金额
     */
    @Override
    protected void onGameBetChanged(JSONObject obj) {
        JSONObject objCt = obj.getJSONObject("ct");
        String uid = objCt.getString("uid");
        int part = objCt.getIntValue("part");
        int index = part - 1;
        boolean isSelf = uid.equals(AppConfig.getInstance().getUid());
        if (isSelf) {//自己下的注
            playGameSound(GameSoundPool.GAME_SOUND_BET_SUCCESS);
            mGameTips.setText(WordUtil.getString(R.string.game_lh_al_bet));
        }
        if (index >= 0 && index < 3) {
            if (isSelf) {
                if (mMyBetView[index] != null) {
                    setBetView(mMyBetView[index], objCt.getString("coin_" + part));
                }
            }
            if (mBetSumAndPerNum[index] != null) {
                setBetAndPersons(mBetSumAndPerNum[index], objCt.getString("coinall_" + part), objCt.getString("num_" + part));
            }
        }

    }

    @Override
    protected void onBetEnd(JSONObject object) {
        mBetEndJSONObject = object;
        if (mBetCount > 0) {
            return;
        }
        mBetStarted = false;
        mBetConfirm.setEnabled(false);
        mGameTips.setText(WordUtil.getString(R.string.game_waiting_appeal));
        for (View v : mLlBetClick) {
            v.setEnabled(false);
        }
    }

    @Override
    protected void onGameResult(JSONObject object) {
        try {
            JSONObject objCt = object.getJSONObject("ct");
            if (mLiveUid.equals(objCt.getString("liveuid"))) {
                onGameResultDialog(objCt);
                setLastCoin((Integer.parseInt(mCoinTextView.getText().toString().split(mChargeString)[0].trim())+objCt.getIntValue("coinwget"))+"");
            }
        }catch (Exception e){

        }

    }

    /**
     * 游戏中途进入直播间的打开游戏窗口
     */
    @Override
    public void enterRoomOpenGameWindow() {
        try {
            if (!mShowed) {
                showGameWindow();
                mBetCount = mBetTime ;
                if (mBetCount > 0 && mBetCountDown != null) {
                    mBetCountDown.setBackgroundResource(R.mipmap.game_lh_clock);
                    mBetCountDown.setText(String.valueOf(mBetCount));
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(WHAT_BET_COUNT_DOWN, 1000);
                    }
                }
                playGameSound(GameSoundPool.GAME_SOUND_BET_START);
                if (mEnterGameLhBean!=null){
                    setBetView(mMyBetView[0],mEnterGameLhBean.getCoin_1());
                    setBetView(mMyBetView[1],mEnterGameLhBean.getCoin_2());
                    setBetView(mMyBetView[2],mEnterGameLhBean.getCoin_3());
                    setBetAndPersons(mBetSumAndPerNum[0],mEnterGameLhBean.getCoinall_1(),mEnterGameLhBean.getNum_1());
                    setBetAndPersons(mBetSumAndPerNum[1],mEnterGameLhBean.getCoinall_2(),mEnterGameLhBean.getNum_2());
                    setBetAndPersons(mBetSumAndPerNum[2],mEnterGameLhBean.getCoinall_3(),mEnterGameLhBean.getNum_3());
                    setRecordView(mEnterGameLhBean.getLongX(),mEnterGameLhBean.getHe(),mEnterGameLhBean.getHu());
                    mWinPerCent[0].setText(mEnterGameLhBean.getRate_1());
                    mWinPerCent[1].setText(mEnterGameLhBean.getRate_2());
                    mWinPerCent[2].setText(mEnterGameLhBean.getRate_3());
                    onGameResultBot(mEnterGameLhBean.getLogarray(),mEnterGameLhBean.getMainarr());
                    if (mEnterGameLhBean.getStatus()==0){
                        mBetStarted = true;
                        mGameTips.setText(WordUtil.getString(R.string.game_lh_please_bet));
                        mBetConfirm.setEnabled(true);
                        for (View v : mLlBetClick) {
                            v.setEnabled(true);
                        }
                        if (!mEnterGameLhBean.getCoin_1().equals("0")){
                            mBetWhich=1;
                        }else if (!mEnterGameLhBean.getCoin_2().equals("0")){
                            mBetWhich=2;
                        }else if (!mEnterGameLhBean.getCoin_3().equals("0")){
                            mBetWhich=3;
                        }
                        setBetViewSelect();
                    }else if (mEnterGameLhBean.getStatus()==1){
                        mBetStarted = false;
                        mBetConfirm.setEnabled(false);
                        mGameTips.setText(WordUtil.getString(R.string.game_waiting_appeal));
                        for (View v : mLlBetClick) {
                            v.setEnabled(false);
                        }
                    }else if (mEnterGameLhBean.getStatus()==2){
                        mGameTips.setText(WordUtil.getString(R.string.game_lh_please_bet));
                        mBetStarted = false;
                        mBetConfirm.setEnabled(false);
                        for (View v : mLlBetClick) {
                            v.setEnabled(false);
                        }
                        if (!mEnterGameLhBean.getCoin_1().equals("0")){
                            mBetWhich=1;
                        }else if (!mEnterGameLhBean.getCoin_2().equals("0")){
                            mBetWhich=2;
                        }else if (!mEnterGameLhBean.getCoin_3().equals("0")){
                            mBetWhich=3;
                        }
                        setBetViewSelect();
                    }
                }

            }
        }catch (Exception e){

        }
    }

    @Override
    protected void betCountDown() {
        super.betCountDown();
        if (!mBetStarted) {
            if (mBetEndJSONObject != null) {
                onBetEnd(mBetEndJSONObject);
                mBetEndJSONObject = null;
            }
        }
    }

    @Override
    public void removeFromParent() {
        super.removeFromParent();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bet_confirm:
                if (mBetWhich == -1) {
                    ToastUtil.show(WordUtil.getString(R.string.game_lh_bet_tip));
                    return;
                }
                audienceBetGame();
                break;
            case R.id.ll_bet_l:
                mBetWhich = 1;
                setBetViewSelect();
                break;
            case R.id.ll_bet_he:
                mBetWhich = 3;
                setBetViewSelect();
                break;
            case R.id.ll_bet_hu:
                mBetWhich = 2;
                setBetViewSelect();
                break;
            case R.id.btn_bet_shi:
                mBetMoney = 10;
                playGameSound(GameSoundPool.GAME_SOUND_BET_CHOOSE);
                break;
            case R.id.btn_bet_bai:
                mBetMoney = 100;
                playGameSound(GameSoundPool.GAME_SOUND_BET_CHOOSE);
                break;
            case R.id.btn_bet_qian:
                mBetMoney = 1000;
                playGameSound(GameSoundPool.GAME_SOUND_BET_CHOOSE);
                break;
            case R.id.btn_bet_wan:
                mBetMoney = 10000;
                playGameSound(GameSoundPool.GAME_SOUND_BET_CHOOSE);
                break;
            case R.id.coin://充值
                forwardCharge();
                break;
        }
    }

    private void setBetView(TextView betView, String money) {
        if (betView.getVisibility() == View.INVISIBLE && !"0".equals(money)) {
            betView.setVisibility(View.VISIBLE);
        }
        betView.setText(money);
    }

    private void setBetAndPersons(TextView view, String money, String num) {
        view.setText(money + "/" + num);
    }

    private void setBetViewSelect() {
        if (mBetView != null) {
            for (int i = 0; i < mBetView.length; i++) {
                if (i == mBetWhich - 1) {
                    mBetView[i].setTextColor(Color.BLACK);
                } else {
                    if (i == 0) {
                        mBetView[i].setTextColor(mContext.getResources().getColor(R.color.long_));
                    } else if (i == 2) {
                        mBetView[i].setTextColor(mContext.getResources().getColor(R.color.hu));
                    } else {
                        mBetView[i].setTextColor(mContext.getResources().getColor(R.color.he));
                    }
                }
            }
        }
    }

    private void gameStart() {
        mBetWhich = -1;
        setBetViewSelect();
        for (TextView v : mBetSumAndPerNum) {
            setBetAndPersons(v, "0", "0");
        }
        for (TextView v : mMyBetView) {
            setBetView(v, "0");
            v.setVisibility(View.INVISIBLE);
        }
        mGameTips.setText(WordUtil.getString(R.string.game_lh_please_bet));
        mBetConfirm.setEnabled(true);
        for (View v : mLlBetClick) {
            v.setEnabled(true);
        }
    }


}
